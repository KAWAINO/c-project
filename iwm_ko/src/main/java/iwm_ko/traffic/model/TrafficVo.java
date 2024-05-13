package iwm_ko.traffic.model;

import iwm_ko.comm.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@ToString
@Getter
@Setter
public class TrafficVo extends BaseVo implements Serializable {
    private String comp_id;
    private String comp_name;
    private String ship_name;
    private String s_code;
    private String s_name;
    private String seq_traf_sch;
    private String sch_name;
    private String sch_unit;
    private String sch_interval;
    private String sch_hour;
    private String sch_min;
    private Integer add_seq;
    private String add_unit;
    private String add_sch;
    private String add_interval;
    private String add_min;
    private String add_hour;
    private String addSCode;
    private String upd_unit;
    private String upd_interval;
    private String upd_hour;
    private String upd_min;
    private String upd_sch;






}
