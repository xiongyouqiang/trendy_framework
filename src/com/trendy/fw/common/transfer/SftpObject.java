package com.trendy.fw.common.transfer;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SftpObject {
	private static Logger log = LoggerFactory.getLogger(SftpObject.class);

	private Session session = null;
	private Channel channel = null;

	public ChannelSftp getChannel(SftpConfigBean bean) {
		try {
			JSch jsch = new JSch(); // 创建JSch对象
			session = jsch.getSession(bean.getUser(), bean.getHost(), bean.getPort()); // 根据用户名，主机ip，端口获取一个Session对象
			session.setPassword(bean.getPassword()); // 设置密码
			Properties properties = new Properties();
			properties.put("StrictHostKeyChecking", "no");
			session.setConfig(properties); // 为Session对象设置properties
			session.setTimeout(bean.getTimeout()); // 设置timeout时间
			session.connect(); // 通过Session建立链接

			channel = session.openChannel("sftp"); // 打开SFTP通道
			channel.connect(); // 建立SFTP通道的连接
		} catch (JSchException jsche) {
			log.error("[Create ChannelSftp Error]:", jsche);
		}

		return (ChannelSftp) channel;
	}

	public void close() {
		try {
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
		} catch (Exception e) {
			log.error("[Close ChannelSftp Error]:", e);
		}
	}
}
