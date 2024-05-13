package iwm_ko.swUpdate.dao;

import iwm_ko.swUpdate.model.SwUpdateVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

//@Mapper
@Repository("iwm_ko.swUpdate.swUpdateDao")
public class SwUpdateDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.swUpdate.";


    public List<SwUpdateVo> selectCompList(SwUpdateVo swUpdateVo) throws Exception{
        List<SwUpdateVo> resList= this.sqlSession.selectList(NAME_SPACE.concat("selectCompList"),swUpdateVo);
        return resList;

    }

    public List<SwUpdateVo> selectSwUpdateList(SwUpdateVo swUpdateVo) throws Exception{
        List<SwUpdateVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectSwUpdateList"),swUpdateVo);
        return resList;

    }

    public List<SwUpdateVo> selectShipNameList(String comp_id) throws Exception{
        List<SwUpdateVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipNameList"),comp_id);
        return resList;

    }

    public List<SwUpdateVo> chkSch(SwUpdateVo swUpdateVo) throws Exception{
        List<SwUpdateVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("chkSch"),swUpdateVo);
        return resList;
    }

    public int selectSeq() throws Exception{
        return this.sqlSession.selectOne(NAME_SPACE.concat("selectSeq"));
    }

    public int insertSwUpdate(SwUpdateVo swUpdateVo) throws Exception{
        return this.sqlSession.insert(NAME_SPACE.concat("insertSwUpdate"),swUpdateVo);

    }

    public int insertSwUpdateSub(SwUpdateVo swUpdateVo) throws Exception{
        return this.sqlSession.insert(NAME_SPACE.concat("insertSwUpdateSub"),swUpdateVo);

    }

    public SwUpdateVo selectSwUpdateInfo(HashMap<String, Object> req) throws Exception{
        return this.sqlSession.selectOne(NAME_SPACE.concat("selectSwUpdateInfo"),req);

    }

    public int updateSwUpdate(SwUpdateVo swUpdateVo) throws Exception{
        return this.sqlSession.update(NAME_SPACE.concat("updateSwUpdate"),swUpdateVo);
    }

    public int deleteSwUpdate(SwUpdateVo swUpdateVo) throws Exception{
        return this.sqlSession.delete(NAME_SPACE.concat("deleteSwUpdate"),swUpdateVo);
    }

    public int deleteSwUpdateSub(SwUpdateVo swUpdateVo) throws Exception{
        return this.sqlSession.delete(NAME_SPACE.concat("deleteSwUpdateSub"),swUpdateVo);

    }
}
