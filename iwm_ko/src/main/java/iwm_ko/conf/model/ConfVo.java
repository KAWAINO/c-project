package iwm_ko.conf.model;

import iwm_ko.comm.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ConfVo extends BaseVo implements Serializable {

    private String excel_msg;
    private String comp_id;


    private String conf_name;
    private String val;
    private String descr;

    private String searchConf;

    private String add_conf;
    private String add_val;
    private String add_descr;

    private String upd_conf;
    private String u_upd_conf;
    private String upd_val;
    private String upd_descr;



}
