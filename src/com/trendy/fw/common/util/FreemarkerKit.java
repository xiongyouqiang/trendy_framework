package com.trendy.fw.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.config.Constants;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreemarkerKit {
	private static Logger log = LoggerFactory.getLogger(FreemarkerKit.class);
	private String DEFAULT_CHARSET = Constants.CODE_UNICODE;

	public FreemarkerKit() {

	}

	public FreemarkerKit(String charset) {
		DEFAULT_CHARSET = charset;
	}

	/**
	 * 获取Freemarker配置
	 * 
	 * @return
	 */
	public Configuration getConfiguration() {
		Configuration cfg = new Configuration();
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setEncoding(Locale.CHINA, DEFAULT_CHARSET);
		cfg.setNumberFormat("0.##########");
		return cfg;
	}

	/**
	 * 获取信息Map
	 * 
	 * @return
	 */
	public HashMap<String, Object> getContentMap() {
		HashMap<String, Object> contentMap = new HashMap<String, Object>();
		contentMap.put("HtmlKit", new HtmlStringKit());
		contentMap.put("StringKit", new StringKit());
		contentMap.put("DateKit", new DateKit());
		contentMap.put("ListKit", new ListKit());

		return contentMap;
	}

	/**
	 * 获取Template实体
	 * 
	 * @param templateContent
	 *            模版内容
	 * @return
	 * @throws Exception
	 */
	public Template getTemplate(String templateContent) throws Exception {
		Configuration cfg = getConfiguration();

		StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
		stringTemplateLoader.putTemplate("stringTemp", templateContent);
		cfg.setTemplateLoader(stringTemplateLoader);

		Template template = cfg.getTemplate("stringTemp");

		return template;
	}

	/**
	 * 获取Template实体
	 * 
	 * @param templatePath
	 *            模版路径
	 * @param templateName
	 *            模版名称
	 * @return
	 * @throws Exception
	 */
	public Template getTemplate(String templatePath, String templateName) throws Exception {
		Configuration cfg = getConfiguration();
		cfg.setDirectoryForTemplateLoading(new File(templatePath));
		Template template = cfg.getTemplate(templateName);
		return template;
	}

	/**
	 * 将模板内容合并成字符串
	 * 
	 * @param contentMap
	 *            内容Map
	 * @param templateContent
	 *            模版内容
	 * @return
	 */
	public String mergeTemplate(HashMap<String, Object> contentMap, String templateContent) {
		String result = "";
		try {
			Template template = getTemplate(templateContent);
			result = mergeTemplate(contentMap, template);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Freemarker生成内容时出错： ", e);
		}
		return result;
	}

	/**
	 * 将模板内容合并成字符串
	 * 
	 * @param contentMap
	 *            内容Map
	 * @param templatePath
	 *            模板路径
	 * @param templateName
	 *            模板名称
	 * @return
	 */
	public String mergeTemplate(HashMap<String, Object> contentMap, String templatePath, String templateName) {
		String result = "";
		try {
			Template template = getTemplate(templatePath, templateName);

			result = mergeTemplate(contentMap, template);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Freemarker生成内容时出错： ", e);
		}
		return result;
	}

	/**
	 * 将模板内容合并成字符串
	 * 
	 * @param contentMap
	 *            内容Map
	 * @param template
	 *            Template实体
	 * @return
	 * @throws Exception
	 */
	public String mergeTemplate(HashMap<String, Object> contentMap, Template template) throws Exception {
		String result = "";
		StringWriter stringWriter = new StringWriter();
		BufferedWriter out = new BufferedWriter(stringWriter);
		template.process(contentMap, out);
		StringReader stringReader = new StringReader(stringWriter.toString());
		BufferedReader bufferedReader = new BufferedReader(stringReader);
		String s = "";
		while ((s = bufferedReader.readLine()) != null) {
			result += s;
		}
		out.flush();
		out.close();

		return result;
	}

	/**
	 * 通过模版生成文件
	 * 
	 * @param contentMap
	 *            内容Map
	 * @param templatePath
	 *            模版路径
	 * @param templateName
	 *            模版名称
	 * @param filePath
	 *            生成文件路径
	 * @return
	 */
	public boolean generateFile(HashMap<String, Object> contentMap, String templatePath, String templateName,
			String filePath) {
		boolean result = false;
		try {
			Template template = getTemplate(templatePath, templateName);

			// 合成内容并生成文件
			generateFile(contentMap, template, filePath);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Freemarker生成内容时出错： ", e);
		}
		return result;
	}

	/**
	 * 
	 * @param contentMap
	 *            内容Map
	 * @param templateContent
	 *            模版内容
	 * @param filePath
	 *            生成文件路径
	 * @return
	 */
	public boolean generateFile(HashMap<String, Object> contentMap, String templateContent, String filePath) {
		boolean result = false;
		try {
			Template template = getTemplate(templateContent);

			// 合成内容并生成文件
			generateFile(contentMap, template, filePath);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Freemarker生成内容时出错： ", e);
		}
		return result;
	}

	/**
	 * 
	 * @param contentMap
	 *            内容Map
	 * @param template
	 *            Template实体
	 * @param filePath
	 *            生成文件路径
	 * @return
	 * @throws Exception
	 */
	public boolean generateFile(HashMap<String, Object> contentMap, Template template, String filePath)
			throws Exception {
		// 处理文件目录
		String dirPath = FileKit.parseDir(filePath);
		FileKit.createDir(dirPath);

		// 生成文件
		File file = new File(filePath);
		Writer writer = new OutputStreamWriter(new FileOutputStream(file), DEFAULT_CHARSET);
		template.process(contentMap, writer);
		writer.flush();
		writer.close();
		return true;
	}

	/**
	 * 输出模版内容
	 * 
	 * @param contentMap
	 *            内容Map
	 * @param templatePath
	 *            模版路径
	 * @param templateName
	 *            模版名称
	 * @param response
	 * @return
	 */
	public boolean outputContent(HashMap<String, Object> contentMap, String templatePath, String templateName,
			HttpServletResponse response) {
		boolean result = false;
		try {
			Template template = getTemplate(templatePath, templateName);

			// 合成内容并生成文件
			outputContent(contentMap, template, response);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Freemarker生成内容时出错： ", e);
		}
		return result;
	}

	/**
	 * 输出模版内容
	 * 
	 * @param contentMap
	 *            内容Map
	 * @param templateContent
	 *            模版内容
	 * @param response
	 * @return
	 */
	public boolean outputContent(HashMap<String, Object> contentMap, String templateContent,
			HttpServletResponse response) {
		boolean result = false;
		try {
			Template template = getTemplate(templateContent);

			// 合成内容并输出内容
			outputContent(contentMap, template, response);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Freemarker生成内容时出错： ", e);
		}
		return result;
	}

	/**
	 * 输出模版内容
	 * 
	 * @param contentMap
	 *            内容Map
	 * @param template
	 *            Template实体
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean outputContent(HashMap<String, Object> contentMap, Template template, HttpServletResponse response)
			throws Exception {
		Writer out = new OutputStreamWriter(response.getOutputStream(), DEFAULT_CHARSET);
		template.process(contentMap, out);
		out.flush();
		out.close();

		return true;
	}
}
