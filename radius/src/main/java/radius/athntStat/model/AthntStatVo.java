package radius.athntStat.model;

import java.io.Serializable;
import java.math.BigInteger;

import radius.comm.BaseVo;

public class AthntStatVo extends BaseVo implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8243477816858469363L;
	
	private String reply;
	private String stat_time;
	private String nas_ip_address;
	private String cause;
	private String cause_name;
	private BigInteger auth_cnt;
	private BigInteger success_cnt;
	private BigInteger fail_cnt;
	private String startDate;
	private String endDate;
	
	private String tableName;
	
	private String searchItems;
	private String searchUnits;
	private String searchReply;
	private String searchNasIp;
	private String searchCause;
	private String searchCauseName;
	private String searchStartMin;
	private String searchEndMin;
	private String searchStartHour;
	private String searchEndHour;
	private String searchStatType;
	private String searchChecked;

	private Boolean chkedReplyList;
	private Boolean chkedNasIpList;
	private Boolean chkedCauseList;
	
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getStat_time() {
		return stat_time;
	}
	public void setStat_time(String stat_time) {
		this.stat_time = stat_time;
	}
	public String getNas_ip_address() {
		return nas_ip_address;
	}
	public void setNas_ip_address(String nas_ip_address) {
		this.nas_ip_address = nas_ip_address;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getCause_name() {
		return cause_name;
	}
	public void setCause_name(String cause_name) {
		this.cause_name = cause_name;
	}
	public BigInteger getAuth_cnt() {
		return auth_cnt;
	}
	public void setAuth_cnt(BigInteger auth_cnt) {
		this.auth_cnt = auth_cnt;
	}
	public BigInteger getSuccess_cnt() {
		return success_cnt;
	}
	public void setSuccess_cnt(BigInteger success_cnt) {
		this.success_cnt = success_cnt;
	}
	public BigInteger getFail_cnt() {
		return fail_cnt;
	}
	public void setFail_cnt(BigInteger fail_cnt) {
		this.fail_cnt = fail_cnt;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getSearchItems() {
		return searchItems;
	}
	public void setSearchItems(String searchItems) {
		this.searchItems = searchItems;
	}
	public String getSearchUnits() {
		return searchUnits;
	}
	public void setSearchUnits(String searchUnits) {
		this.searchUnits = searchUnits;
	}
	public String getSearchReply() {
		return searchReply;
	}
	public void setSearchReply(String searchReply) {
		this.searchReply = searchReply;
	}
	public String getSearchNasIp() {
		return searchNasIp;
	}
	public void setSearchNasIp(String searchNasIp) {
		this.searchNasIp = searchNasIp;
	}
	public String getSearchCause() {
		return searchCause;
	}
	public void setSearchCause(String searchCause) {
		this.searchCause = searchCause;
	}
	public String getSearchCauseName() {
		return searchCauseName;
	}
	public void setSearchCauseName(String searchCauseName) {
		this.searchCauseName = searchCauseName;
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
	public Boolean getChkedReplyList() {
		return chkedReplyList;
	}
	public void setChkedReplyList(Boolean chkedReplyList) {
		this.chkedReplyList = chkedReplyList;
	}
	public Boolean getChkedNasIpList() {
		return chkedNasIpList;
	}
	public void setChkedNasIpList(Boolean chkedNasIpList) {
		this.chkedNasIpList = chkedNasIpList;
	}
	public Boolean getChkedCauseList() {
		return chkedCauseList;
	}
	public void setChkedCauseList(Boolean chkedCauseList) {
		this.chkedCauseList = chkedCauseList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "AthntStatVo [reply=" + reply + ", stat_time=" + stat_time + ", nas_ip_address=" + nas_ip_address
				+ ", cause=" + cause + ", cause_name=" + cause_name + ", auth_cnt=" + auth_cnt + ", success_cnt="
				+ success_cnt + ", fail_cnt=" + fail_cnt + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", tableName=" + tableName + ", searchItems=" + searchItems + ", searchUnits=" + searchUnits
				+ ", searchReply=" + searchReply + ", searchNasIp=" + searchNasIp + ", searchCause=" + searchCause
				+ ", searchCauseName=" + searchCauseName + ", searchStartMin=" + searchStartMin + ", searchEndMin="
				+ searchEndMin + ", searchStartHour=" + searchStartHour + ", searchEndHour=" + searchEndHour
				+ ", searchStatType=" + searchStatType + ", searchChecked=" + searchChecked + ", chkedReplyList="
				+ chkedReplyList + ", chkedNasIpList=" + chkedNasIpList + ", chkedCauseList=" + chkedCauseList + "]";
	}

	
	
}
