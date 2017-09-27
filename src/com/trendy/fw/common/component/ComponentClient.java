package com.trendy.fw.common.component;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectPrx;
import Ice.ObjectPrxHelperBase;
import Ice.Properties;
import Ice.Util;

import com.trendy.fw.common.util.PropertiesKit;

public class ComponentClient {
	private static Logger log = LoggerFactory.getLogger(ComponentClient.class);

	// 各应用组件服务器配置
	private static String COMPONENT_CONFIG_PATH = "/config/component_client_config.xml";

	// 组件服务器客户端配置
	private static String COMPONENT_PROP_PATH = "config/component_client";

	private static Map<String, List<String>> configMap = new HashMap<String, List<String>>();

	// Helper结尾标识
	public static final String REGEX_HELPER_SUFFIX = "Helper";

	private static Communicator communicator = null;

	private static Random randomEndPoint = new Random();

	private static Random randomConnection = new Random();

	private static int connectionMax = 60;

	static {
		init();
	}

	/**
	 * 初始化配置
	 */
	private static void init() {
		initConfigMap();
		initComponentClient();
	}

	private static void initConfigMap() {
		log.debug("[组件服务客户端配置]配置文件地址：{}", COMPONENT_CONFIG_PATH);
		// 读取配置文件
		configMap = new HashMap<String, List<String>>();
		String operateTimeout = PropertiesKit.getBundleProperties(COMPONENT_PROP_PATH, "Client.Operate.Timeout");
		try {
			InputStreamReader in = new InputStreamReader(
					ComponentClient.class.getResourceAsStream(COMPONENT_CONFIG_PATH));
			SAXReader reader = new SAXReader();
			Document document = reader.read(in);
			Element root = document.getRootElement();
			Iterator<Element> iter = root.elementIterator("component_server");
			while (iter.hasNext()) {
				Element config = iter.next();
				String appId = config.elementText("app_id");
				log.debug(appId);

				List<String> serverList = new ArrayList<String>();
				Element servers = config.element("servers");
				Iterator<Element> serverIter = servers.elementIterator("server");
				while (serverIter.hasNext()) {
					Element serverElement = serverIter.next();
					String server = serverElement.getText();
					server = parseServerPath(server) + operateTimeout;
					serverList.add(server);
				}
				configMap.put(appId, serverList);
			}
			in.close();
			log.debug("[组件服务客户端配置]配置信息：{}", configMap);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[组件服务客户端配置]加载组件服务客户端配置文件异常：", e);
		}
	}

	private static String parseServerPath(String server) {
		String[] serverArray = server.split(":");
		return ":tcp -h " + serverArray[0] + " -p " + serverArray[1] + " -t ";
	}

	/**
	 * 初始化ICE服务端
	 */
	private static void initComponentClient() {
		// 读取配置文件
		Properties prop = Util.createProperties();
		Map<String, String> config = PropertiesKit.getBundleAllProperties(COMPONENT_PROP_PATH);
		for (String key : config.keySet()) {
			prop.setProperty(key, config.get(key));
		}

		InitializationData initData = new InitializationData();
		initData.properties = prop;
		communicator = Util.initialize(initData);

		// 初始化最大链接数
		connectionMax = Integer.parseInt(config.get("Client.Connection.Max"));
	}

	/**
	 * 获取代理类
	 * 
	 * @param appId
	 *            应用ID
	 * @param interfaceClazz
	 *            接口类
	 * @return 代理类实体
	 */
	public Object getProxy(String appId, Class interfaceClazz) {
		List<String> endPointList = configMap.get(appId);
		int index = randomEndPoint.nextInt(endPointList.size());
		String endPoint = endPointList.get(index);
		return getNativeProxy(endPoint, interfaceClazz);
	}

	private String getConnectionId() {
		StringBuilder sb = new StringBuilder();
		sb.append(randomConnection.nextInt(connectionMax));
		sb.append(randomConnection.nextInt(connectionMax));
		return sb.toString();
	}

	/**
	 * 获取本地代理类
	 * 
	 * @param endPoint
	 *            服务端地址
	 * @param interfaceClazz
	 *            接口类
	 * @return 代理类实体
	 */
	public Object getNativeProxy(String endPoint, Class interfaceClazz) {
		log.debug("端点:{}, 接口类:{}", endPoint, interfaceClazz);
		String proxyName = interfaceClazz.getName();
		ObjectPrx objectPrx = communicator.stringToProxy(proxyName + endPoint);
		ObjectPrxHelperBase helperBase = null;
		try {
			// 使用短连接
			objectPrx = objectPrx.ice_connectionId(getConnectionId());

			String helperName = proxyName + REGEX_HELPER_SUFFIX;
			Class helperClazz = Class.forName(helperName);
			helperBase = (ObjectPrxHelperBase) helperClazz.newInstance();
			helperBase.__copyFrom((ObjectPrx) objectPrx);
			return helperBase;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取组件Proxy错误：{}", e);
		}
		return null;
	}

	public static void closeCommunicator() {
		communicator.shutdown();
		communicator.destroy();
	}
}
