/************************************************************************ 
版权所有 (C)2010, 深圳市康佳集团股份有限公司。
 *  
 * 项目名称：KKUnionReport                   
 * 文件名称：FileUtil.java            
 * 文件标识：          
 * 内容摘要：
 * 其它说明： 
 * 当前版本：             
 * 作 者：         邱剑锋         
 * 完成日期：2013-5-22             
 * 修改记录： 
 * 修改日期：                 
 * 版 本 号：                 
 * 修 改 人：                 
 * 修改内容：                 
 ************************************************************************/
package cloudservices.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class FileUtil {
	//private static final Logger logger = Logger.getLogger(FileUtil.class);

	/**
	 * 保存字符串到文件(NIO)
	 * 
	 * @param filePath
	 *            文件完整路径（包括文件名）
	 * @param content
	 *            内容
	 * @throws Exception
	 */
	public void saveStringToFile(String filePath, String content)
			throws Exception {
		FileOutputStream fos = new FileOutputStream(filePath);
		FileChannel outChannel = fos.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);

		for (int i = 0; i < content.length(); i++) {
			buffer.putChar(content.charAt(i));
		}
		buffer.flip();
		outChannel.write(buffer);

		outChannel.close();
		fos.close();
	}

	/**
	 * 保存字符串到文件(IO)
	 * 
	 * @param filePath
	 * @param content
	 * @throws Exception
	 */
	public void writeFile(String filePath, String content) throws Exception {
		File f = new File(filePath);
		if (!f.exists()) {
			f.createNewFile();
		}
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(
				f), "UTF-8");
		BufferedWriter writer = new BufferedWriter(write);
		writer.write(content);
		writer.close();
	}

	/**
	 * 一个字符一个字符的读取速度比较慢，待优化
	 * 
	 * @param path
	 * @return
	 */
	public static String readFile(String path) {
		//logger.debug(path);
		String result = "";
		try {
			File f = new File(path);
			InputStream in = new FileInputStream(f);
			StringBuffer b = new StringBuffer();
			int c;
			while ((c = in.read()) != -1) {
				b.append((char) c);
			}
			result = new String(b.toString().getBytes("ISO-8859-1"), "gbk");
		} catch (Exception e) {
			//logger.error("读取模板文件发生异常:" + e);
		}
		return result;

	}

	/**
	 * 文件另存为
	 * 
	 * @param directory
	 *            存储的文件夹路径
	 * @param fileName
	 *            文件名称
	 * @param file
	 *            目标文件
	 * @return
	 * @throws IOException
	 */
	public static void fileSaveAs(File sourceFile, File targetFile) throws IOException {
		// 新建文件输入流并对它进行缓冲   
        FileInputStream input = new FileInputStream(sourceFile);  
        BufferedInputStream inBuff=new BufferedInputStream(input);  
  
        // 新建文件输出流并对它进行缓冲   
        FileOutputStream output = new FileOutputStream(targetFile);  
        BufferedOutputStream outBuff=new BufferedOutputStream(output);  
          
        // 缓冲数组   
        byte[] b = new byte[1024 * 5];  
        int len;  
        while ((len =inBuff.read(b)) != -1) {  
            outBuff.write(b, 0, len);  
        }  
        // 刷新此缓冲的输出流   
        outBuff.flush();  
          
        //关闭流   
        inBuff.close();  
        outBuff.close();  
        output.close();  
        input.close();  
	}

	// 复制文件夹
	public static void directiorySaveAs(String sourceDir, String targetDir)
			throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(new File(targetDir).getAbsolutePath() 
								+ File.separator
								+ file[i].getName());
				fileSaveAs(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				directiorySaveAs(dir1, dir2);
			}
		}
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static boolean mkdirs(String path) {
		File savePath = new File(path);
		if (!savePath.exists() || !savePath.isDirectory()) {
			return savePath.mkdirs();
		}
		return true;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public static boolean deleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}
}
