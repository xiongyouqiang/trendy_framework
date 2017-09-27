package com.trendy.fw.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.trendy.fw.common.config.Constants;

public class FileKit {

	/**
	 * 复制文件
	 * 
	 * @param srcPath
	 *            源文件路径
	 * @param destPath
	 *            目标文件路径
	 * @throws IOException
	 */
	public static void copyFile(String srcPath, String destPath) throws IOException {
		FileInputStream in = new FileInputStream(srcPath);
		FileOutputStream out = new FileOutputStream(destPath);
		FileChannel ifc = in.getChannel();// 得到文件输入流的通道
		FileChannel ofc = out.getChannel(); // 分配一个字节缓冲区，大小为1024
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		while (true) {
			buffer.clear();// 清空缓冲区，使其处于可接受字节状态
			int i = ifc.read(buffer);// 从文件输入流通道里读取数据，大小取决于缓冲区大小，以及文件剩余字节大小
			if (i == -1) {// 如果返回值为-1表示已读取完毕
				break;
			}
			buffer.flip();// 反转缓冲区，使其处于可写入通道状态
			ofc.write(buffer); // 把缓冲区数据写入文件输出流通道
		}
		in.close();
		out.close();
	}

	/**
	 * 写入文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param content
	 *            内容
	 * @param charset
	 *            编码
	 * @throws IOException
	 */
	public static void writeFile(String filePath, String content, String charset) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			String dir = parseDir(filePath);
			createDir(dir);
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file, true);
		out.write(content.getBytes(charset));
		out.close();
	}

	/**
	 * 重命名文件
	 * 
	 * @param srcPath
	 *            源文件路径
	 * @param destPath
	 *            目标文件路径
	 * @return
	 */
	public static boolean renameFile(String srcPath, String destPath) {
		boolean result = false;
		if (!srcPath.equals(destPath)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
			File srcFile = new File(srcPath);
			File destFile = new File(destPath);
			if (destFile.exists()) {// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
				result = true;
			} else {
				result = srcFile.renameTo(destFile);
			}
		}
		return result;
	}

	/**
	 * 更改文件路径
	 * 
	 * @param fileName
	 *            文件名
	 * @param srcPath
	 *            源文件路径
	 * @param destPath
	 *            目标文件路径
	 * @param isCover
	 *            是否覆盖
	 * @return
	 */
	public static boolean changeDirectory(String fileName, String srcPath, String destPath, boolean isCover) {
		boolean result = false;
		if (!srcPath.equals(destPath)) {
			File srcFile = new File(srcPath + "/" + fileName);
			File destFile = new File(destPath + "/" + fileName);
			if (destFile.exists()) {// 若在待转移目录下，已经存在待转移文件
				if (isCover) {// 覆盖
					result = srcFile.renameTo(destFile);
				} else {
					result = true;
				}
			} else {
				result = srcFile.renameTo(destFile);
			}
		}
		return result;
	}

	/**
	 * 读取文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param charset
	 *            编码
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String filePath, String charset) throws IOException {
		return new String(readFile(filePath).getBytes(), charset);
	}

	public static String readFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists() || file.isDirectory()) {
			throw new FileNotFoundException();
		}
		return readFile(file);
	}

	public static String readFile(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String content = null;
		StringBuilder sb = new StringBuilder();
		content = br.readLine();
		while (content != null) {
			sb.append(content);
			content = br.readLine();
		}
		br.close();
		return sb.toString();
	}

	public static String readFile(InputStream ins) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(ins));
		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\r\n");
		}
		br.close();
		return sb.toString();
	}

	public static boolean existFile(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * 列出目录下指定后缀的文件
	 * 
	 * @param dirPath
	 *            目录路径
	 * @param suffixName
	 *            后缀名称
	 * @return
	 */
	public static File[] listFiles(String dirPath, final String suffixName) {
		File file = new File(dirPath);
		return file.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith("." + suffixName.toLowerCase());
			}
		});
	}

	/**
	 * 列出目录下所有文件
	 * 
	 * @param dirPath
	 *            目录路径
	 * @return
	 */
	public static File[] listFiles(String dirPath) {
		File file = new File(dirPath);
		return file.listFiles();
	}

	/**
	 * 创建文件夹
	 * 
	 * @param dirPath
	 *            文件夹路径
	 * @return
	 */
	public static boolean createDir(String dirPath) {
		boolean result = false;
		File dir = new File(dirPath);
		if (!dir.exists()) {
			result = dir.mkdirs();
		}
		return result;
	}

	/**
	 * 获取文件路径
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	public static String parseDir(String filePath) {
		String result = "";
		int pose = filePath.lastIndexOf(Constants.FILE_SEPARATOR);
		if (pose > 0) {
			result = filePath.substring(0, pose);
		}
		return result;
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @param dirPath
	 *            文件夹路径
	 */
	public static void deleteDir(String dirPath) {
		File dir = new File(dirPath);
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDir(dirPath + "/" + files[i].getName());
				} else {
					files[i].delete();
				}
			}
			dir.delete();
		}
	}

	/**
	 * 将数据流转换成文件
	 * 
	 * @param ins
	 * @param filePath
	 *            文件路径
	 * @throws IOException
	 */
	public static void inputStream2File(InputStream ins, String filePath) throws IOException {
		inputStream2File(ins, new File(filePath));
	}

	/**
	 * 将数据流转换成文件
	 * 
	 * @param ins
	 * @param file
	 *            文件
	 * @throws IOException
	 */
	public static void inputStream2File(InputStream ins, File file) throws IOException {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[1024];
		while ((bytesRead = ins.read(buffer, 0, 1024)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
	}

	public static void bytes2File(byte[] bytes, String filePath) throws IOException {
		bytes2File(bytes, new File(filePath));
	}

	public static void bytes2File(byte[] bytes, File file) throws IOException {
		OutputStream os = new FileOutputStream(file);
		os.write(bytes);
		os.close();
	}
}
