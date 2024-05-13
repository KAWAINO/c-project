package radius.alarmSys.model;

import java.io.Serializable;

import radius.comm.BaseVo;

public class AlarmSysVo extends BaseVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9057166152639961803L;
	
	private String alarmLocation;	// loc_a + loc_b + loc_c

	private String idx;
	private String userId;
	private String evt_time;
	private String aId;
	private String severity;
	private String loc_a;
	private String loc_b;
	private String loc_c;
	private String alarm_str;
	private String add_str;
	private String first_time;
	private String alarm_name;
	
	private String s_cnt;
	
	private String searchEvtTime;
	private String searchAId;
	private String searchSeverity;
	private String searchLocA;
	private String searchLocB;
	private String searchLocC;
	private String searchAlarmStr;
	private String searchAddStr;
	private Character searchFilterYn;
	private String searchFirstTime;

	
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getS_cnt() {
		return s_cnt;
	}
	public void setS_cnt(String s_cnt) {
		this.s_cnt = s_cnt;
	}
	public String getAlarmLocation() {
		return alarmLocation;
	}
	public void setAlarmLocation(String alarmLocation) {
		this.alarmLocation = alarmLocation;
	}
	public String getEvt_time() {
		return evt_time;
	}
	public void setEvt_time(String evt_time) {
		this.evt_time = evt_time;
	}
	public String getaId() {
		return aId;
	}
	public void setaId(String aId) {
		this.aId = aId;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getLoc_a() {
		return loc_a;
	}
	public void setLoc_a(String loc_a) {
		this.loc_a = loc_a;
	}
	public String getLoc_b() {
		return loc_b;
	}
	public void setLoc_b(String loc_b) {
		this.loc_b = loc_b;
	}
	public String getLoc_c() {
		return loc_c;
	}
	public void setLoc_c(String loc_c) {
		this.loc_c = loc_c;
	}
	public String getAlarm_str() {
		return alarm_str;
	}
	public void setAlarm_str(String alarm_str) {
		this.alarm_str = alarm_str;
	}
	public String getAdd_str() {
		return add_str;
	}
	public void setAdd_str(String add_str) {
		this.add_str = add_str;
	}

	public String getFirst_time() {
		return first_time;
	}
	public void setFirst_time(String first_time) {
		this.first_time = first_time;
	}
	public String getAlarm_name() {
		return alarm_name;
	}
	public void setAlarm_name(String alarm_name) {
		this.alarm_name = alarm_name;
	}
	public String getSearchEvtTime() {
		return searchEvtTime;
	}
	public void setSearchEvtTime(String searchEvtTime) {
		this.searchEvtTime = searchEvtTime;
	}
	public String getSearchAId() {
		return searchAId;
	}
	public void setSearchAId(String searchAId) {
		this.searchAId = searchAId;
	}
	public String getSearchSeverity() {
		return searchSeverity;
	}
	public void setSearchSeverity(String searchSeverity) {
		this.searchSeverity = searchSeverity;
	}
	public String getSearchLocA() {
		return searchLocA;
	}
	public void setSearchLocA(String searchLocA) {
		this.searchLocA = searchLocA;
	}
	public String getSearchLocB() {
		return searchLocB;
	}
	public void setSearchLocB(String searchLocB) {
		this.searchLocB = searchLocB;
	}
	public String getSearchLocC() {
		return searchLocC;
	}
	public void setSearchLocC(String searchLocC) {
		this.searchLocC = searchLocC;
	}
	public String getSearchAlarmStr() {
		return searchAlarmStr;
	}
	public void setSearchAlarmStr(String searchAlarmStr) {
		this.searchAlarmStr = searchAlarmStr;
	}
	public String getSearchAddStr() {
		return searchAddStr;
	}
	public void setSearchAddStr(String searchAddStr) {
		this.searchAddStr = searchAddStr;
	}
	public Character getSearchFilterYn() {
		return searchFilterYn;
	}
	public void setSearchFilterYn(Character searchFilterYn) {
		this.searchFilterYn = searchFilterYn;
	}
	public String getSearchFirstTime() {
		return searchFirstTime;
	}
	public void setSearchFirstTime(String searchFirstTime) {
		this.searchFirstTime = searchFirstTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
