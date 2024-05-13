package iwm_ko.autoUpdate.model;

import iwm_ko.comm.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class AutoUpdateVo extends BaseVo implements Serializable {

    private String comp_id;


    private String seq;
    private String p_name;
    private String version;
    private String file_loc;
    private String local_loc;
    private String down_loc;
    private String svr_proc_flag;
    private String download_flag;
    private String down_file_svr;
    private String down_file_cli;
    private String down_file_name;
    private String cmd_flag;
    private String cmd_str;
    private String script_flag;
    private String script_file;


    private String add_name;
    private String add_version;
    private String add_svr_loc;
    private String add_local_loc;
    private String add_proc_flag;
    private String add_down_loc;
    private String add_down_flag;
    private String add_down_svr;
    private String add_down_local;
    private String add_down_name;
    private String add_cmd_flag;
    private String add_cmd_str;
    private String add_script_flag;
    private String add_script_loc;

    private String upd_name;
    private String upd_version;
    private String upd_file_loc;
    private String upd_local_loc;
    private String upd_down_loc;
    private String upd_svr_proc_flag;
    private String upd_download_flag;
    private String upd_down_file_svr;
    private String upd_down_file_cli;
    private String upd_down_file_name;
    private String upd_cmd_flag;
    private String upd_cmd_str;
    private String upd_script_flag;
    private String upd_script_file;

}
