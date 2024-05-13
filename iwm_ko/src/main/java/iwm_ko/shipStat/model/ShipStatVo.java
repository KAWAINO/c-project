package iwm_ko.shipStat.model;

import iwm_ko.comm.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ShipStatVo extends BaseVo implements Serializable {

    private String excel_msg;
    private String sessionCompId;
    private String mon_time;
    private String val1;
    private String val2;
    private String sumval;
    private String cs_unit;
    private String searchCondition;
    private String comp_name;
    private String comp_id;
    private String s_code;
    private String s_name;
    private String searchStartDate;
    private String searchStartHour;
    private String searchEndDate;
    private String searchEndHour;
    private String searchCompId;
    private String searchSCode;
    private String searchTableName;
    private String cs_ref_comp;
    private String cs_ref_ship;


}
