package iwm_ko.slog.model;

import java.io.Serializable;

import iwm_ko.comm.b20p10.BaseVo_20;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SlogVo extends BaseVo_20 implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3237436225008199328L;
	
	private String scomp;
	private String language;
	
	// TB_GUI_INFO_SHIP
	private String gui_code;
	private String gui_name;
	private String en_gui_name;

	
	private String log_date;
	private String comp_name;
	private String user_id;
	private String user_name;
	private String job_name;
	private String gui_remark;
	
	private String s_code;
	private String s_name;
	
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
	private String searchGuiCode;
	private String searchJobName;
}
