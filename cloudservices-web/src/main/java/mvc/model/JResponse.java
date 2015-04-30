package mvc.model;

public class JResponse {
	public static final String SUCCESS = "1";
	public static final String FAIL = "0";
	
	public String code;
	public String msg;
	
	public static JResponse success(String msg) {
		return new JResponse(SUCCESS, msg);
	}
	public static JResponse fail(String msg){
		return new JResponse(FAIL, msg);
	}
	
	public JResponse(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
