package iwm_ko.cuser.model;

import java.io.Serializable;
import java.util.Date;

import iwm_ko.comm.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class CuserVo extends BaseVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 685406968008587794L;
	
	private String scomp;
	
	// TB_USER_INFO_CENTER
	private String user_id;
	private String passWd;
	private String user_name;
	private String user_grade;
	private String comp_id;
	private String team_name;
	private String phone;
	private String mobile;
	private String email;
	private char del_flag;
	private Date del_date;
	private Date modify_date;
	private char gui_flag;
	private Date pwd_change_date;
	private String type;
	
	private String excelMsg;
	
	// TB_USER_INFO_LOCK
	private int fail_cnt;
	
	// TB_SCOMP_INFO
	private String comp_name;
	private String descr;
	private int ship_Hold;
	
	// TB_GRADE_INFO_CENTER
	private String grade;
	private String gname;
	private int gorder;
	
	// TB_CONF_VAL(LOCK COUNT)
	private String val;
	
	// 검색
	private String searchCompId;
	private String searchCompName;
	private String searchUserId;
	private String searchUserName;
	private String searchGrade;
	
	// 추가
	private String addUserId;
	private String addPassWd;
	private String addUserName;
	private String addUserGrade;
	private String addCompId;
	
	// 수정
	private String updUserId;
	private String updPassWd;
	private String updUserName;
	private String updUserGrade;
	private String updCompId;
	private String updGname;
	private String cngPwd;
	
}
