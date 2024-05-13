package radius.athntHistory.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import radius.comm.BaseVo;

public class AthntHistoryVo extends BaseVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3730291131157005954L;

	private int id;
	private String userName;
	private String pass;
	private String gpp_imsi;
	private String msisdn;
	private String reply;
	private String authdate;
	private String apn;
	private String gpp_sgsn_address;
	private String gpp_ggsn_address;
	private String framed_ip_address;
	private String nas_ip_address;
	private String nas_port_no;
	private String cause;
	private String cause_name;
	
	// 검색
	private String StartDate;
	private String EndDate;
	private String searchMsisdn;
	private String searchImsi;
	private String searchUserName;
	private String searchReply;
	private String searchCause;
	private String searchAccept;
	private String searchReject;
	private String searchSuccess;
	private String searchFail;
	private String searchOrver;
	private String searchStartMin;
	private String searchEndMin;
	private String searchStartHour;
	private String searchEndHour;
	private String searchStatType;
	private String searchChecked;

	
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public String getEndDate() {
		return EndDate;
	}
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}
	public String getSearchStartMin() {
		return searchStartMin;
	}
	public void setSearchStartMin(String searchStartMin) {
		this.searchStartMin = searchStartMin;
	}
	public String getSearchEndMin() {
		return searchEndMin;
	}
	public void setSearchEndMin(String searchEndMin) {
		this.searchEndMin = searchEndMin;
	}
	public String getSearchStartHour() {
		return searchStartHour;
	}
	public void setSearchStartHour(String searchStartHour) {
		this.searchStartHour = searchStartHour;
	}
	public String getSearchEndHour() {
		return searchEndHour;
	}
	public void setSearchEndHour(String searchEndHour) {
		this.searchEndHour = searchEndHour;
	}
	public String getSearchStatType() {
		return searchStatType;
	}
	public void setSearchStatType(String searchStatType) {
		this.searchStatType = searchStatType;
	}
	public String getSearchChecked() {
		return searchChecked;
	}
	public void setSearchChecked(String searchChecked) {
		this.searchChecked = searchChecked;
	}
	public String getSearchSuccess() {
		return searchSuccess;
	}
	public void setSearchSuccess(String searchSuccess) {
		this.searchSuccess = searchSuccess;
	}
	public String getSearchFail() {
		return searchFail;
	}
	public void setSearchFail(String searchFail) {
		this.searchFail = searchFail;
	}
	public String getSearchOrver() {
		return searchOrver;
	}
	public void setSearchOrver(String searchOrver) {
		this.searchOrver = searchOrver;
	}
	public String getSearchAccept() {
		return searchAccept;
	}
	public void setSearchAccept(String searchAccept) {
		this.searchAccept = searchAccept;
	}
	public String getSearchReject() {
		return searchReject;
	}
	public void setSearchReject(String searchReject) {
		this.searchReject = searchReject;
	}
	public String getCause_name() {
		return cause_name;
	}
	public void setCause_name(String cause_name) {
		this.cause_name = cause_name;
	}
	public String getSearchMsisdn() {
		return searchMsisdn;
	}
	public void setSearchMsisdn(String searchMsisdn) {
		this.searchMsisdn = searchMsisdn;
	}
	public String getSearchImsi() {
		return searchImsi;
	}
	public void setSearchImsi(String searchImsi) {
		this.searchImsi = searchImsi;
	}
	public String getSearchUserName() {
		return searchUserName;
	}
	public void setSearchUserName(String searchUserName) {
		this.searchUserName = searchUserName;
	}
	public String getSearchReply() {
		return searchReply;
	}
	public void setSearchReply(String searchReply) {
		this.searchReply = searchReply;
	}
	public String getSearchCause() {
		return searchCause;
	}
	public void setSearchCause(String searchCause) {
		this.searchCause = searchCause;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getGpp_imsi() {
		return gpp_imsi;
	}
	public void setGpp_imsi(String gpp_imsi) {
		this.gpp_imsi = gpp_imsi;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getAuthdate() {
		return authdate;
	}
	public void setAuthdate(String authdate) {
		this.authdate = authdate;
	}
	public String getApn() {
		return apn;
	}
	public void setApn(String apn) {
		this.apn = apn;
	}
	public String getGpp_sgsn_address() {
		return gpp_sgsn_address;
	}
	public void setGpp_sgsn_address(String gpp_sgsn_address) {
		this.gpp_sgsn_address = gpp_sgsn_address;
	}
	public String getGpp_ggsn_address() {
		return gpp_ggsn_address;
	}
	public void setGpp_ggsn_address(String gpp_ggsn_address) {
		this.gpp_ggsn_address = gpp_ggsn_address;
	}
	public String getFramed_ip_address() {
		return framed_ip_address;
	}
	public void setFramed_ip_address(String framed_ip_address) {
		this.framed_ip_address = framed_ip_address;
	}
	public String getNas_ip_address() {
		return nas_ip_address;
	}
	public void setNas_ip_address(String nas_ip_address) {
		this.nas_ip_address = nas_ip_address;
	}
	public String getNas_port_no() {
		return nas_port_no;
	}
	public void setNas_port_no(String nas_port_no) {
		this.nas_port_no = nas_port_no;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getCauseName() {
		return cause_name;
	}
	public void setCauseName(String cause_name) {
		this.cause_name = cause_name;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
