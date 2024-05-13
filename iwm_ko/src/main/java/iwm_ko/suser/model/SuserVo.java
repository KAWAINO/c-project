package iwm_ko.suser.model;

import java.io.Serializable;

import iwm_ko.comm.b15p10.BaseVo_15;
import iwm_ko.login.model.LoginVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SuserVo extends BaseVo_15 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8395761679957743419L;
	
	private String scomp;
	
	private String s_code;
	private String user_id;
	private String passwd;
	private String user_name;
	private String user_grade;
	private String team_name;
	private String phone;
	private String mobile;
	private String email;
	private char del_flag;
	private String del_date;
	private String modify_date;
	private char gui_flag;
	
	private String excelMsg;
	
	// TB_SHIP_INFO
	private String s_name;
	
	// TB_GRADE_INFO_CENTER
	private String grade;
	private String gname;
	
	// TB_SCOMP_INFO
	private String comp_id;
	private String comp_name;
	
	// 검색
	private String searchCompId;
	private String searchCompName;
	private String searchGrade;
	private String searchGname;
	private String searchSname;
	private String searchUserId;
	private String searchUserName;
	
	// 추가
	private String addShipName;
	private String addCompId;
	private String addPassWd;
	private String addUserGrade;
	private String addUserId;
	private String addUserName;
	
	// 수정
	private String updScode;
	private String updCompId;
	private String updPassWd;
	private String updUserGrade;
	private String updUserId;
	private String updUserName;
	private String cngPwd;
	
}
