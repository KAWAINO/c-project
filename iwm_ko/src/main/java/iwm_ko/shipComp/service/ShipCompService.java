package iwm_ko.shipComp.service;

import iwm_ko.shipComp.model.ShipCompVo;

import java.util.HashMap;
import java.util.List;

public interface ShipCompService {
    public List<ShipCompVo> selectShipCompList(ShipCompVo shipCompVo) throws Exception;

    public List<ShipCompVo> selectShipCompCount(ShipCompVo shipCompVo) throws Exception;

    public List<ShipCompVo> chkInsertShipComp(ShipCompVo shipCompVo) throws Exception;

    public int insertShipComp(ShipCompVo shipCompVo)throws Exception;
    public ShipCompVo selectCompInfo(HashMap<String, Object> map) throws Exception;


    public int compIdSeq() throws Exception;

    public int updateShipComp(ShipCompVo shipCompVo) throws Exception;

    public int deleteShipComp(ShipCompVo shipCompVo) throws Exception;

    String chkFlag(ShipCompVo shipCompVo) throws Exception;

    int updateInsertShipComp(ShipCompVo shipCompVo);


    String chkDel(ShipCompVo shipCompVo) throws Exception;
}
