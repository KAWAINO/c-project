package apms.suser.model;

import java.io.Serializable;

public class GradeVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3641659783807828313L;
	
	private String grade;		//운영자 등급
	private String gname;		//운영자 등급명
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}

}
