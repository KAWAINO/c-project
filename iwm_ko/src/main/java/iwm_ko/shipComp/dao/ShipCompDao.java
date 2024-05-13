package iwm_ko.shipComp.dao;


import iwm_ko.shipComp.model.ShipCompVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

//@Mapper
@Repository("iwm_ko.shipComp.shipCompDao")
public class ShipCompDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.shipComp.";

    public List<ShipCompVo> selectShipCompList(ShipCompVo shipCompVo) throws Exception{
        List<ShipCompVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipCompList"),shipCompVo);
        return resList;
    }

    public List<ShipCompVo> selectShipCompCount(ShipCompVo shipCompVo) throws Exception{
        List<ShipCompVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipCompCount"),shipCompVo);
        return resList;
    }

    public List<ShipCompVo> chkShipComp(ShipCompVo shipCompVo) throws Exception{
        List<ShipCompVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("chkShipComp"),shipCompVo);
        return resData;
    }

    public int insertShipComp(ShipCompVo shipCompVo) throws Exception {
        return sqlSession.insert(NAME_SPACE.concat("insertShipComp"),shipCompVo);
    }

    public ShipCompVo selectCompInfo(HashMap<String, Object> map) throws Exception{
        ShipCompVo resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectCompInfo"), map);
        return resData;
    }


    public int selectCompIdSeq() throws Exception{
        return sqlSession.selectOne(NAME_SPACE.concat("selectCompIdSeq"));
    }

    public int updateShipComp(ShipCompVo shipCompVo) {
        return sqlSession.update(NAME_SPACE.concat("updateShipComp"),shipCompVo);
    }


    public int deleteShipComp(ShipCompVo shipCompVo) throws Exception{
        return sqlSession.update(NAME_SPACE.concat("deleteShipComp"),shipCompVo);
    }


    public String chkFlag(ShipCompVo shipCompVo) {
        return sqlSession.selectOne(NAME_SPACE.concat("chkFlag"),shipCompVo);
    }

    public int updateInsertShipComp(ShipCompVo shipCompVo) {
        return sqlSession.update(NAME_SPACE.concat("updateInsertShipComp"),shipCompVo);
    }

    public String chkDel(ShipCompVo shipCompVo) {
        return sqlSession.selectOne(NAME_SPACE.concat("chkDel"),shipCompVo);
    }
}
