package iwm_ko.wapStat.dao;

import iwm_ko.wapStat.model.WapStatVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper
@Repository("iwm_ko.wapStat.wapStatDao")
public class WapStatDao {


    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.wapStat.";


    public List<WapStatVo> selectCompList(WapStatVo wapStatVo)  throws Exception{
        List<WapStatVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCompList"),wapStatVo);
        return resList;
    }

    public String selectTableName(WapStatVo wapStatVo) throws Exception{
        return this.sqlSession.selectOne(NAME_SPACE.concat("selectTableName"),wapStatVo);
    }


    public List<WapStatVo> selectWapStatList(WapStatVo wapStatVo) throws Exception{
        List<WapStatVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectWapStatList"),wapStatVo);
        return resList;

    }

    public List<WapStatVo> selectWapStatCount(WapStatVo wapStatVo) throws Exception{
        List<WapStatVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectWapStatCount"),wapStatVo);
        return resList;
    }

    public List<WapStatVo> selectWapList(WapStatVo wapStatVo) throws Exception{
        List<WapStatVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectWapList"),wapStatVo);
        return resList;
    }

    public List<WapStatVo> selectShipNameList(String comp_id) throws Exception{
    List<WapStatVo>resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipNameList"),comp_id);
    return resList;
    }


}
