package cloudservices.client.http.async.support;

import cloudservices.client.http.async.AsyncHttpConnection;
import cloudservices.client.http.async.support.ParamsWrapper.NameValue;
import cloudservices.client.http.async.support.ParamsWrapper.PathParam;
import cloudservices.client.http.async.utility.MIMEType;
import cloudservices.client.http.async.utility.TextUtils;
import cloudservices.utils.StringUtil;

import java.io.*;
import java.net.*;


/**
 * @author : 桥下一粒砂
 * email  : chenyoca@gmail.com
 * date   : 2012-10-23
 * desc   : 简单的HTTP实现
 */
public class SimpleHttpInvoker extends RequestInvoker {
	
	public final static String DEFAULT_USER_AGENT = String.format("HttpConnection version %s", AsyncHttpConnection.VERSION);
	
	private static final String BOUNDARY = randomBoundary();
	private static final String MP_BOUNDARY = "--" + BOUNDARY;
	private static final String END_MP_BOUNDARY = "--" + BOUNDARY + "--";
	private static final String END_MP_BLOCK = "\r\n\r\n";
	private static final String MULTIPART_FORM_DATA = "multipart/form-data";
	private static final String XWWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
	private static final String STREAM  = "application/octet-stream";

    private static final CookieManager COOKIE_MANAGER;
    static {
        //System wide
        COOKIE_MANAGER = new CookieManager( null, CookiePolicy.ACCEPT_ALL );
        CookieHandler.setDefault(COOKIE_MANAGER);
    }

	@Override
	protected void execute() {
		HttpURLConnection httpConnection = null;
        try{
            final boolean isGetMethod = HttpMethod.GET.equals(method) && (params != null || !StringUtil.isEmpty(paramsString));
            URL targetURL = createURL(isGetMethod);
            httpConnection  = connect(targetURL);
            configHttpConnection(httpConnection);
            callback.onSubmit(targetURL, params);
            if( !isGetMethod ){
            	if (StringUtil.isEmpty(paramsString)) {
            		fillParamsToConnection(httpConnection, params);
            	} else {
            		fillParamsToConnection(httpConnection, paramsString);
            	}
                
            }
            // 设置自定义cookies
            if(customCookieStore != null && customCookieStore.getCookies().size() > 0) {
                httpConnection.setRequestProperty("Cookie",TextUtils.join(";", customCookieStore.getCookies()));
            }

            InputStream stream = httpConnection.getInputStream();
            CookieStore cookieStore = COOKIE_MANAGER.getCookieStore();
            // InputStream 由 onResponse 关闭
            callback.onResponse(cookieStore,stream,targetURL);
        }catch(IOException exp){
            exp.printStackTrace();
            callback.onConnectError(exp);
        }catch (Throwable exp){
            exp.printStackTrace();
            callback.onUncatchedError(exp);
        }
        if(httpConnection != null){
            httpConnection.disconnect();
        }
	}

	private URL createURL(boolean isGetMethod) throws MalformedURLException, UnsupportedEncodingException {
		String urlPath = url;
		if(isGetMethod){
			String strParam = StringUtil.isEmpty(paramsString) ? params.getStringParams() : paramsString;
			if(strParam != null) urlPath = url + "?" + strParam;
		}
		return new URL(urlPath);
	}

	private HttpURLConnection connect(URL targetURL) throws IOException {
		URLConnection connection = httpProxy == null ? targetURL.openConnection() : targetURL.openConnection(httpProxy);
		return (HttpURLConnection) connection;
	}

