package iwm_ko.autoUpdateLog.dao;

import iwm_ko.autoUpdateLog.model.AutoUpdateLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper
@Repository("iwm_ko.autoUpdateLog.autoUpdateLogDao")
public class AutoUpdateLogDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.autoUpdateLog.";

    public List<AutoUpdateLogVo> selectAutoUpdateLogList(AutoUpdateLogVo autoUpdateLogVo) throws Exception{
        List<AutoUpdateLogVo>resList = this.sqlSession.selectList(NAME_SPACE.concat("selectAutoUpdateLogList"),autoUpdateLogVo);
        return resList;
    }

    public List<AutoUpdateLogVo> selectCount(AutoUpdateLogVo autoUpdateLogVo) throws Exception{
        List<AutoUpdateLogVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCount"),autoUpdateLogVo);
        return resList;
    }

    public List<AutoUpdateLogVo> selectCompList(AutoUpdateLogVo autoUpdateLogVo) throws Exception{
        List<AutoUpdateLogVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCompList"),autoUpdateLogVo);
        return resList;

    }

    public List<AutoUpdateLogVo> selectShipNameList(String comp_id) throws Exception{
        List<AutoUpdateLogVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipNameList"),comp_id);
        return resList;
    }
}
