package iwm_ko.crewauth.model;

import java.io.Serializable;

import iwm_ko.comm.b20p10.BaseVo_20;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CrewAuthVo extends BaseVo_20 implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1022217153254496799L;
	
	private String scomp;
	
	private String mac;
	private String s_code;
	private String s_name;
	private String crew_id;
	private String crew_name;
	private String ap_mac;
	private String ap_name;
	private String comp_name;
	private String auth_flag;
	private String status;
	private String conn_time;
	private String disconn_flag;
	private String disconn_time;
	private String conn;
	private String disconn;
	
	private String excelMsg;
	
	// 날짜 및 시간
	private String defaultDate;
	private String conStartDefaultDate;
	private String conEndDefaultDate;
	private String disconStartDefaultDate;
	private String disconEndDefaultDAte;
	
	private String conStrDate;
	private String conEndDate;
	private String conStrMin;
	private String conEndMin;
	private String conStrHour;
	private String conEndHour;
	private String disconStrDate;
	private String disconEndDate;
	private String disconStrMin;
	private String disconEndMin;
	private String disconStrHour;
	private String disconEndHour;
	
	private String searchCompId;
	private String searchShipCode;
	private String searchShipName;
	private String searchCrewId;
	private String searchCrewName;
	private String searchStatus;
	private String searchDisconFlag;
	private String searchChked;

}
