package iwm_ko.shipComp.model;

import iwm_ko.comm.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ShipCompVo extends BaseVo implements Serializable {

    private String sessionCompId;

    private String comp_id;
    private String comp_name;

    private int s_cnt;
    private int a_cnt;
    private String ship_hold;
    private String descr;

    private String searchShipComp;

    private String addCompName;

    private String addCompId;

    private String addCompCnt;

   private String addShipHold;

   private String addDescr;

   private String u_updCompName;

   private String updCompName;

   private String updCompHold;

   private String updDescr;

   private String excel_msg;
   private String updCompId;

   private String chkDel;



}
