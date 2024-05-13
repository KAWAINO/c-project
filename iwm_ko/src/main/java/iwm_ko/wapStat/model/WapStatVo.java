package iwm_ko.wapStat.model;

import iwm_ko.comm.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class WapStatVo  extends BaseVo implements Serializable {

    private String sessionCompId;
    private String excel_msg;
    private String comp_id;
    private String cs_ref_ship;
    private String cs_ref_comp;
    private String cs_ref_ap;
    private String cs_unit;
    private String comp_name;
    private String cs_code;


    private String mon_time;
    private String s_name;
    private String s_code;
    private String ap_name;
    private String  val1;
    private String val2;
    private String sumval;

    private String searchTableName;
    private String searchStartDate;
    private String searchEndDate;
    private String searchStartHour;
    private String searchEndHour;
    private String searchCompId;
    private String searchSCode;
    private String searchAp;


}
