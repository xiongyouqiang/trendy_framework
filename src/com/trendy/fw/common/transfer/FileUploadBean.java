package com.trendy.fw.common.transfer;

public class FileUploadBean {
	private String fieldName = "";
	private String outputPath = "";
	private String outputFileName = "";

	public FileUploadBean(String fieldName) {
		this.fieldName = fieldName;
	}

	public FileUploadBean(String fieldName, String outputPath, String outputFileName) {
		this.fieldName = fieldName;
		this.outputPath = outputPath;
		this.outputFileName = outputFileName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public String getOutputFileName() {
		return outputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
}
