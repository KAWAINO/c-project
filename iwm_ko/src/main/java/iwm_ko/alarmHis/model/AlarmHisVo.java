package iwm_ko.alarmHis.model;

import java.io.Serializable;

import iwm_ko.comm.b20p10.BaseVo_20;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AlarmHisVo extends BaseVo_20 implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7391831273511086247L;
	
	private String alarm_id;
	private String alarm_name;
	private String evt_time;
	private String aid;
	private String severity;
	private String loc;
	private String alarm_str;
	private String first_time;
	private String dup_cnt;
	private String clr_flag;
	private String clr_time;
	private String clr_user;
	private String comp_id;
	
	private String excelMsg;
	
	private String searchChked;
	
	// 날짜 및 시간
	private String defaultDate;
	private String startDefaultDate;
	private String endDefaultDate;
	
	private String startDate;
	private String endDate;
	private String startHour;
	private String endHour;
	private String startMin;
	private String endMin;
	
	private String searchAlarmCause;

}
