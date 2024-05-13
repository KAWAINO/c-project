package iwm_ko.monitor.model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MonitorVo implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 6473057506723144510L;
	
	private String scomp;
	private String language;
	private String url;

	private String userGrade;
    private String gui_name;
    private String en_gui_name;
    private String web_name;
    private String comp_id;
    private String comp_name;
    private String s_code;
    private String s_name;
    private String mon_time;
    private String val2;
    
    private String searchChked;
    private String startDefaultDate;
    private String endDefaultDate;
    private String defaultDate;
    private String startDate;
    private String endDate;
    private String startHour;
    private String endHour;
    
    private String searchCompId;
    private String searchComp;
    private String searchShipCode;
    private String searchUnit;
    private String searchRange;
    private String searchTime;
    private String searchCompCnt;
    private String searchOrder;
    
    private String tableName;
    
    private int diffTime;
    
    private String chkShipCode;
    private String chkCompId;
    String[] shipCodes;

}
