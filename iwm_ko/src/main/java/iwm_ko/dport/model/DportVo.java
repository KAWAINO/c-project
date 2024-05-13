package iwm_ko.dport.model;

import iwm_ko.comm.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class DportVo extends BaseVo implements Serializable {

    private String comp_id;
    private String port_from;
    private String port_to;

    private String add_from;
    private String add_to;


}
