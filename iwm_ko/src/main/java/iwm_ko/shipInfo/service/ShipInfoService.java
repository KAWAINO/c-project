package iwm_ko.shipInfo.service;

import iwm_ko.shipInfo.model.ShipInfoVo;

import java.util.HashMap;
import java.util.List;

public interface ShipInfoService {

    public List<ShipInfoVo> selectShipInfoList(ShipInfoVo shipInfoVo) throws Exception;

    public List<ShipInfoVo> selectShipInfoCount(ShipInfoVo shipInfoVo) throws Exception;

    public List<String> selectCompList(String scomp) throws Exception;

    public String chkInsertShipInfo(ShipInfoVo shipInfoVo) throws Exception;

    int insertShipInfo(ShipInfoVo shipInfoVo) throws Exception;

    public ShipInfoVo selectCompInfo(HashMap<String, Object> map) throws Exception;

    public int updateShipInsert(ShipInfoVo shipInfoVo) throws Exception;
    public int updateShipInfo(ShipInfoVo shipInfoVo) throws Exception;


    public int deleteShipInfo(ShipInfoVo shipInfoVo) throws  Exception;

    String chkShipName(ShipInfoVo shipInfoVo)throws Exception;

    List<ShipInfoVo> chkSCode(ShipInfoVo shipInfoVo)throws Exception;

    String chkDelFlag(ShipInfoVo shipInfoVo)throws Exception;

    String chkDel(ShipInfoVo shipInfoVo) throws Exception;
}
