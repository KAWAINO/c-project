package radius.alarmConf.model;

import java.io.Serializable;

import radius.comm.BaseVo;

public class AlarmConfVo extends BaseVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5602206956854089188L;

	private String period;
	private String auth_flag;
	private String auth_cnt;
	private String use_yn;
	private String descr;
	
	private String addPeriod;
	private String addAuthFlag;
	private String addAuthCnt;
	private String addUseYn;
	private String addDescr;
	
	private String updPeriod;
	private String updAuthFlag;
	private String updAuthCnt;
	private String updUseYn;
	private String updDescr;
	
	
	public String getAuth_flag() {
		return auth_flag;
	}
	public void setAuth_flag(String auth_flag) {
		this.auth_flag = auth_flag;
	}
	public String getAuth_cnt() {
		return auth_cnt;
	}
	public void setAuth_cnt(String auth_cnt) {
		this.auth_cnt = auth_cnt;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getUpdAuthCnt() {
		return updAuthCnt;
	}
	public void setUpdAuthCnt(String updAuthCnt) {
		this.updAuthCnt = updAuthCnt;
	}
	public String getUpdUseYn() {
		return updUseYn;
	}
	public void setUpdUseYn(String updUseYn) {
		this.updUseYn = updUseYn;
	}
	public String getUpdDescr() {
		return updDescr;
	}
	public void setUpdDescr(String updDescr) {
		this.updDescr = updDescr;
	}
	public String getUpdAuthFlag() {
		return updAuthFlag;
	}
	public void setUpdAuthFlag(String updAuthFlag) {
		this.updAuthFlag = updAuthFlag;
	}
	public String getUpdPeriod() {
		return updPeriod;
	}
	public void setUpdPeriod(String updPeriod) {
		this.updPeriod = updPeriod;
	}
	public String getAddPeriod() {
		return addPeriod;
	}
	public void setAddPeriod(String addPeriod) {
		this.addPeriod = addPeriod;
	}
	public String getAddAuthFlag() {
		return addAuthFlag;
	}
	public void setAddAuthFlag(String addAuthFlag) {
		this.addAuthFlag = addAuthFlag;
	}
	public String getAddAuthCnt() {
		return addAuthCnt;
	}
	public void setAddAuthCnt(String addAuthCnt) {
		this.addAuthCnt = addAuthCnt;
	}
	public String getAddUseYn() {
		return addUseYn;
	}
	public void setAddUseYn(String addUseYn) {
		this.addUseYn = addUseYn;
	}
	public String getAddDescr() {
		return addDescr;
	}
	public void setAddDescr(String addDescr) {
		this.addDescr = addDescr;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
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
