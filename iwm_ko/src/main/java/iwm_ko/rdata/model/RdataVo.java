package iwm_ko.rdata.model;

import iwm_ko.comm.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class RdataVo extends BaseVo implements Serializable {

    private String sessionCompId;

    private String rate_name;
    private String comp_name;
    private String comp_id;
    private String s_name;
    private String s_code;
    private String ship_name;
    private String ship_code;
    private String crew_id;
    private String chKDel;

    private String amt_total_month;
    private String limit_day_amt;
    private String limit_cont_amt;
    private String limit_cont_time;
    private String time_from;
    private String time_to;
    private String apply_day;
    private String day_from;
    private String day_to;
    private String time_from2;
    private String time_to2;
    private String time_from3;
    private String time_to3;

    private String searchCompId;
    private String searchSName;
    private String searchRateName;

    private String addComp;
    private String addRateName;
    private String addMonth;
    private String addDay;
    private String addUse;
    private String addMin;
    private String addTimeFrom;
    private String addTimeTo;
    private String addApplyDays;
    private String addDayFrom;
    private String addDayTo;
    private String addTimeFrom2;
    private String addTimeTo2;
    private String addTimeFrom3;
    private String addTimeTo3;

    private String addName;


    //수정
    private String updSCode;
    private String updMonth;
    private String updDay;
    private String updUse;
    private String updMin;
    private String updTimeFrom;
    private String updTimeTo;
    private String updApplyDays;
    private String updDayFrom;
    private String updDayTo;
    private String updTimeFrom2;
    private String updTimeTo2;
    private String updTimeFrom3;
    private String updTimeTo3;
    private String updRateName;

    private String u_updRateName;

    private String excel_msg;


}
