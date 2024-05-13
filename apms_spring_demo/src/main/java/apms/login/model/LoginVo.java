package apms.login.model;

import java.io.Serializable;

public class LoginVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6874283856814665586L;
	
	private String user_id;			//ID
	private String passwd;			//비밀번호
	private String user_name;		//이름
	private String user_grade;		//권한
	private String gname;			//권한명
	private String scode;			//S_CODE
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_grade() {
		return user_grade;
	}
	public void setUser_grade(String user_grade) {
		this.user_grade = user_grade;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public String getScode() {
		return scode;
	}
	public void setScode(String scode) {
		this.scode = scode;
	}
	
	
}
