package com.trendy.fw.common.excel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.trendy.fw.common.web.HttpResponseKit;

public class WebExcelWriteKit extends ExcelWriteKit {

	/**
	 * 输出成文档
	 * 
	 * @param wb
	 *            工作表
	 * @param fileName
	 *            文件名
	 * @param response
	 */
	public void output(Workbook wb, String fileName, HttpServletRequest request, HttpServletResponse response) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			wb.write(os);

			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);

			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			HttpResponseKit.setAttachmentFile(request, response, fileName);

			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(response.getOutputStream());

			byte[] buff = new byte[2048];
			int bytesRead;

			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (IOException e) {
			log.error("输出Excel出错:", e);
		} catch (Exception e) {
			log.error("输出Excel出错:", e);
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
				if (os != null)
					os.close();
			} catch (Exception e) {
				log.error("输出Excel出错:{}", e);
			}
		}
	}

	/**
	 * 从页面上输出Excel
	 * 
	 * @param bean
	 *            WriteExcelBean
	 * @param response
	 */
	public void outputExcel(ExcelWriteBean bean, HttpServletRequest request, HttpServletResponse response) {
		Workbook wb = writeExcel(bean);
		String fileName = bean.getFileName() + "." + bean.getFileType();
		output(wb, fileName, request, response);
	}
	
	/**
	 * 从页面上输出Excel多个sheet
	 * 
	 * @param bean
	 *            WriteExcelBean
	 * @param response
	 */
	public void outputExcel(List<ExcelWriteBean> excelWriteList, HttpServletRequest request, HttpServletResponse response) {
		Workbook wb = writeExcel(excelWriteList);
		String fileName = excelWriteList.get(0).getFileName() + "." + excelWriteList.get(0).getFileType();
		output(wb, fileName, request, response);
	}
}
