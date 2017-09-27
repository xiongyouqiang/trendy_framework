package com.trendy.fw.common.transfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPFile;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.trendy.fw.common.util.StringKit;

public class FtpKit {
	protected static Logger log = LoggerFactory.getLogger(FtpKit.class);

	private FtpConfigBean configBean = null;

	/**
	 * 初始化
	 * 
	 * @param configBean
	 */
	public FtpKit(FtpConfigBean configBean) {
		this.configBean = configBean;
	}

	/**
	 * 获取远程目录文件列表
	 * 
	 * @param remoteDir
	 * @return
	 */
	public List<String> getFileList(String remoteDir) {
		List<String> list = new ArrayList<String>();
		FtpObject ftpObject = null;
		try {
			ftpObject = new FtpObject();
			FileTransferClient ftpClient = ftpObject.getFtpClient(configBean);
			FTPFile[] files = ftpClient.directoryList(ftpClient.getRemoteDirectory() + remoteDir);
			for (FTPFile file : files) {
				list.add(file.getName());
			}
		} catch (Exception e) {
			log.error("获取文件夹下文件名称异常：", e);
		} finally {
			ftpObject.close();
		}
		return list;
	}

	/**
	 * 从FTP下载单个文件
	 * 
	 * @param fileName
	 * @param remoteDir
	 * @param localDir
	 * @return
	 */
	public boolean downloadFile(String fileName, String remoteDir, String localDir) {
		boolean result = false;
		FtpObject ftpObject = null;
		try {
			ftpObject = new FtpObject();
			FileTransferClient ftpClient = ftpObject.getFtpClient(configBean);
			ftpClient.changeDirectory(ftpClient.getRemoteDirectory() + remoteDir);
			// 下载文件
			ftpClient.downloadFile(getFilePath(localDir, fileName), fileName);
			result = true;
		} catch (Exception e) {
			log.error("从FTP下载文件操作出错：", e);
		} finally {
			ftpObject.close();
		}
		return result;
	}

	/**
	 * 从FTP批量下载文件
	 * 
	 * @param fileList
	 * @param remoteDir
	 * @param localDir
	 * @return
	 */
	public boolean downloadFile(List<String> fileList, String remoteDir, String localDir) {
		boolean result = false;
		FtpObject ftpObject = null;
		try {
			ftpObject = new FtpObject();
			FileTransferClient ftpClient = ftpObject.getFtpClient(configBean);
			ftpClient.changeDirectory(ftpClient.getRemoteDirectory() + remoteDir);
			for (String fileName : fileList) {
				try {
					ftpClient.downloadFile(getFilePath(localDir, fileName), fileName);
				} catch (Exception e) {
					log.error("FTP下载文件操作出错[{}]", fileName, e);
				}
			}
			result = true;
		} catch (Exception e) {
			log.error("从FTP批量下载文件操作出错：", e);
		} finally {
			ftpObject.close();
		}
		return result;
	}

	/**
	 * 从FTP删除单个文件
	 * 
	 * @param fileName
	 * @param remoteDir
	 * @return
	 */
	public boolean deleteFile(String fileName, String remoteDir) {
		boolean result = false;
		FtpObject ftpObject = null;
		try {
			ftpObject = new FtpObject();
			FileTransferClient ftpClient = ftpObject.getFtpClient(configBean);
			ftpClient.changeDirectory(ftpClient.getRemoteDirectory() + remoteDir);
			ftpClient.deleteFile(fileName);
			result = true;
		} catch (Exception e) {
			log.error("从FTP删除文件操作出错：", e);
		} finally {
			ftpObject.close();
		}
		return result;
	}

	/**
	 * 修改文件名
	 * 
	 * @param remoteDir
	 * @param srcFileName
	 * @param destFileName
	 * @return
	 */
	public boolean renameFile(String remoteDir, String srcFileName, String destFileName) {
		boolean result = false;
		FtpObject ftpObject = null;
		try {
			ftpObject = new FtpObject();
			FileTransferClient ftpClient = ftpObject.getFtpClient(configBean);
			ftpClient.changeDirectory(ftpClient.getRemoteDirectory() + remoteDir);
			ftpClient.rename(srcFileName, destFileName);
			result = true;
		} catch (Exception e) {
			log.error("FTP修改文件名操作出错：", e);
		} finally {
			ftpObject.close();
		}
		return result;
	}

	/**
	 * 上传单个文件至FTP服务器
	 * 
	 * @param fileName
	 * @param remoteDir
	 * @param localDir
	 * @return
	 */
	public boolean uploadFile(String fileName, String remoteDir, String localDir) {
		boolean result = false;
		FtpObject ftpObject = null;
		try {
			ftpObject = new FtpObject();
			FileTransferClient ftpClient = ftpObject.getFtpClient(configBean);
			changeRemoteDir(ftpClient, remoteDir);
			ftpClient.uploadFile(getFilePath(localDir, fileName), fileName);
			result = true;
		} catch (Exception e) {
			log.error("上传文件至FTP操作出错：", e);
		} finally {
			ftpObject.close();
		}
		return result;
	}

