package apms.suser.model;

import java.io.Serializable;

import apms.comm.BaseVo;

public class SuserVo extends BaseVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -784504733354832934L;
	
	//검색조건
	private String searchGrade;		//등급
	private String searchUserId;	//운영자 ID
	private String searchUserName;	//운영자 이름
	
	//TABLE
	private String user_id;			//운영자 ID
	private String user_name;		//운영자 이름
	private String passwd;			//비밀번호
	private String user_grade;		//운영자 등급
	private String gname;			//등급명
	private String team_name;		//팀명
	private String phone;			//전화번호
	private String mobile;			//휴대전화번호
	private String email;			//email
	private String del_flag;		//삭제여부
	private String s_code;			//선박코드
	private String s_name;			//선박명
	private String comp_id;			//선주사 ID
	private String comp_name;		//선주사명
	
	//추가 data
	private String addUserId;		//운영자 Id
	private String addUserName;		//운영자 이름
	private String addUserPwd;		//비밀번호
	private String addUserGrade;	//운영자 등급
	
	//수정 data
	private String updUserId;		//운영자 Id
	private String updUserName;		//운영자 이름
	private String updUserPwd;		//비밀번호
	private String updUserGrade;	//운영자 등급
	private String cngPwd;			//비밀번호 변경 flag
	
	public String getSearchGrade() {
		return searchGrade;
	}
	public void setSearchGrade(String searchGrade) {
		this.searchGrade = searchGrade;
	}
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
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
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
	public String getTeam_name() {
		return team_name;
	}
	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDel_flag() {
		return del_flag;
	}
	public void setDel_flag(String del_flag) {
		this.del_flag = del_flag;
	}
	public String getS_code() {
		return s_code;
	}
	public void setS_code(String s_code) {
		this.s_code = s_code;
	}
	public String getS_name() {
		return s_name;
	}
	public void setS_name(String s_name) {
		this.s_name = s_name;
	}
	public String getComp_id() {
		return comp_id;
	}
	public void setComp_id(String comp_id) {
		this.comp_id = comp_id;
	}
	public String getComp_name() {
		return comp_name;
	}
	public void setComp_name(String comp_name) {
		this.comp_name = comp_name;
	}
	public String getAddUserId() {
		return addUserId;
	}
	public void setAddUserId(String addUserId) {
		this.addUserId = addUserId;
	}
	public String getAddUserName() {
		return addUserName;
	}
	public void setAddUserName(String addUserName) {
		this.addUserName = addUserName;
	}
	public String getAddUserPwd() {
		return addUserPwd;
	}
	public void setAddUserPwd(String addUserPwd) {
		this.addUserPwd = addUserPwd;
	}
	public String getAddUserGrade() {
		return addUserGrade;
	}
	public void setAddUserGrade(String addUserGrade) {
		this.addUserGrade = addUserGrade;
	}
	public String getUpdUserId() {
		return updUserId;
	}
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}
	public String getUpdUserName() {
		return updUserName;
	}
	public void setUpdUserName(String updUserName) {
		this.updUserName = updUserName;
	}
	public String getUpdUserPwd() {
		return updUserPwd;
	}
	public void setUpdUserPwd(String updUserPwd) {
		this.updUserPwd = updUserPwd;
	}
	public String getUpdUserGrade() {
		return updUserGrade;
	}
	public void setUpdUserGrade(String updUserGrade) {
		this.updUserGrade = updUserGrade;
	}
	public String getCngPwd() {
		return cngPwd;
	}
	public void setCngPwd(String cngPwd) {
		this.cngPwd = cngPwd;
	}
}
