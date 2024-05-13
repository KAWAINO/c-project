package iwm_ko.rejectExcept.model;

import java.io.Serializable;

import iwm_ko.comm.b15p10.BaseVo_15;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RejectExceptVo extends BaseVo_15 implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5557088960961349394L;
	
	private String scomp;

	private String mac;
	private String comp_name;
	private String s_name;
	private String s_code;
	private String descr;
	
	private String excelMsg;
	
	private String searchCompId;
	private String searchShipName;
	
	private String addMac;
	private String addCompName;
	private String addShipName;
	private String addDescr;
	
	private String updMac;
	private String u_updMac;
	private String updCompName;
	private String updShipName;
	private String updDescr;
	private String updScode;
}