	private void configHttpConnection (HttpURLConnection httpConnection) throws ProtocolException {
		httpConnection.setConnectTimeout(httpConnectTimeout);
		httpConnection.setReadTimeout(httpSocketTimeout);
		httpConnection.setDoInput(true);
		httpConnection.setUseCaches( HttpMethod.GET.equals(method) );
		httpConnection.setRequestMethod(method.name());
		httpConnection.setRequestProperty("User-agent",DEFAULT_USER_AGENT);
	}

	
	/**
	 * 将参数填充到Http连接中
	 * @param conn Http连接
	 * @param params 参数组
	 * @throws IOException
	 */
	public static void fillParamsToConnection (final HttpURLConnection conn, ParamsWrapper params) throws IOException {
		if(params == null) return;
		conn.setDoOutput(true);
		if (params.streamParams != null) {
			conn.setRequestProperty("Content-Type", STREAM + "; boundary=" + BOUNDARY);
			conn.setRequestProperty("Content-length", "" + params.streamParams.length);
			DataOutputStream paramsOutStream = new DataOutputStream(conn.getOutputStream());
			paramsOutStream.write(params.streamParams);
			paramsOutStream.close();
		}
		else if(params.pathParamArray.isEmpty()){
			conn.setRequestProperty("Content-Type", XWWW_FORM_URLENCODED + "; boundary=" + BOUNDARY);
			DataOutputStream paramsOutStream = new DataOutputStream(conn.getOutputStream());
			paramsOutStream.write(params.getStringParams().getBytes());
			paramsOutStream.close();
		}else{
			conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
			DataOutputStream paramsOutStream = new DataOutputStream(conn.getOutputStream());
			final int nameValueCount = params.nameValueArray.size();
			for(int i=0;i<nameValueCount;i++) {
				NameValue param = params.nameValueArray.get(i);
				setStringParamForPost(paramsOutStream, param.name, param.value);
			}
			final int pathParamCount = params.pathParamArray.size();
			for(int i=0;i<pathParamCount;i++) {
				PathParam param = params.pathParamArray.get(i);
				setPathParamForPost(paramsOutStream, param.param.name, param.param.value, param.path);
			}
			paramsOutStream.flush();
			paramsOutStream.close();
		}
	}
	public static void fillParamsToConnection (final HttpURLConnection conn, String paramsString) throws IOException {
		if(StringUtil.isEmpty(paramsString)) return;
		conn.setDoOutput(true);
		
		conn.setRequestProperty("Content-Type", XWWW_FORM_URLENCODED + "; boundary=" + BOUNDARY);
		DataOutputStream paramsOutStream = new DataOutputStream(conn.getOutputStream());
		paramsOutStream.write(paramsString.getBytes());
		paramsOutStream.flush();
		paramsOutStream.close();
	}

	/**
	 * 将字符参数填充到POST参数块中
	 * @param out 参数输出流
	 * @param paramName 参数名
	 * @param value 参数对象
	 * @throws IOException
	 */
	public static void setStringParamForPost(OutputStream out,String paramName,String value) throws IOException{
		StringBuilder buffer = new StringBuilder();
		buffer.append(MP_BOUNDARY).append("\r\n");
		buffer.append("Content-Disposition: form-data; name=\"").append(paramName).append("\"").append(END_MP_BLOCK);
		buffer.append(value).append("\r\n");
		byte[] res = buffer.toString().getBytes();
		out.write(res);
	}

	/**
	 * 将文件参数填充到POST参数块中
	 * @param out 参数输出流
	 * @param paramName 参数名
	 * @param fileName 文件名
	 * @param filePath 文件路径
	 * @throws IOException
	 */
	public static void setPathParamForPost(OutputStream out, String paramName,String fileName,String filePath) throws IOException{
		StringBuilder buffer = new StringBuilder();
		buffer.append(MP_BOUNDARY).append("\r\n");
		buffer.append("Content-Disposition: form-data; name=\"").append(paramName).append("\"; ")
			.append("filename=\"").append(fileName).append("\"\r\n");
		final String contentType = MIMEType.getContentType(exportSuffix(filePath));
		buffer.append("Content-Type: ").append(contentType).append(END_MP_BLOCK);
		byte[] resourceSplitLine = buffer.toString().getBytes();
		FileInputStream input = null;
		try {
			out.write(resourceSplitLine);
			input = new FileInputStream(filePath);
			byte[] byteBuffer = new byte[10 * 1024];
			int EOF_SIZE = 0;
			while( (EOF_SIZE = input.read(byteBuffer)) != -1 ){
				out.write(byteBuffer, 0, EOF_SIZE);
			}
			out.write(END_MP_BLOCK.getBytes());
			out.write(END_MP_BOUNDARY.getBytes());
		}finally {
			if (null != input) {
				input.close();
			}
		}
	}
	
	public static void fillStreamParamToConnection() {
		
	}
	
	private static String exportSuffix(String path){
		return path.substring(path.lastIndexOf(".") + 1);
	}
	
	static String randomBoundary () {
        StringBuilder buffer = new StringBuilder("----BoundaryGenByInvoker");
        for (int t = 1; t < 12; t++) {
            long time = System.currentTimeMillis() + t;
            if (time % 3 == 0) {
                buffer.append((char) time % 9);
            } else if (time % 3 == 1) {
                buffer.append((char) (65 + time % 26));
            } else {
                buffer.append((char) (97 + time % 26));
            }
        }
        return buffer.toString();
    }

}