	/**
	 * 批量上传文件至FTP服务器
	 * 
	 * @param fileList
	 * @param remoteDir
	 * @param localDir
	 * @return
	 */
	public boolean uploadFile(List<String> fileList, String remoteDir, String localDir) {
		boolean result = false;
		FtpObject ftpObject = null;
		try {
			ftpObject = new FtpObject();
			FileTransferClient ftpClient = ftpObject.getFtpClient(configBean);
			changeRemoteDir(ftpClient, remoteDir);
			for (String fileName : fileList) {
				try {
					ftpClient.uploadFile(getFilePath(localDir, fileName), fileName);
				} catch (Exception e) {
					log.error("ftp上传异常[{}]", fileName, e);
				}
			}
			result = true;
		} catch (Exception e) {
			log.error("批量上传文件至ftp异常：", e);
		} finally {
			ftpObject.close();
		}
		return result;
	}

	/**
	 * 以流的形式上传文件
	 * 
	 * @param srcStream
	 * @param fileName
	 * @param remoteDir
	 * @return
	 */
	public boolean uploadFile(InputStream srcStream, String fileName, String remoteDir) {
		boolean result = false;
		// 连接FTP
		FtpObject ftpObject = null;
		try {
			ftpObject = new FtpObject();
			FileTransferClient ftpClient = ftpObject.getFtpClient(configBean);
			changeRemoteDir(ftpClient, remoteDir);
			OutputStream os = ftpClient.uploadStream(fileName);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = srcStream.read(bytes)) != -1) {
				os.write(bytes, 0, c);
			}
			os.close();
			os = null;
			result = true;
		} catch (Exception e) {
			log.error("上传文件至FTP操作出错：{}", e);
		} finally {
			ftpObject.close();
		}
		return result;
	}

	/**
	 * 在FTP服务器上创建目录
	 * 
	 * @param remoteDir
	 * @return
	 */
	public boolean createRemoteDir(String remoteDir) {
		boolean result = false;
		FtpObject ftpObject = null;
		try {
			ftpObject = new FtpObject();
			FileTransferClient ftpClient = ftpObject.getFtpClient(configBean);
			createRemoteDir(ftpClient, remoteDir);
			result = true;
		} catch (Exception e) {
			log.error("FTP创建文件夹操作出错：", e);
		} finally {
			ftpObject.close();
		}
		return result;
	}

	/**
	 * 删除FTP服务器上目录
	 * 
	 * @param remoteDir
	 * @return
	 */
	public boolean deleteRemoteDir(String remoteDir) {
		boolean result = false;
		FtpObject ftpObject = null;
		try {
			ftpObject = new FtpObject();
			FileTransferClient ftpClient = ftpObject.getFtpClient(configBean);
			ftpClient.deleteDirectory(ftpClient.getRemoteDirectory() + remoteDir);
			result = true;
		} catch (Exception e) {
			log.error("FTP创建文件夹操作出错：", e);
		} finally {
			ftpObject.close();
		}
		return result;
	}

	/**
	 * 获取文件全路径
	 * 
	 * @param dirPath
	 *            目录路径
	 * @param fileName
	 *            文件名
	 * @return
	 */
	private String getFilePath(String dirPath, String fileName) {
		return dirPath + "/" + fileName;
	}

	/**
	 * 更改目录路径，如果路径不存在创建目录
	 * 
	 * @param ftpClient
	 * @param remoteDir
	 * @throws IOException
	 * @throws FTPException
	 */
	private void changeRemoteDir(FileTransferClient ftpClient, String remoteDir) throws IOException, FTPException {
		try {
			ftpClient.changeDirectory(ftpClient.getRemoteDirectory() + remoteDir);
		} catch (FTPException fe) {
			createRemoteDir(ftpClient, remoteDir);
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param ftpClient
	 * @param remoteDir
	 * @throws IOException
	 * @throws FTPException
	 */
	private void createRemoteDir(FileTransferClient ftpClient, String remoteDir) throws IOException, FTPException {
		String[] dirNameArray = remoteDir.split("/");
		for (String dirName : dirNameArray) {
			if (StringKit.isValid(dirName.trim())) {
				try {
					ftpClient.changeDirectory(dirName);
				} catch (FTPException ftpe) {
					ftpClient.createDirectory(dirName);
					ftpClient.changeDirectory(dirName);
				}
			}
		}
	}
}
