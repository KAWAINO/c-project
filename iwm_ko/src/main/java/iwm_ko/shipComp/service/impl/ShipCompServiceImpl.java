package iwm_ko.shipComp.service.impl;

import iwm_ko.shipComp.dao.ShipCompDao;
import iwm_ko.shipComp.model.ShipCompVo;
import iwm_ko.shipComp.service.ShipCompService;
import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("iwm_ko.shipComp.ShipCompService")
//@MapperScan("iwm_ko.shipComp.dao")
public class ShipCompServiceImpl implements ShipCompService {
	
    @Resource(name="iwm_ko.shipComp.shipCompDao")
    private ShipCompDao shipCompDao;

    @Override
    public List<ShipCompVo> selectShipCompList(ShipCompVo shipCompVo) throws Exception {
        List<ShipCompVo> resList = this.shipCompDao.selectShipCompList(shipCompVo);
        return resList;
    }

    @Override
    public List<ShipCompVo> selectShipCompCount(ShipCompVo shipCompVo) throws Exception {
        List<ShipCompVo> resList = this.shipCompDao.selectShipCompCount(shipCompVo);
        return resList;
    }

    @Override
    public List<ShipCompVo> chkInsertShipComp(ShipCompVo sprotocolVo) throws Exception {
        List<ShipCompVo> resData = this.shipCompDao.chkShipComp(sprotocolVo);
        return resData;
    }

    @Override
    public int insertShipComp(ShipCompVo shipCompVo) throws Exception {
        return this.shipCompDao.insertShipComp(shipCompVo);
    }

    @Override
    public ShipCompVo selectCompInfo(HashMap<String, Object> map) throws Exception {
        return this.shipCompDao.selectCompInfo(map);
    }

    @Override
    public int compIdSeq() throws Exception {
        return this.shipCompDao.selectCompIdSeq();
    }

    @Override
    public int updateShipComp(ShipCompVo shipCompVo) throws Exception {
        return this.shipCompDao.updateShipComp(shipCompVo);
    }

    @Override
    public int deleteShipComp(ShipCompVo shipCompVo) throws Exception {
        return this.shipCompDao.deleteShipComp(shipCompVo);
    }

    @Override
    public String chkFlag(ShipCompVo shipCompVo) throws Exception {
        return this.shipCompDao.chkFlag(shipCompVo);
    }

    @Override
    public int updateInsertShipComp(ShipCompVo shipCompVo) {
        return this.shipCompDao.updateInsertShipComp(shipCompVo);
    }

    @Override
    public String chkDel(ShipCompVo shipCompVo) throws Exception {
        return this.shipCompDao.chkDel(shipCompVo);
    }


}
