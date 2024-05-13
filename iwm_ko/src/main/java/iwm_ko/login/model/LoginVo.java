package iwm_ko.login.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class LoginVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6874283856814665586L;
	
	private String grade;
	private String groupCode;
	
	private String user_id;			//ID
	private String passwd;			//비밀번호
	private String passwd256;

	private String user_name;		//이름
	private String user_grade;		//권한

	private String comp_id ;
	private String gname;			//권한명
	private String scode;			//S_CODE
	private String del_flag;

	private int limit_days;
	private int val;
	private int failCnt;
	private int lockCnt;



}
