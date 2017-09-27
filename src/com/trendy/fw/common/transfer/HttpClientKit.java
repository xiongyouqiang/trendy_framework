package com.trendy.fw.common.transfer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.config.Constants;

public class HttpClientKit {
	private static Logger log = LoggerFactory.getLogger(HttpClientKit.class);

	private static final String DEFAULT_CHARSET = Constants.CODE_UNICODE;// 默认编码
	private static final int DEFAULT_CONNECTION_TIMEOUT = 5000;// 默认链接超时时间，5s
	private static final int DEFAULT_SOCKET_TIMEOUT = 10000;// 默认socket超时时间，10s

	private int maxConnectionsPerHost = MultiThreadedHttpConnectionManager.DEFAULT_MAX_HOST_CONNECTIONS;
	private int maxTotalConnections = MultiThreadedHttpConnectionManager.DEFAULT_MAX_TOTAL_CONNECTIONS;
	private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
	private int socketTimeout = DEFAULT_SOCKET_TIMEOUT;

	public int getMaxConnectionsPerHost() {
		return maxConnectionsPerHost;
	}

	public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
		this.maxConnectionsPerHost = maxConnectionsPerHost;
	}

	public int getMaxTotalConnections() {
		return maxTotalConnections;
	}

	public void setMaxTotalConnections(int maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	private HttpClient getHttpClient() {
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		params.setDefaultMaxConnectionsPerHost(getMaxConnectionsPerHost());
		params.setMaxTotalConnections(getMaxTotalConnections());
		params.setConnectionTimeout(getConnectionTimeout());
		params.setSoTimeout(getSocketTimeout());

		MultiThreadedHttpConnectionManager connManager = new MultiThreadedHttpConnectionManager();
		connManager.setParams(params);

		HttpClient client = new HttpClient(connManager);
		return client;
	}

	public HttpClientResultBean getContent(String url, Map<String, String> paramMap) {
		return getContent(url, paramMap, DEFAULT_CHARSET);
	}

	public HttpClientResultBean getContent(String url, Map<String, String> paramMap, String charset) {
		Set<String> keySet = paramMap.keySet();
		Iterator<String> it = keySet.iterator();
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			String key = (String) it.next();
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(key);
			sb.append("=");
			sb.append(paramMap.get(key));
		}
		return getContent(url, charset);
	}

	public HttpClientResultBean getContent(String url, String charset) {
		HttpClientResultBean result = new HttpClientResultBean();
		HttpClient httpClient = getHttpClient();
		GetMethod getMethod = new CharsetGetMethod(url, charset);
		InputStream responseStream = null;
		try {
			int statusCode = httpClient.executeMethod(getMethod);// 执行提交
			if (statusCode == HttpStatus.SC_OK) {// 成功获取返回内容
				responseStream = getMethod.getResponseBodyAsStream();
				result.setResult(true);
				result.setResultByteContent(IOUtils.toByteArray(responseStream));
				result.setResultContent(new String(result.getResultByteContent(), charset));
			} else {
				responseStream = getMethod.getResponseBodyAsStream();
				result.setResult(false);
				result.setResultContent(new String(IOUtils.toByteArray(responseStream), charset));
			}
		} catch (Exception e) {
			result.setResult(false);
			result.setResultContent(e.toString());
			log.error("通过get方法获取Http内容错误：", e);
		} finally {
			if (responseStream != null) {
				try {
					responseStream.close();
					responseStream = null;
				} catch (Exception e) {
					log.error("关闭输出流时错误：", e);
				}
			}
			if (getMethod != null) {
				getMethod.releaseConnection();
				getMethod = null;
			}
		}
		return result;
	}

	public HttpClientResultBean postContent(String url, Map<String, String> paramMap) {
		return postContent(url, paramMap, DEFAULT_CHARSET);
	}

	public HttpClientResultBean postContent(String url, Map<String, String> paramMap, String charset) {
		Set<String> keySet = paramMap.keySet();
		Iterator<String> it = keySet.iterator();
		NameValuePair[] data = new NameValuePair[keySet.size()];
		int count = 0;
		while (it.hasNext()) {
			String key = (String) it.next();
			NameValuePair item = new NameValuePair(key, paramMap.get(key));
			data[count] = item;
			count++;
		}
		return postContent(url, data, charset);
	}

	public HttpClientResultBean postContent(String url, NameValuePair[] data, String charset) {
		HttpClientResultBean result = new HttpClientResultBean();
		HttpClient httpClient = getHttpClient();
		PostMethod postMethod = new CharsetPostMethod(url, charset);
		postMethod.getRequestCharSet();
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);
		// 执行postMethod
		try {
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result.setResult(true);
				result.setResultByteContent(IOUtils.toByteArray(postMethod.getResponseBodyAsStream()));
				result.setResultContent(new String(result.getResultByteContent(), charset));
			} else {
				result.setResult(false);
				postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
			}
		} catch (HttpException e) {
			result.setResult(false);
			result.setResultContent(e.toString());
			log.error("通过post方法获取Http内容错误[HttpException]：", e);
		} catch (IOException e) {
			result.setResult(false);
			result.setResultContent(e.toString());
			log.error("通过post方法获取Http内容错误[IOException]：", e);
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
				postMethod = null;
			}
		}
		return result;
	}

	public HttpClientResultBean postContent(String url, String data, String charset){
		return postContent(url, data, charset, null);
	}
	
	public HttpClientResultBean postContent(String url, String data, String charset,String contentType) {
		HttpClientResultBean result = new HttpClientResultBean();
		HttpClient httpClient = getHttpClient();
		PostMethod postMethod = new CharsetPostMethod(url, charset);
		postMethod.getRequestCharSet();
		
		// 执行postMethod
		try {
			// 将数据的值放入postMethod中
//			postMethod.setRequestEntity(new StringRequestEntity(data));
			postMethod.setRequestEntity(new StringRequestEntity(data, contentType, charset));
			
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result.setResult(true);
				result.setResultByteContent(IOUtils.toByteArray(postMethod.getResponseBodyAsStream()));
				result.setResultContent(new String(result.getResultByteContent(), charset));
			} else {
				result.setResult(false);
				postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
			}
		} catch (HttpException e) {
			result.setResult(false);
			result.setResultContent(e.toString());
			log.error("通过post方法获取Http内容错误[HttpException]：", e);
		} catch (IOException e) {
			result.setResult(false);
			result.setResultContent(e.toString());
			log.error("通过post方法获取Http内容错误[IOException]：", e);
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
				postMethod = null;
			}
		}
		return result;
	}
}
