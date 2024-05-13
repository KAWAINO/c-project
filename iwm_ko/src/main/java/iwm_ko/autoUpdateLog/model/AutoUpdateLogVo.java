package iwm_ko.autoUpdateLog.model;

import iwm_ko.comm.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter
@Setter
@ToString
public class AutoUpdateLogVo extends BaseVo implements Serializable {

    private String comp_id;
    private String p_name;
    private String v1;
    private String v2;
    private String last_result;
    private String update_date;
    private String s_code;
    private String comp_name;
    private String s_name;

    private String searchCompId;
    private String searchSCode;
    private String cs_upd;
    private String endRow;



}
