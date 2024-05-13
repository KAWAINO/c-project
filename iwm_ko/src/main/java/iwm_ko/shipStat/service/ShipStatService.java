package iwm_ko.shipStat.service;


import iwm_ko.shipStat.model.ShipStatVo;

import java.util.Collection;
import java.util.List;

public interface ShipStatService{


    public    List<ShipStatVo> selectShipStatList(ShipStatVo shipStatVo) throws  Exception;

    public String selectTableName(ShipStatVo shipStatVo) throws Exception;


    public List<ShipStatVo> selectCompList(ShipStatVo shipStatVo) throws Exception;

    List<ShipStatVo> selectShipStatCount(ShipStatVo shipStatVo) throws  Exception;

    public List<ShipStatVo> shipNameList(String comp_id) throws Exception;
}
