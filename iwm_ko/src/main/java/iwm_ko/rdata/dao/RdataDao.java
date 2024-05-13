package iwm_ko.rdata.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.rdata.model.RdataVo;

//@Mapper
@Repository("iwm_ko.rdata.rdataDao")
public class RdataDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.rdata.";


    public List<RdataVo> selectRdataList(RdataVo rdataVo, Locale locale) throws Exception{
        List<RdataVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectRdataList"),rdataVo);
        return resList;
    }

    public List<RdataVo> selectRdataCount(RdataVo rdataVo) throws Exception{
        List<RdataVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectRdataCount"),rdataVo);
        return resList;
    }

    public List<RdataVo> selectShipCompList(RdataVo rdataVo) throws Exception{
        List<RdataVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipCompList"),rdataVo);
        return resList;
    }


    public List<RdataVo> selectShipNameList(String compId) {
        List<RdataVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipNameList"),compId);
        return resList;
    }
    public List<RdataVo> selectRateNameList(String s_name) {
        List<RdataVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectRateNameList"),s_name);
        return resList;
    }
    public List<RdataVo> chkRdata(RdataVo rdataVo) throws Exception{
        List<RdataVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("chkRdata"),rdataVo);
        return resData;
    }

    public int insertRdata(RdataVo rdataVo) throws Exception {
        return sqlSession.insert(NAME_SPACE.concat("insertRdata"),rdataVo);
    }

    public RdataVo selectRdataInfo(HashMap<String, Object> map) throws Exception{
        RdataVo resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectRdataInfo"), map);
        return resData;
    }

    public int updateRdata(RdataVo rdataVo) {
        return sqlSession.update(NAME_SPACE.concat("updateRdata"),rdataVo);
    }

    public int deleteRdata(RdataVo rdataVo) throws Exception{
        return sqlSession.update(NAME_SPACE.concat("deleteRdata"),rdataVo);
    }

    public List<RdataVo> selectCrewIdList(String s_name) {
        List<RdataVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCrewIdList"),s_name);
        return resList;
    }

    public String chkDel(RdataVo rdataVo) throws Exception{
        return this.sqlSession.selectOne(NAME_SPACE.concat("chkDel"),rdataVo);
    }
}
