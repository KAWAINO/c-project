package iwm_ko.report.model;

import iwm_ko.comm.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@ToString
@Getter
public class ReportVo extends BaseVo implements Serializable {

    private String sessionCompId;

    private String user_id;
    private String comp_id;
    private String comp_name;
    private String viewer;
    private String s_code;
    private String s_name;
    private String stime;
    private String etime;

    private String searchCompId;
    private String searchSCode;
    private String cs_year;
    private String cs_month;
    private String tableName;
    private String pre_month;
    private String preTableName;
    private String preSTime;
    private String preETime;
    private String s_year;
    private String s_month;



    private String crew_id;
    private String crew_name;
    private String amt_total_month;
    private String val2;
    private String imgData;
    private String result;
    private String redirectUrl;
    private String path;
    private String startDate;
    private String endDate;
    private String cs_comp;
    private String cs_ship;
    private String img_path;



}
