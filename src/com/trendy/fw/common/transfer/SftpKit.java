package com.trendy.fw.common.transfer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;

public class SftpKit {
	private static Logger log = LoggerFactory.getLogger(SftpKit.class);

	private SftpConfigBean configBean = null;

	public SftpKit(SftpConfigBean configBean) {
		this.configBean = configBean;
	}

	/**
	 * 获取文件列表
	 * 
	 * @param remoteDir
	 *            远程目录
	 * @return
	 */
	public List<String> getFileList(String remoteDir) {
		List<String> list = new ArrayList<String>();
		SftpObject sftpObject = null;
		try {
			sftpObject = new SftpObject();
			ChannelSftp channel = sftpObject.getChannel(configBean);
			Vector vector = channel.ls(remoteDir);
			Iterator it = vector.iterator();
			while (it.hasNext()) {
				LsEntry entry = (LsEntry) it.next();
				if (!entry.getAttrs().isDir()) {
					list.add(entry.getFilename());
				}
			}
		} catch (Exception e) {
			log.error("获取文件夹下文件名称异常：", e);
		} finally {
			sftpObject.close();
		}
		return list;
	}

	/**
	 * 文件下载
	 * 
	 * @param fileName
	 *            文件名
	 * @param remoteDir
	 *            远程目录
	 * @param localDir
	 *            本地目录
	 * @return
	 */
	public boolean downloadFile(String fileName, String remoteDir, String localDir) {
		boolean result = false;
		SftpObject sftpObject = null;
		try {
			sftpObject = new SftpObject();
			ChannelSftp channel = sftpObject.getChannel(configBean);
			channel.get(getFilePath(remoteDir, fileName), getFilePath(localDir, fileName));
			result = true;
		} catch (Exception e) {
			log.error("从SFTP下载文件操作出错：", e);
		} finally {
			sftpObject.close();
		}
		return result;
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 *            文件名
	 * @param remoteDir
	 *            远程目录
	 * @return
	 */
	public boolean deleteFile(String fileName, String remoteDir) {
		boolean result = false;
		SftpObject sftpObject = null;
		try {
			sftpObject = new SftpObject();
			ChannelSftp channel = sftpObject.getChannel(configBean);
			channel.rm(getFilePath(remoteDir, fileName));
			result = true;
		} catch (Exception e) {
			log.error("从SFTP删除文件操作出错：", e);
		} finally {
			sftpObject.close();
		}
		return result;
	}

	/**
	 * 重命名文件
	 * 
	 * @param srcFileName
	 *            原文件名
	 * @param destFileName
	 *            新文件名
	 * @param remoteDir
	 *            远程目录
	 * @return
	 */
	public boolean renameFile(String srcFileName, String destFileName, String remoteDir) {
		boolean result = false;
		SftpObject sftpObject = null;
		try {
			sftpObject = new SftpObject();
			ChannelSftp channel = sftpObject.getChannel(configBean);
			channel.rename(getFilePath(remoteDir, srcFileName), getFilePath(remoteDir, destFileName));
			result = true;
		} catch (Exception e) {
			log.error("SFTP修改文件名操作出错：", e);
		} finally {
			sftpObject.close();
		}
		return result;
	}

	/**
	 * 批量上传文件
	 * 
	 * @param fileList
	 *            文件列表
	 * @param remoteDir
	 *            远程目录
	 * @param localDir
	 *            本地目录
	 * @return
	 */
	public boolean uploadFile(List<String> fileList, String remoteDir, String localDir) {
		boolean result = false;
		SftpObject sftpObject = null;
		try {
			sftpObject = new SftpObject();
			ChannelSftp channel = sftpObject.getChannel(configBean);
			for (String filePath : fileList) {
				try {
					makeRemoteDir(channel, remoteDir);
					channel.put(getFilePath(localDir, filePath), getFilePath(remoteDir, filePath));
				} catch (Exception ue) {
					log.error("批量上传文件至SFTP操作出错，文件名称=[{}]", filePath, ue);
				}
			}
			result = true;
		} catch (Exception e) {
			log.error("批量上传文件至SFTP异常：", e);
		} finally {
			sftpObject.close();
		}
		return result;
	}

	/**
	 * 单个文件上传
	 * 
	 * @param fileName
	 *            文件名
	 * @param remoteDir
	 *            远程目录
	 * @param localDir
	 *            本地目录
	 * @return
	 */
	public boolean uploadFile(String fileName, String remoteDir, String localDir) {
		boolean result = false;
		SftpObject sftpObject = null;
		try {
			sftpObject = new SftpObject();
			ChannelSftp channel = sftpObject.getChannel(configBean);
			makeRemoteDir(channel, remoteDir);
			channel.put(getFilePath(localDir, fileName), getFilePath(remoteDir, fileName));
			result = true;
		} catch (Exception e) {
			log.error("上传文件至SFTP操作出错：", e);
		} finally {
			sftpObject.close();
		}
		return result;
	}

	/**
	 * 上传文件
	 * 
	 * @param srcStream
	 *            文件流
	 * @param fileName
	 *            文件名
	 * @param remoteDir
	 *            远程目录
	 * @return
	 */
	public boolean uploadFile(InputStream srcStream, String fileName, String remoteDir) {
		boolean result = false;
		SftpObject sftpObject = null;
		try {
			sftpObject = new SftpObject();
			ChannelSftp channel = sftpObject.getChannel(configBean);
			makeRemoteDir(channel, remoteDir);
			channel.put(srcStream, getFilePath(remoteDir, fileName));
			result = true;
		} catch (Exception e) {
			log.error("上传文件至SFTP操作出错：", e);
		} finally {
			sftpObject.close();
		}
		return result;
	}

	public boolean makeRemoteDir(String remoteDir) {
		boolean result = false;
		SftpObject sftpObject = null;
		try {
			sftpObject = new SftpObject();
			ChannelSftp channel = sftpObject.getChannel(configBean);
			channel.mkdir(remoteDir);
			result = true;
		} catch (Exception e) {
			log.error("SFTP创建文件夹操作出错：", e);
		} finally {
			sftpObject.close();
		}
		return result;
	}

	public boolean removeRemoteDir(String remoteDir) {
		boolean result = false;
		SftpObject sftpObject = null;
		try {
			sftpObject = new SftpObject();
			ChannelSftp channel = sftpObject.getChannel(configBean);
			channel.rmdir(remoteDir);
			result = true;
		} catch (Exception e) {
			log.error("SFTP创建文件夹操作出错：", e);
		} finally {
			sftpObject.close();
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
	 * 判断是否存在目录，如果不存在创建新的目录
	 * 
	 * @param channel
	 * @param remoteDir
	 * @throws SftpException
	 */
	private void makeRemoteDir(ChannelSftp channel, String remoteDir) throws SftpException {
		try {
			channel.ls(remoteDir);
		} catch (SftpException se) {
			channel.mkdir(remoteDir);
		}
	}
}
