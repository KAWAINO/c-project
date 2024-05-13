package iwm_ko.enableTime.model;

import java.io.Serializable;

import iwm_ko.comm.b15p10.BaseVo_15;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EnableTimeVo extends BaseVo_15 implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1515182847775978818L;
	
	private String scomp;

	private String from_time;
	private String to_time;
	private String comp_id;
	private String comp_name;
	private String s_name;
	private String s_code;
	private String descr;
	private String modify_date;
	
	private String case_result;
	
	private String fromHour;
	private String fromMin;
	private String toHour;
	private String toMin;

	private String searchCompId;
	private String searchShipName;
	
	private String addFromTime;
	private String addToTime;
	private String addCompName;
	private String addShipName;
	private String addDescr;
}
