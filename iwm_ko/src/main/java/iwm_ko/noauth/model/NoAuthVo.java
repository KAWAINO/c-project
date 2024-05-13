package iwm_ko.noauth.model;

import java.io.Serializable;

import iwm_ko.comm.b15p10.BaseVo_15;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class NoAuthVo extends BaseVo_15 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4228694428429686050L;
	
	private String scomp;

	private String mac;
	private String s_code;
	private String term_name;
	private String descr;
	private String del_flag;
	private String del_date;
	private String modify_date;
	private String gui_flag;
	private String comp_name;
	private String s_name;
	private String comp_id;
	
	// 조회
	private String searchCompId;
	private String searchShipName;
	private String searchTermName;
	
	// 추가
	private String addMac;
	private String addTermName;
	private String addCompId;
	private String addShipName;
	private String addDescr;
	
	// 수정
	private String updMac;
	private String updTermName;
	private String updCompId;
	private String updShipName;
	private String updDescr;
	private String updScode;
	private String u_updMac;
	
	private String excelMsg;
}
