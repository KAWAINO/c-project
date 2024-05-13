package radius.athntHistory.model;

import java.io.Serializable;

public class CauseInfoVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8948074346157444951L;

	private String cause;
	private String cause_name;
	
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getCause_name() {
		return cause_name;
	}
	public void setCause_name(String cause_name) {
		this.cause_name = cause_name;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
