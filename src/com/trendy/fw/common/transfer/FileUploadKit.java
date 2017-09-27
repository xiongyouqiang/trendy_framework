package com.trendy.fw.common.transfer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.config.Constants;
import com.trendy.fw.common.transfer.FileUploadResultBean.FileUploadItem;
import com.trendy.fw.common.util.FileKit;
import com.trendy.fw.common.util.ListKit;
import com.trendy.fw.common.util.StringKit;

public class FileUploadKit {
	protected static Logger log = LoggerFactory.getLogger(FileUploadKit.class);
	private long maxSize = 20 * 1024 * 1024;// 单个文件最大限制，默认20M
	private String allowedFileType = "";// 限定文件类型，字符串以逗号进行分隔
	private String characterEncoding = Constants.CODE_UNICODE;
	private boolean lowerExtensionFileName = false;

	// 输出文件类型
	public static final String OFT_FILE = "FILE";// 文件
	public static final String OFT_STREAM = "STREAM";// 文件流
	public static final String OFT_BYTE = "BYTE";// byte流

	public long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	public String getAllowedFileType() {
		return allowedFileType;
	}

	public void setAllowedFileType(String allowedFileType) {
		this.allowedFileType = allowedFileType;
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

	public boolean isLowerExtensionFileName() {
		return lowerExtensionFileName;
	}

	public void setLowerExtensionFileName(boolean lowerExtensionFileName) {
		this.lowerExtensionFileName = lowerExtensionFileName;
	}

	public List<FileItem> getUploadItemList(HttpServletRequest request, FileUploadResultBean resultBean) {
		DiskFileItemFactory factory = new DiskFileItemFactory();// DiskFileItem工厂,主要用来设定上传文件的参数
		factory.setSizeThreshold(4 * 1024);// 上传文件所用到的缓冲区大小,超过此缓冲区的部分将被写入到磁盘
		ServletFileUpload upload = new ServletFileUpload(factory);// 使用fileItemFactory为参数实例化一个ServletFileUpload对象
		upload.setSizeMax(maxSize);// 要求上传的form-data数据不超过maxSize
		upload.setHeaderEncoding(characterEncoding);

		// java.util.List实例来接收上传的表单数据和文件数据
		List<FileItem> itemList = null;
		try {
			itemList = upload.parseRequest(request);
			resultBean.setResult(true);
		} catch (FileUploadBase.SizeLimitExceededException e) {// 请求数据的size超出了规定
			resultBean.setResult(false);
			resultBean.setResultContent("文件大小超出规定");
			log.error("文件大小超出规定：", e);
		} catch (FileUploadBase.InvalidContentTypeException e) {// 无效的请求类型
			resultBean.setResult(false);
			resultBean.setResultContent("请求类型enctype != multipart/form-data");
			log.error("无效的请求类型：", e);
		} catch (FileUploadException e) {// 如果都不是以上子异常,则抛出此总的异常,出现此异常原因无法说明.
			resultBean.setResult(false);
			resultBean.setResultContent("文件上传过程中出现错误");
			log.error("文件上传过程中出现错误：", e);
		}
		return itemList;
	}

	public FileUploadResultBean fileUpload(HttpServletRequest request, String fieldName, String outputPath,
			String outputFileName) {
		FileUploadBean uploadBean = new FileUploadBean(fieldName, outputPath, outputFileName);
		List<FileUploadBean> uploadList = new ArrayList<FileUploadBean>();
		uploadList.add(uploadBean);
		return fileUpload(request, uploadList, OFT_FILE);
	}

	public FileUploadResultBean fileUpload(HttpServletRequest request, String fieldName, String outputFileType) {
		FileUploadBean uploadBean = new FileUploadBean(fieldName);
		List<FileUploadBean> uploadList = new ArrayList<FileUploadBean>();
		uploadList.add(uploadBean);
		return fileUpload(request, uploadList, outputFileType);
	}

	public FileUploadResultBean fileUpload(HttpServletRequest request, List<FileUploadBean> uploadList,
			String outputFileType) {
		FileUploadResultBean resultBean = new FileUploadResultBean();

		List<FileItem> itemList = getUploadItemList(request, resultBean);

		if (!resultBean.getResult()) {
			log.error("文件上传失败");
			return resultBean;
		}
		HashMap<String, FileUploadBean> uploadMap = parseUploadMap(uploadList);

		if (itemList != null) {// 如果itemList不为空则使用iterator遍历它
			Iterator<FileItem> it = itemList.iterator();
			while (it.hasNext()) {
				FileItem item = (FileItem) it.next();
				if (item.isFormField()) {
					try {
						resultBean.setParamMapValue(item.getFieldName(), item.getString(characterEncoding));
					} catch (Exception e) {
						log.error("", e);
						resultBean.setParamMapValue(item.getFieldName(), item.getString());
					}
				} else {// 文件
					String uploadFieldName = item.getFieldName();
					if (uploadMap.containsKey(uploadFieldName)) {// 判断是否在字段中
						FileUploadBean uploadBean = uploadMap.get(uploadFieldName);

						String uploadFileName = item.getName();
						if (isAllowedFileType(uploadFileName)) {// 判断文件是否符合类型
							try {
								FileUploadItem fileUploadItem = resultBean.new FileUploadItem();
								fileUploadItem.setFieldName(item.getFieldName());
								fileUploadItem.setFileName(uploadFileName);
								if (outputFileType.equals(OFT_FILE)) {// 输出到文件
									FileKit.createDir(uploadBean.getOutputPath());
									String filePath = uploadBean.getOutputPath() + Constants.FILE_SEPARATOR
											+ parseFileName(uploadFileName, uploadBean.getOutputFileName());
									item.write(new File(filePath));
									fileUploadItem.setFilePath(filePath);
								} else if (outputFileType.equals(OFT_STREAM)) {
									fileUploadItem.setFileStream(item.getInputStream());
								} else if (outputFileType.equals(OFT_BYTE)) {
									fileUploadItem.setFileBytes(item.get());
								}
								resultBean.setFileItemListValue(fileUploadItem);
							} catch (Exception e) {
								resultBean.setResult(false);
								resultBean.setResultContent("文件写入失败");
								return resultBean;
							}
						}
					} else {
						log.error("文件[{}]{}不符合类型", item.getFieldName(), item.getName());
					}
				}
			}
		} else {
			resultBean.setResult(false);
			resultBean.setResultContent("上传内容为空");
			return resultBean;
		}

		return resultBean;
	}

	private boolean isAllowedFileType(String fileName) {
		if (StringKit.isValid(allowedFileType)) {
			List<String> fileTypeList = new ArrayList<String>();
			fileTypeList = ListKit.string2List(allowedFileType, ",");
			for (String fileType : fileTypeList) {
				if (fileName.toLowerCase().lastIndexOf("." + fileType.toLowerCase()) > 0) {
					return false;
				}
			}
		}
		return true;
	}

	private HashMap<String, FileUploadBean> parseUploadMap(List<FileUploadBean> uploadList) {
		HashMap<String, FileUploadBean> uploadMap = new HashMap<String, FileUploadBean>();
		for (FileUploadBean bean : uploadList) {
			uploadMap.put(bean.getFieldName(), bean);
		}
		return uploadMap;
	}

	private String parseFileName(String fileName, String outputFileName) {
		String extName = fileName.substring(fileName.lastIndexOf("."));
		if (lowerExtensionFileName) {
			extName = extName.toLowerCase();
		}
		return outputFileName + extName;
	}
}
