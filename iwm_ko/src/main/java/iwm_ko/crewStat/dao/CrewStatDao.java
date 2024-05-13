package iwm_ko.crewStat.dao;


import iwm_ko.crewStat.model.CrewStatVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper
@Repository("iwm_ko.crewStat.crewStatDao")
public class CrewStatDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.crewStat.";


    public List<CrewStatVo> selectCompList(CrewStatVo crewStatVo) throws Exception{
        List<CrewStatVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCompList"),crewStatVo);
        return resList;
    }

    public String selectTableName(CrewStatVo crewStatVo) throws Exception{
       return this.sqlSession.selectOne(NAME_SPACE.concat("selectTableName"),crewStatVo);

    }

    public List<CrewStatVo> selectCrewStatList(CrewStatVo crewStatVo) throws Exception{
        List<CrewStatVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCrewStatList"),crewStatVo);
        return resList;
    }

    public List<CrewStatVo> selectCrewStatCount(CrewStatVo crewStatVo) throws Exception{
        List<CrewStatVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCrewStatCount"),crewStatVo);
        return resList;
    }

    public List<CrewStatVo> crewSelectList(CrewStatVo crewStatVo) {
        List<CrewStatVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("crewSelectList"),crewStatVo);
        return resList;

    }

    public List<CrewStatVo> selectShipNameList(String comp_id) {
        List<CrewStatVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipNameList"),comp_id);
        return resList;
    }
}
