package iwm_ko.sauth.model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SauthVo implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 859923559649217673L;
	
	private String s_code;
	private String s_name;
	private String gui_code;
	private String gui_name;
	private String en_gui_name;
	private String grade;
	private String use_flag;

	// 조회
	private String searchCompId;
	private String searchScode;
	private String searchGrade;
	private String searchGuiCode;
	private String searchScode2;
	
	private String updUseFlag;
	private String updGuiCode;
	private String updGname;
}
