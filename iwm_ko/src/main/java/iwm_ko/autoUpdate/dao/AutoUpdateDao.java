package iwm_ko.autoUpdate.dao;


import iwm_ko.autoUpdate.model.AutoUpdateVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

//@Mapper
@Repository("iwm_ko.autoUpdate.autoUpdateDao")
public class AutoUpdateDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.autoUpdate.";

    public List<AutoUpdateVo> selectAutoUpdateList(AutoUpdateVo autoUpdateVo) throws Exception{
        List<AutoUpdateVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectAutoUpdateList"),autoUpdateVo);
        return resList;
    }

    public List<AutoUpdateVo> selectAutoUpdateCount(AutoUpdateVo autoUpdateVo) throws Exception{
        List<AutoUpdateVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectAutoUpdateCount"),autoUpdateVo);
        return resList;
    }

    public List<AutoUpdateVo> chkAutoUpdate(AutoUpdateVo autoUpdateVo) throws Exception{
        List<AutoUpdateVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("chkAutoUpdate"),autoUpdateVo);
        return resList;
    }

    public int insertAutoUpdate(AutoUpdateVo autoUpdateVo) throws Exception{
        return this.sqlSession.insert(NAME_SPACE.concat("insertAutoUpdate"),autoUpdateVo);

    }

    public String selectSeq() throws Exception{
        return this.sqlSession.selectOne(NAME_SPACE.concat("selectSeq"));
    }

    public AutoUpdateVo selectAutoUpdateInfo(HashMap<String, Object> req) throws Exception{
        return this.sqlSession.selectOne(NAME_SPACE.concat("selectAutoUpdateInfo"),req);

    }

    public int updateAutoUpdate(AutoUpdateVo autoUpdateVo) throws Exception{
        return this.sqlSession.update(NAME_SPACE.concat("updateAutoUpdate"),autoUpdateVo);

    }

    public int deleteAutoUpdate(AutoUpdateVo autoUpdateVo) throws Exception{
        return this.sqlSession.delete(NAME_SPACE.concat("deleteAutoUpdate"),autoUpdateVo);

    }
}
