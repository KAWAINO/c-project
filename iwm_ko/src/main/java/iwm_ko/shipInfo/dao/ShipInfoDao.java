package iwm_ko.shipInfo.dao;


import iwm_ko.shipInfo.model.ShipInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

//@Mapper
@Repository("iwm_ko.shipInfo.shipInfoDao")
public class ShipInfoDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.shipInfo.";


    public List<ShipInfoVo> selectShipInfoList(ShipInfoVo shipInfoVo) throws Exception{
        List<ShipInfoVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipInfoList"),shipInfoVo);
        return resList;
    }

    public List<ShipInfoVo> selectShipInfoCount(ShipInfoVo shipInfoVo) throws Exception{
        List<ShipInfoVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipInfoCount"),shipInfoVo);
        return resList;

    }

    public List<String> selectCompList(String scomp) throws Exception{
        List<String> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCompList"),scomp);
        return resList;
    }

    public String chkShipInfo(ShipInfoVo shipInfoVo) throws Exception{
        String resData = this.sqlSession.selectOne(NAME_SPACE.concat("chkShipInfo"),shipInfoVo);
        return resData;
    }


    public int insertShipInfo(ShipInfoVo shipInfoVo) throws Exception {
        return sqlSession.insert(NAME_SPACE.concat("insertShipInfo"),shipInfoVo);
    }


    public ShipInfoVo selectCompInfo(HashMap<String, Object> map) throws Exception{
        ShipInfoVo resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectCompInfo"), map);
        return resData;
    }

    public int updateShipInsert(ShipInfoVo shipInfoVo) {
        return sqlSession.update(NAME_SPACE.concat("updateShipInsert"),shipInfoVo);
    }
    public int updateShipInfo(ShipInfoVo shipInfoVo) {
        return sqlSession.update(NAME_SPACE.concat("updateShipInfo"),shipInfoVo);
    }


    public String chkShipName(ShipInfoVo shipInfoVo) throws Exception{
        return sqlSession.selectOne(NAME_SPACE.concat("chkShipName"),shipInfoVo);
    }

    public List<ShipInfoVo> chkSCode(ShipInfoVo shipInfoVo) throws Exception{
        return sqlSession.selectList(NAME_SPACE.concat("chkSCode"),shipInfoVo);
    }

    public String chkDelFlag(ShipInfoVo shipInfoVo) throws Exception{
        return sqlSession.selectOne(NAME_SPACE.concat("chkDelFlag"),shipInfoVo);
    }

    public String chkDel(ShipInfoVo shipInfoVo) throws Exception{
        return sqlSession.selectOne(NAME_SPACE.concat("chkDel"),shipInfoVo);
    }
}
