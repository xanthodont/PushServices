package cloudservices.client.http.async.support;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * @author : 桥下一粒砂 chenyoca@gmail.com
 * date   : 2012-10-23
 * A http connection parameters wrapper
 */
public class ParamsWrapper implements Serializable {

	/**
	 * 序列化Id
	 */
	private static final long serialVersionUID = 1L;
	public final ArrayList<NameValue> nameValueArray = new ArrayList<NameValue>();
	public final ArrayList<PathParam> pathParamArray = new ArrayList<PathParam>();
	public byte[] streamParams;
	
	public static class NameValue{
		public final String name;
		public final String value;
		public NameValue(String name,Object value){
			this.name = name;
			this.value = String.valueOf(value);
		}
	}
	
	public static class PathParam{
		public final NameValue param;
		public final String path;
		public PathParam(String name, Object value, String path){
			this.param = new NameValue(name,value);
			this.path = path;
		}
	}
	
	public ParamsWrapper put(String name,String value){
		appendToParamsArray(name,value);
		return this;
	}
	
	public ParamsWrapper put(String name,int value){
		appendToParamsArray(name,value);
		return this;
	}

	public ParamsWrapper put(String name,boolean value){
		appendToParamsArray(name,value);
		return this;
	}

	public ParamsWrapper put(String name,float value){
		appendToParamsArray(name,value);
		return this;
	}

	public ParamsWrapper put(String name,long value){
		appendToParamsArray(name,value);
		return this;
	}

	public ParamsWrapper put(String name,double value){
		appendToParamsArray(name,value);
		return this;
	}
	
	public ParamsWrapper put(String name,String fileName,String path){
		pathParamArray.add(new PathParam(name, fileName, path));
		return this;
	}

	public boolean containsValue(String name){
		boolean contains = false;
		if( name == null ) return contains;
		for(NameValue item : nameValueArray){
			if( name.equalsIgnoreCase( item.name )){
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	private ParamsWrapper appendToParamsArray(String name,Object value){
		if(name != null && value != null 
				&& !"".equals(name) && !"".equals(value) ){
			nameValueArray.add(new NameValue(name, value));
		}
		return this;
	}
	
	@Override
	public String toString(){
		try {
			return getStringParams();
		} catch (UnsupportedEncodingException exp) {
			exp.printStackTrace();
			return "Params contains unsupported encoding content!";
		}
	}
	
	/**
	 * 取出其参数组
	 * @return 参数字符串 i.e:"name=chenyoca&age=24&email=chenyoca@gmail.com"
	 * @throws UnsupportedEncodingException 
	 */
	public String getStringParams() throws UnsupportedEncodingException{
		return getStringParams("utf-8");
	}
	
	/**
	 * 取出字符型参数组，并指定URL参数编码类型
	 * @param urlEncoding 编码类型
	 * @return null或者参数字符串 i.e:"name=chenyoca&age=24&email=chenyoca@gmail.com"
	 * @throws UnsupportedEncodingException
	 */
	public String getStringParams(String urlEncoding) throws UnsupportedEncodingException{
		StringBuilder buffer = new StringBuilder();
		for(NameValue param : nameValueArray){
			buffer.append(param.name).append("=")
				.append(URLEncoder.encode(param.value,urlEncoding))
				.append("&");
		}
		if(buffer.length()>0) buffer.deleteCharAt(buffer.length()-1);
		return buffer.length() > 0 ? buffer.toString() : "";
	}
	
}
