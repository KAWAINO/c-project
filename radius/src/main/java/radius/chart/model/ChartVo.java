package radius.chart.model;

import java.io.Serializable;
import java.math.BigInteger;

import radius.comm.BaseVo;

public class ChartVo extends BaseVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9084882987238354431L;
	
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
	private String hour;
	

	
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	
	
	
}
