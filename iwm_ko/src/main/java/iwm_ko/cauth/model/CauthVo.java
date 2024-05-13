package iwm_ko.cauth.model;

import java.io.Serializable;

import iwm_ko.comm.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class CauthVo extends BaseVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4277828422513158404L;
	
	private String lang;

	// TB_GUI_GRADE_INFO_CENTER
	private String grade;
	private String gui_code;
	private String use_flag;
	private char gui_flag;
	
	// TB_GUI_INFO_CENTER
	private String gui_name;
	private String gui_group;
	private String dir_name;
	private String web_name;
	private String en_gui_name;
	private String en_gui_group;
	
	private String updUseFlag;
	private String updGuiCode;
	
	private String searchGrade;
	
}
