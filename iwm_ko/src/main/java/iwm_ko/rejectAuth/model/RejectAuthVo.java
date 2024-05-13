package iwm_ko.rejectAuth.model;

import java.io.Serializable;

import iwm_ko.comm.b15p10.BaseVo_15;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RejectAuthVo extends BaseVo_15 implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5347967262437183182L;
	
	private String scomp;
	
	private String mac;
	private String comp_name;
	private String s_name;
	private String s_code;
	private String descr;
	
	private String excelMsg;
	
	private String addMac;
	private String addCompName;
	private String addShipName;
	private String addDescr;

	private String searchCompId;
	private String searchShipName;
	
	private String updMac;
	private String u_updMac;
	private String updCompName;
	private String updShipName;
	private String updDescr;
	private String updScode;
}
