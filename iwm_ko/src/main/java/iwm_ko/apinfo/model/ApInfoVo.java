package iwm_ko.apinfo.model;

import java.io.Serializable;

import iwm_ko.comm.b15p10.BaseVo_15;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApInfoVo extends BaseVo_15 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6852943802135919563L;
	
	private String comp_id;
	
	private String scomp;
	
	private String s_code;
	private String s_name;
	private String mac;
	private String comp_name;
	private String ap_name;
	private String serial_no;
	private int band_width;
	private int con_user;
	private String status;
	private String fw_version;
	private String ip_no;
	private int wifi_conn_flag;
	private String conn_time;
	private String descr;
	private String del_flag;
	private String rebootFlag;

	// 검색
	private String searchCompId;
	private String searchStatus;
	private String searchShipName;
	private String searchApName;
	
	// 추가
	private String addMac;
	private String addApName;
	private String addCompId;
	private String addShipName;
	private String addSerial;
	private String addIpNumber;
	private int addBand;
	private int addConUser;
	private String addWIFI;
	private String addHour;
	private String addMin;
	private String addUnLimit;
	private String addDescr;
	private String addRebootFlag;
	private String addConnTime;
	
	// 수정
	private String updMac;
	private String updApName;
	private String updCompId;
	private String updScode;
	private String updShipName;
	private String updSerial;
	private String updIpNumber;
	private int updBand;
	private int updConUser;
	private String updWIFI;
	private String updHour;
	private String updMin;
	private String updUnLimit;
	private String updDescr;
	private String updConnTime;
	private String updConnTimeChange;
	
	// 프로시저
	private String pFlag;
	private String pTermMac;
	private String pDiscReason;
	private Integer vResult;
	
	private String excelMsg;
	
}
