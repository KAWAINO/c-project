package iwm_ko.clog.model;

import java.io.Serializable;

import iwm_ko.comm.b20p10.BaseVo_20;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ClogVo extends BaseVo_20 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5345360855339100713L;
	
	private String scomp;

	// TB_USER_LOG_CENTER
	private String log_date;
	private String gui_code;
	private String job_name;
	private String gui_remark;
	private String user_id;
	private String sql_str;
	
	private String excelMsg;
	
	// logList 조회
	private String comp_name;
	private String user_name;
	
	// TB_GUI_INFO_CENTER
	private String gui_name;
	private String en_gui_name;
	
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
	private String searchGuiCode;
	private String searchGuiName;
	private String searchUserName;
	private String searchUserId;
	private String searchJobName;
	
}
