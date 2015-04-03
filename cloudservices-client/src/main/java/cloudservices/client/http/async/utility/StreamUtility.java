package cloudservices.client.http.async.utility;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : 桥下一粒砂 chenyoca@gmail.com
 * date   : 2012-10-23
 * 服务端响应流处理工具类
 */
public class StreamUtility {

	private final static int STREAM_EOF = -1;
	private final static int BYTE_CACHE_SIZE = 1024;
	private final static int FIND_CHARSET_CACHE_SIZE = 4 * 1024;
	private final static String DEFAULT_CHARSET = "utf-8";
	
	public static String extraCharsetFromHTML(String content){
		String encoding = null;
		 Matcher m = Pattern.compile("<meta.*charset=\"?([a-zA-Z0-9-_/]+)\"?").matcher(content);
		 if(m.find()){
        	encoding = m.group(1);
		 }
        return encoding;
	}
	
	public static String convertToString(InputStream is) throws Throwable{
	    if( null == is ) return null;
	    String encoding = DEFAULT_CHARSET;
	    InputStreamReader isr = new InputStreamReader(is,encoding);
		if(isr.markSupported()){
			isr.mark(FIND_CHARSET_CACHE_SIZE);
	        char[] cache = new char[FIND_CHARSET_CACHE_SIZE];
	        isr.read(cache);
	        isr.reset();
	        encoding = extraCharsetFromHTML(new String(cache));
	        if(encoding == null) encoding = DEFAULT_CHARSET;
	        isr = new InputStreamReader(is,encoding);
		}
		BufferedReader reader = new BufferedReader(isr);
		char cache[] = new char[BYTE_CACHE_SIZE];
		int cacheSize = STREAM_EOF;
		StringBuilder sb = new StringBuilder();
		while((cacheSize = reader.read(cache)) != STREAM_EOF){
			String data = new String(cache, 0, cacheSize);
			sb.append(data);
		}
		return sb.toString();
	}
	
	public static byte[] convertToByteArray(InputStream is) throws Throwable{
	    if( null == is ) return null;
		byte[] cache = new byte[ BYTE_CACHE_SIZE * 2 ];
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		for (int length = 0; (length = is.read(cache)) != STREAM_EOF;) {
		    buffer.write(cache, 0, length);
        }
		return buffer.toByteArray();
	}
	
	public static void closeSilently(InputStream is){
        if(is == null) return;
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
