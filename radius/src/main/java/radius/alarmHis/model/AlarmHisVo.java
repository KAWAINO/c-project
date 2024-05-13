package radius.alarmHis.model;

import java.io.Serializable;
import java.util.Date;

import radius.comm.BaseVo;

public class AlarmHisVo extends BaseVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3301500426604515691L;
	
	private String userId;
	private String idx;
	private String evt_time;
	private String aId;
	private String severity;
	private String loc_a;
	private String loc_b;
	private String loc_c;
	private String alarm_str;
	private String add_str;
	private Character filterYn;
	private String first_time;
	private int dup_cnt;
	private char clr_flag;
	private String clr_time;
	private String clr_user;
	private Date saveDate;
	
	private String alarmLocation;	// loc_a + loc_b + loc_c
	
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
	private int searchDupCnt;
	private Character searchClrFlag;
	private String searchClrTime;
	private String searchClrUser;
	private Date searchSaveDate;
	
	private String startDate;
	private String endDate;
	private String searchStartMin;
	private String searchEndMin;
	private String searchStartHour;
	private String searchEndHour;
	
	private String searchChked;
	private String searchChked2;
	

	
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

	public int getSearchDupCnt() {
		return searchDupCnt;
	}

	public void setSearchDupCnt(int searchDupCnt) {
		this.searchDupCnt = searchDupCnt;
	}

	public Character getSearchClrFlag() {
		return searchClrFlag;
	}

	public void setSearchClrFlag(Character searchClrFlag) {
		this.searchClrFlag = searchClrFlag;
	}

	public String getSearchClrTime() {
		return searchClrTime;
	}

	public void setSearchClrTime(String searchClrTime) {
		this.searchClrTime = searchClrTime;
	}

	public String getSearchClrUser() {
		return searchClrUser;
	}

	public void setSearchClrUser(String searchClrUser) {
		this.searchClrUser = searchClrUser;
	}

	public Date getSearchSaveDate() {
		return searchSaveDate;
	}

	public void setSearchSaveDate(Date searchSaveDate) {
		this.searchSaveDate = searchSaveDate;
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

	public String getSearchChked() {
		return searchChked;
	}

	public void setSearchChked(String searchChked) {
		this.searchChked = searchChked;
	}

	public String getSearchChked2() {
		return searchChked2;
	}

	public void setSearchChked2(String searchChked2) {
		this.searchChked2 = searchChked2;
	}

	public Character getFilterYn() {
		return filterYn;
	}
	
	public void setFilterYn(Character filterYn) {
		this.filterYn = filterYn;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
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

	public int getDup_cnt() {
		return dup_cnt;
	}

	public void setDup_cnt(int dup_cnt) {
		this.dup_cnt = dup_cnt;
	}

	public char getClr_flag() {
		return clr_flag;
	}

	public void setClr_flag(char clr_flag) {
		this.clr_flag = clr_flag;
	}

	public String getClr_time() {
		return clr_time;
	}

	public void setClr_time(String clr_time) {
		this.clr_time = clr_time;
	}

	public String getClr_user() {
		return clr_user;
	}

	public void setClr_user(String clr_user) {
		this.clr_user = clr_user;
	}

	public Date getSaveDate() {
		return saveDate;
	}

	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}

	public String getAlarmLocation() {
		return alarmLocation;
	}

	public void setAlarmLocation(String alarmLocation) {
		this.alarmLocation = alarmLocation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
