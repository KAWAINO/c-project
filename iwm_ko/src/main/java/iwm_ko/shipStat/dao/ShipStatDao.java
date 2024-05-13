package iwm_ko.shipStat.dao;

import iwm_ko.shipStat.model.ShipStatVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper
@Repository("iwm_ko.shipStat.shipStatDao")
public class ShipStatDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.shipStat.";

    public List<ShipStatVo> selectShipStatList(ShipStatVo shipStatVo) throws Exception {
        List<ShipStatVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipStatList"),shipStatVo);
        return resList;
    }

    public String selectTableName(ShipStatVo shipStatVo) throws Exception{
        return this.sqlSession.selectOne(NAME_SPACE.concat("selectTableName"),shipStatVo);
    }

    public List<ShipStatVo> selectShipStatCount(ShipStatVo shipStatVo) throws Exception{
        return this.sqlSession.selectList(NAME_SPACE.concat("selectShipStatCount"),shipStatVo);
    }


    public List<ShipStatVo> selectCompList(ShipStatVo shipStatVo) {
        List<ShipStatVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCompList"),shipStatVo);
        return resList;
    }

    public List<ShipStatVo> selectShipNameList(String comp_id) throws Exception{
        List<ShipStatVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipNameList"),comp_id);
        return resList;
    }
}
