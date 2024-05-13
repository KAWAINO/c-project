package iwm_ko.crewcdr.dao;


import iwm_ko.crewcdr.model.CrewcdrVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper
@Repository("iwm_ko.crewcdr.crewcdrDao")
public class CrewcdrDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.crewcdr.";

    public List<CrewcdrVo> selectCrewcdrList(CrewcdrVo crewcdrVo) throws Exception{
        List<CrewcdrVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCrewcdrList"),crewcdrVo);
        return resList;
    }


    public String selectTableName(CrewcdrVo crewcdrVo) throws Exception{
        return this.sqlSession.selectOne(NAME_SPACE.concat("selectTableName"),crewcdrVo);
    }

    public List<CrewcdrVo> selectShipCompList(CrewcdrVo crewcdrVo) throws Exception{
        List<CrewcdrVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipCompList"),crewcdrVo);
        return resList;
    }

    public List<CrewcdrVo> selectCrewcdrCount(CrewcdrVo crewcdrVo) throws Exception{
        List<CrewcdrVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCrewcdrCount"),crewcdrVo);
        return resList;

    }

    public List<CrewcdrVo> selectCrewIdList(String s_code) throws Exception{
        List<CrewcdrVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCrewIdList"),s_code);
        return resList;

    }

    public List<CrewcdrVo> selectShipNameList(String comp_id) throws Exception{
        List<CrewcdrVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipNameList"),comp_id);
        return resList;
    }
}
