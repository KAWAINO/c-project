package radius.suser.model;

import java.io.Serializable;

import radius.comm.BaseVo;

public class SuserVo extends BaseVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6930582162587450996L;

	// 테이블
	private String userId;		// 운영자 ID
	private String passWd;		// 운영자 비밀번호
	private String userName;	// 운영자 이름
	private String descr;		// 비고
	
	// 검색
	private String searchUserId;
	private String searchUserName;
	
	// 추가
	private String addUserId;
	private String addPassWd;
	private String addUserName;
	private String addDescr;
	
	// 수정
	private String updUserId;
	private String updPassWd;
	private String updUserName;
	private String updDescr;
	private String cngPwd;
	
	
	
	public String getSearchUserId() {
		return searchUserId;
	}
	public void setSearchUserId(String searchUserId) {
		this.searchUserId = searchUserId;
	}
	public String getSearchUserName() {
		return searchUserName;
	}
	public void setSearchUserName(String searchUserName) {
		this.searchUserName = searchUserName;
	}
	public String getUpdUserId() {
		return updUserId;
	}
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}
	public String getUpdPassWd() {
		return updPassWd;
	}
	public void setUpdPassWd(String updPassWd) {
		this.updPassWd = updPassWd;
	}
	public String getUpdUserName() {
		return updUserName;
	}
	public void setUpdUserName(String updUserName) {
		this.updUserName = updUserName;
	}
	public String getUpdDescr() {
		return updDescr;
	}
	public void setUpdDescr(String updDescr) {
		this.updDescr = updDescr;
	}
	public String getCngPwd() {
		return cngPwd;
	}
	public void setCngPwd(String cngPwd) {
		this.cngPwd = cngPwd;
	}
	public String getAddUserId() {
		return addUserId;
	}
	public void setAddUserId(String addUserId) {
		this.addUserId = addUserId;
	}
	public String getAddPassWd() {
		return addPassWd;
	}
	public void setAddPassWd(String addPassWd) {
		this.addPassWd = addPassWd;
	}
	public String getAddUserName() {
		return addUserName;
	}
	public void setAddUserName(String addUserName) {
		this.addUserName = addUserName;
	}
	public String getAddDescr() {
		return addDescr;
	}
	public void setAddDescr(String addDescr) {
		this.addDescr = addDescr;
	}
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
