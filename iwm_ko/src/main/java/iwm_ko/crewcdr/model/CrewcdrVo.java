package iwm_ko.crewcdr.model;

import iwm_ko.comm.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@Getter
@Setter
@ToString
public class CrewcdrVo extends BaseVo implements Serializable {

    private String searchCompId;
    private String searchCrewId;
    private String searchSCode;
    private String searchTableName;
    private String chkStartDate;
    private String searchStartTime;
    private String searchStartDate;
    private String searchEndTime;
    private String searchEndDate;
    private String searchStartHour;
    private String searchEndHour;
    private String chkSearchStartHour;
    private String chkSearchStartDate;
    private String chkSearchEndDate;
    private String chkSearchEndHour;
    private String searchFromDate;
    private String searchToDate;
    private String crew_id;
    private String crew_name;


    private String comp_id;
    private String comp_name;

    private String mac;
    private String ap_name;
    private String s_name;
    private String s_code;
    private String ship_name;

    private String start_time;
    private String end_time;
    private String use_time;
    private String amt_use;
    private String amt_acc_day;
    private String amt_acc_mon;
    private String amt_bal;
    private String searchStartDateTime;
    private String excel_msg;
    private String finalSearchStartDate;
    private String finalSearchEndDate;
    private String finalChkSearchStartDate;
    private String finalChkSearchEndDate;


}
