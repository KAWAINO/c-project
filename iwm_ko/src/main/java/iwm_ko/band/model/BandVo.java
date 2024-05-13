package iwm_ko.band.model;

import iwm_ko.comm.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class BandVo extends BaseVo implements Serializable {

    private String comp_id;
    private String band_width;

    private String add_band;
}
