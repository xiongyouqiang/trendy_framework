package com.trendy.fw.common.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.trendy.fw.common.web.HttpResponseKit;

public class CsvKit {
	private static Logger log = LoggerFactory.getLogger(CsvKit.class);

	private static char DEFAULT_SEPARATOR = ',';// 默认分隔符
	private static char DEFAULT_QUOTE_CHARACTER = '"';// 默认引用符号

	/**
	 * 读取CSV文件
	 * 
	 * @param fileName
	 *            文件名
	 * @param charSet
	 *            字符类型
	 * @return List<String[]>
	 */
	public static List<String[]> readCsv(String fileName, String charset) {
		return readCsv(fileName, DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER, charset);
	}

	/**
	 * 读取CSV文件
	 * 
	 * @param fileName
	 *            文件名
	 * @param separator
	 *            分隔符
	 * @param quoteCharater
	 *            引用符号
	 * @param charSet
	 *            字符类型
	 * @return List<String[]>
	 */
	public static List<String[]> readCsv(String fileName, char separator, char quoteCharater, String charset) {
		List<String[]> list = new ArrayList<String[]>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charset));
			CSVReader reader = new CSVReader(br, separator, quoteCharater);
			list = reader.readAll();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("读取CSV文件时出错：", e);
		}
		return list;
	}

	/**
	 * 输出成文档
	 * 
	 * @param list
	 *            内容，类型List<String[]>
	 * @param fileName
	 *            文件名
	 * @param response
	 */
	public static void outputCsv(List<String[]> list, String fileName, HttpServletRequest request,
			HttpServletResponse response, String charset) {
		outputCsv(list, fileName, DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER, request, response, charset);
	}

	/**
	 * 输出成文档
	 * 
	 * @param list
	 *            内容，类型List<String[]>
	 * @param fileName
	 *            文件名
	 * @param separator
	 *            分隔符
	 * @param quoteCharater
	 *            字符类型
	 * @param response
	 */
	public static void outputCsv(List<String[]> list, String fileName, char separator, char quoteCharater,
			HttpServletRequest request, HttpServletResponse response, String charset) {
		try {
			response.reset();
			response.setContentType("text/csv;charset=UTF-8");
			HttpResponseKit.setAttachmentFile(request, response, fileName);

			CSVWriter writer = new CSVWriter(new OutputStreamWriter(response.getOutputStream(), charset), separator,
					quoteCharater);
			writer.writeAll(list);
			writer.close();

			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("写CSV文件时出错", e);
		}
	}

	/**
	 * 输出成文档
	 * 
	 * @param list
	 *            内容，类型List<String[]>
	 * @param fileName
	 *            文件名
	 */
	public static void outputCsv(List<String[]> list, String fileName, String charset) {
		outputCsv(list, fileName, DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER, charset);
	}

	/**
	 * 输出成文档
	 * 
	 * @param list
	 *            内容，类型List<String[]>
	 * @param fileName
	 *            文件名
	 * @param separator
	 *            分隔符
	 * @param quoteCharater
	 *            字符类型
	 */
	public static void outputCsv(List<String[]> list, String fileName, char separator, char quoteCharater,
			String charset) {
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fileName), charset);
			CSVWriter writer = new CSVWriter(out, separator, quoteCharater);
			writer.writeAll(list);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("输出CSV文件时出错：", e);
		}
	}
}
