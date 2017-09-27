package com.trendy.fw.common.transfer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FileTransferClient;

public class FtpObject {
	private static Logger log = LoggerFactory.getLogger(FtpObject.class);

	private FileTransferClient ftpClient = null;

	/**
	 * 初始化FTP连接参数，连接FTP服务器
	 */
	public FileTransferClient getFtpClient(FtpConfigBean bean) {
		try {
			ftpClient = new FileTransferClient();

			ftpClient.setRemoteHost(bean.getHost());
			ftpClient.setUserName(bean.getUser());
			ftpClient.setPassword(bean.getPassword());
			ftpClient.setTimeout(bean.getTimeout());
			ftpClient.setRemotePort(bean.getPort());
			ftpClient.setContentType(bean.getContentType());
			ftpClient.getAdvancedFTPSettings().setConnectMode(bean.getConnectMode());
			ftpClient.connect();
		} catch (IOException e) {
			log.error("FTP连接IO出错：", e);
		} catch (FTPException e) {
			log.error("初始化FTP连接出错：", e);
		}
		return ftpClient;
	}

	/**
	 * 关闭FTP连接
	 */
	public void close() {
		try {
			ftpClient.disconnect();
			ftpClient = null;
		} catch (Exception e) {
			log.error("[FTP配置]关闭FTP连接操作出错：", e);
		}
	}
}
