package iwm_ko.shipInfo.model;

import iwm_ko.comm.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter
@Setter
@ToString
public class ShipInfoVo extends BaseVo implements Serializable {
	
	private String scomp;
	
    private String sessionCompId;
    private String chkDel;

    private String comp_id;
    private String s_code;
    private String s_name;
    private String comp_name;
    private String amt_use_month;
    private String amt_use_day;
    private String apms_status;
    private String a_cnt;
    private String descr;
    private String del_flag;

    private String searchSCode;

    private String searchSName;

    private String searchCompId;

    private String searchApmsStatus;

    private String searchCompName;
    private String selectedCompId;

    private String addCode;
    private String addName;
    private String addDescr;
    private String addCompId;

    private String updCode;
    private String updName;
    private String updCompName;
    private String updDescr;
    private String updCompId;

    private String u_updCode;
    private String u_updDescr;
    private String u_updName;

    private Integer result;

    private String excel_msg;


}
