package iwm_ko.apport.model;

import java.io.Serializable;

import iwm_ko.comm.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApportVo extends BaseVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -344071442231428571L;
	
	private String scomp;
	
	private String comp_id;
	private String s_code;
	private String s_name;
	private String comp_name;
	
	private String searchShipCode;
	private String searchShipName;
	
	private String rule_name;
	private int port_from;
	private int port_to;
	private String use_flag;
	private String descr;
	
	private String addScode;
	private String addShipName;
	private String addRuleName;
	private String addPortFrom;
	private String addPortTo;
	private String addUseFlag;
	private String addDescr;
	
	private String updScode;
	private String updShipName;
	private String updRuleName;
	private String updPortFrom;
	private String updPortTo;
	private String updUseFlag;
	private String updDescr;
	
	private String excelMsg;
	private String allowAllPort;
	private String shipCode;
}
