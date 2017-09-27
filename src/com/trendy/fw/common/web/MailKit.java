package com.trendy.fw.common.web;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.bean.SendMailBean;
import com.trendy.fw.common.bean.SimpleAuthenticator;
import com.trendy.fw.common.config.Constants;
import com.trendy.fw.common.util.EncodeKit;
import com.trendy.fw.common.util.StringKit;

public class MailKit {
	private static Logger log = LoggerFactory.getLogger(MailKit.class);

	public static final String DEFAULT_CONTENT_TYPE = "text/plain";

	/**
	 * 发送邮件
	 * 
	 * @param bean
	 * @return
	 */
	public static boolean sendMail(SendMailBean bean) {
		boolean result = false;
		try {
			// 处理邮件属性
			Properties properties = new Properties();
			properties.put("mail.user", bean.getSendUserName());
			properties.put("mail.from",
					StringKit.isValid(bean.getSendFrom()) ? bean.getSendFrom() : bean.getSendUserName());
			properties.put("mail.smtp.user", bean.getSendUserName());
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.allow8bitmime", "true");
			properties.put("mail.host", bean.getSmtpServer());
			properties.put("mail.smtp.host", bean.getSmtpServer());
			properties.put("mail.smtp.connectiontimeout", bean.getConnectionTimeout());
			properties.put("mail.smtp.timeout", bean.getSocketTimeout());

			// 建立与邮件服务器会话
			Session session = Session.getInstance(properties,
					new SimpleAuthenticator(bean.getSendUserName(), bean.getSendUserPwd()));
			session.setDebug(false);

			// 处理邮件内容
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setSubject(bean.getSubject(), bean.getCharset());

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			if (bean.getContentType() == null || (bean.getContentType().trim().length() == 0)) {
				bean.setContentType(DEFAULT_CONTENT_TYPE);
			}

			if (bean.getContentType().equals(DEFAULT_CONTENT_TYPE)) {
				mimeBodyPart.setText(bean.getContent(), bean.getCharset());
			} else {
				mimeBodyPart.setContent(EncodeKit.toSystemCode(bean.getContent()), bean.getContentType());
			}

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart, 0);

			// 处理发送、接收人员
			InternetAddress fromAddress = new InternetAddress(bean.getSendFrom());
			mimeMessage.setFrom(fromAddress);
			mimeMessage.setRecipients(Message.RecipientType.TO, parseAddresses(bean.getToUserList()));
			mimeMessage.setRecipients(Message.RecipientType.CC, parseAddresses(bean.getCcUserList()));
			mimeMessage.setRecipients(Message.RecipientType.BCC, parseAddresses(bean.getBccUserList()));

			// 处理附件
			if (bean.getAttachmentList() != null && bean.getAttachmentList().size() > 0) {
				for (String fileName : bean.getAttachmentList()) {
					MimeBodyPart attachmentMbp = parseAttachment(fileName);
					multipart.addBodyPart(attachmentMbp);
				}
			}

			// 发送邮件
			mimeMessage.setHeader("X-Mailer", "Java Mailer");
			mimeMessage.setSentDate(new Date());
			mimeMessage.setContent(multipart);
			Transport.send(mimeMessage);

			result = true;
		} catch (AddressException e) {
			log.error("邮件发送时出错[AddressException]：", e);
		} catch (MessagingException e) {
			log.error("邮件发送时出错[MessagingException]：", e);
		} catch (Exception e) {
			log.error("邮件发送时出错[Exception]：", e);
		}
		return result;
	}

	/**
	 * 处理发送的邮件地址
	 * 
	 * @param list
	 * @return
	 * @throws MessagingException
	 */
	private static InternetAddress[] parseAddresses(List<String> list) throws MessagingException {
		if (list == null || list.size() == 0) {
			return null;
		}
		InternetAddress[] toAddresses = new InternetAddress[list.size()];
		for (int i = 0; i < list.size(); i++) {
			String str = list.get(i);
			InternetAddress toAddress = new InternetAddress(str);
			toAddresses[i] = toAddress;
		}
		return toAddresses;
	}

	/**
	 * 处理邮件附件
	 * 
	 * @param fileName
	 * @return
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	private static MimeBodyPart parseAttachment(String fileName) throws MessagingException,
			UnsupportedEncodingException {
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		FileDataSource fds = new FileDataSource(fileName);
		mimeBodyPart.setDataHandler(new DataHandler(fds));
		mimeBodyPart.setDescription("attachment");

		String attachmentName = fileName.substring(fileName.lastIndexOf(Constants.FILE_SEPARATOR) + 1);
		mimeBodyPart.setFileName(MimeUtility.encodeWord(attachmentName));

		return mimeBodyPart;
	}
}
