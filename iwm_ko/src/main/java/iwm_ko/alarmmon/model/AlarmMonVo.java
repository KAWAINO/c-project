package iwm_ko.alarmmon.model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AlarmMonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4436410688715103578L;

	private String comp_id;
	
	private String clr_user;
	
	private String evt_time;
	private String aid;
	private String severity;	
	private String loc;
	private String loc_a;
	private String loc_b;
	private String loc_c;
	private String alarm_str;
	private String filteryn;
	private String first_time;
	private int dup_cnt;
	private String is_new;
	private String savedate;
	private int seq;
	private int send_cnt;
	private String snd_yn;
	private String clr_flag;
	private String clr_time;
	
	// active
	private String alias_name;
	private String ha_status;
	private String ip_addr;
	private String update_time;
	
	private String s_cnt;

}
