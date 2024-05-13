package iwm_ko.crewInfo.model;

import iwm_ko.comm.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@ToString
public class CrewInfoVo extends BaseVo implements Serializable {

    private  String excel_msg;
    private String searchBtn;

    private String sessionCompId;
    private String cs_code;
    private String sessionUserId;
    private String crewNameSCode;
    private String crewNameCrewId;

    private String searchCompId;

    private String comp_id;

    private String comp_name;

    private String searchSName;

    private String searchIdName;
    private String ship_name;

    private String s_code;
    private String s_name;
    private String crew_name;
    private String crew_id;
    private String crew_status;
    private String acc_use_day;
    private String acc_use_month;
    private String amt_rest;
    private String add_remain;
    private String rate_name;
    private String amt_total_month;
    private String limit_day_amt;
    private String limit_cont_amt;
    private String limit_cont_time;
    private String time_from;
    private String time_from2;
    private String time_from3;
    private String time_to;
    private String time_to2;
    private String time_to3;
    private String apply_day;
    private String wifi_conn;
    private String val2;
    private String day_from;
    private String day_to;
    private String del_flag;


    //추가

    private String addCrewId;
    private String addCrewName;

    private String addPw;

    private String addPwChk;

    private String addComp;

    private String addSName;
    private String addRdata;
    private String addVal2;
    private String addMonth;
    private String addDay;
    private String addUse;
    private String addMin;
    private String addTimeFrom;
    private String addTimeFrom2;
    private String addTimeFrom3;
    private String addTimeTo;
    private String addTimeTo2;
    private String addTimeTo3;
    private String addApplyDays;
    private String addDayFrom;
    private String addDayTo;

    //수정

    private String u_updCrewId;
    private String updCrewId;
    private String updCrewName;
    private String updPw;
    private String changePw;
    private String updCompName;
    private String u_updCompName;
    private String updSName;
    private String u_updSName;
    private String u_updSCode;
    private String updRdata;
    private String updVal2;
    private String updMonth;
    private String updDay;
    private String updMin;
    private String updUse;
    private String updTimeFrom;
    private String updTimeFrom2;
    private String updTimeFrom3;
    private String updTimeTo;
    private String updTimeTo2;
    private String updTimeTo3;

    private String updDayFrom;
    private String updDayTo;
    private String updApplyDays;


    private String cs_select;
    private List<String> selectedCrewIds;
    private String save_flag;
    private String allSCode;
    private String cs_data;
    private String defaultRateName;
    private String allVal2;
    private String allRdata;



}
