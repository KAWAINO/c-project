package radius.login.model;

import java.io.Serializable;

public class LoginVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6036913633236083020L;
	
	private String userId;		// 운영자 ID
	private String passWd;		// 운영자 비밀번호
	private String userName;	// 운영자 이름
	private String descr;		// 비고
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassWd() {
		return passWd;
	}
	public void setPassWd(String passWd) {
		this.passWd = passWd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
