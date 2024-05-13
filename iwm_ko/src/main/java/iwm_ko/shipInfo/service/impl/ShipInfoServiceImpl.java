package iwm_ko.shipInfo.service.impl;

import iwm_ko.shipInfo.dao.ShipInfoDao;
import iwm_ko.shipInfo.model.ShipInfoVo;
import iwm_ko.shipInfo.service.ShipInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;

@Service("iwm_ko.shipInfo.ShipInfoService")
//@MapperScan("iwm_ko.shipInfo.dao")
public class ShipInfoServiceImpl implements ShipInfoService {

    private JdbcTemplate jdbcTemplate;
    @Resource(name="iwm_ko.shipInfo.shipInfoDao")
    private ShipInfoDao shipInfoDao;

    @Override
    public List<ShipInfoVo> selectShipInfoList(ShipInfoVo shipInfoVo) throws Exception {
        List<ShipInfoVo> resList = this.shipInfoDao.selectShipInfoList(shipInfoVo);
        return resList;
    }

    @Override
    public List<ShipInfoVo> selectShipInfoCount(ShipInfoVo shipInfoVo) throws Exception {
        List<ShipInfoVo> resList = this.shipInfoDao.selectShipInfoCount(shipInfoVo);
        return resList;
    }

    @Override
    public List<String> selectCompList(String scomp) throws Exception {
        List<String> resList = this.shipInfoDao.selectCompList(scomp);
        return resList;
    }

    @Override
    public String chkInsertShipInfo(ShipInfoVo shipInfoVo) throws Exception {
        String resData = this.shipInfoDao.chkShipInfo(shipInfoVo);
        return resData;
    }

    @Override
    public int insertShipInfo(ShipInfoVo shipInfoVo) throws Exception {
        return this.shipInfoDao.insertShipInfo(shipInfoVo);
    }

    @Override
    public ShipInfoVo selectCompInfo(HashMap<String, Object> map) throws Exception {
        return this.shipInfoDao.selectCompInfo(map);
    }

    @Override
    public int updateShipInsert(ShipInfoVo shipInfoVo) throws Exception {
        return this.shipInfoDao.updateShipInsert(shipInfoVo);
    }
    @Override
    public int updateShipInfo(ShipInfoVo shipInfoVo) throws Exception {
        return this.shipInfoDao.updateShipInfo(shipInfoVo);
    }


    public void ShipService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Autowired
    public ShipInfoServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int deleteShipInfo(ShipInfoVo shipInfoVo) {
        String sql = "{call RPC_CLEAN_SCODE (?,?)}";
        String scode = shipInfoVo.getS_code();

        return jdbcTemplate.execute(
                sql,
                (CallableStatement cs) -> {
                    cs.setString(1, scode);
                    cs.registerOutParameter(2, Types.INTEGER);
                    cs.execute();
                    return cs.getInt(2);
                }
        );

    }

    @Override
    public String chkShipName(ShipInfoVo shipInfoVo) throws Exception {
        return this.shipInfoDao.chkShipName(shipInfoVo);
    }

    @Override
    public List<ShipInfoVo> chkSCode(ShipInfoVo shipInfoVo) throws Exception {
        return this.shipInfoDao.chkSCode(shipInfoVo);
    }

    @Override
    public String chkDelFlag(ShipInfoVo shipInfoVo) throws Exception {
        return this.shipInfoDao.chkDelFlag(shipInfoVo);
    }

    @Override
    public String chkDel(ShipInfoVo shipInfoVo) throws Exception {
        return this.shipInfoDao.chkDel(shipInfoVo);
    }

}
