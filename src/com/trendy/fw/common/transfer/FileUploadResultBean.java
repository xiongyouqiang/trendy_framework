package com.trendy.fw.common.transfer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileUploadResultBean {
	private boolean result = false;
	private String resultContent = "";
	private HashMap<String, String> paramMap = new HashMap<String, String>();
	private List<FileUploadItem> fileItemList = new ArrayList<FileUploadItem>();

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getResultContent() {
		return resultContent;
	}

	public void setResultContent(String resultContent) {
		this.resultContent = resultContent;
	}

	public HashMap<String, String> getParamMap() {
		return paramMap;
	}

	public void setParamMap(HashMap<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	public void setParamMapValue(String fieldName, String fieldValue) {
		paramMap.put(fieldName, fieldValue);
	}

	public List<FileUploadItem> getFileItemList() {
		return fileItemList;
	}

	public void setFileItemList(List<FileUploadItem> fileItemList) {
		this.fileItemList = fileItemList;
	}

	public void setFileItemListValue(FileUploadItem fileUploadItem) {
		fileItemList.add(fileUploadItem);
	}

	public class FileUploadItem {
		private String fieldName = "";
		private String fileName = "";
		private String filePath = "";
		private InputStream fileStream = null;
		private byte[] fileBytes = null;

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		public InputStream getFileStream() {
			return fileStream;
		}

		public void setFileStream(InputStream fileStream) {
			this.fileStream = fileStream;
		}

		public byte[] getFileBytes() {
			return fileBytes;
		}

		public void setFileBytes(byte[] fileBytes) {
			this.fileBytes = fileBytes;
		}
	}
}
