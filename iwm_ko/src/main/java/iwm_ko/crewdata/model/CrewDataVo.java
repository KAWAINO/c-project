package iwm_ko.crewdata.model;

import java.io.Serializable;

import iwm_ko.comm.b20p10.BaseVo_20;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CrewDataVo extends BaseVo_20 implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5232425051094880075L;
	
	private String scomp;

	private String log_date;
	private String comp_name;
	private String s_name;
	private String user_name;
	private String user_id;
	private String crew_name;
	private String crew_id;
	private int pre_data;
	private int data;
	private String diff_data;
	
	private String excelMsg;
	 
	// 날짜 및 시간
	private String defaultDate;
	private String startDefaultDate;
	private String endDefaultDate;
	private String startDate;
	private String endDate;
	private String startMin;
	private String endMin;
	private String startHour;
	private String endHour;
	
	// 조회
	private String searchChked;
	private String searchCompId;
	private String searchShipName;
	private String searchUserName;
	private String searchUserId;

}
