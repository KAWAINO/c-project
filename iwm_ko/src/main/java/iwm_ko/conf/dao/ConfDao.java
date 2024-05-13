package iwm_ko.conf.dao;

import iwm_ko.conf.model.ConfVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

//@Mapper
@Repository("iwm_ko.conf.confDao")
public class ConfDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.conf.";


    public List<ConfVo> selectConfList(ConfVo confVo) throws Exception{
        List<ConfVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectConfList"),confVo);
        return resList;
    }

    public List<ConfVo> selectConfCount(ConfVo confVo) throws Exception{
        List<ConfVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectConfCount"),confVo);
        return resList;

    }

    public List<ConfVo> chkConf(ConfVo confVo) throws Exception{
        List<ConfVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("chkConf"),confVo);
        return resList;
    }

    public int insertConf(ConfVo confVo) throws Exception{
        return this.sqlSession.insert(NAME_SPACE.concat("insertConf"),confVo);


    }

    public ConfVo selectConfInfo(HashMap<String, Object> req)  throws Exception{
        return this.sqlSession.selectOne(NAME_SPACE.concat("selectConfInfo"),req);

    }

    public int updateConf(ConfVo confVo) throws  Exception{
        return this.sqlSession.update(NAME_SPACE.concat("updateConf"),confVo);
    }

    public int deleteConf(ConfVo confVo)throws Exception {
        return this.sqlSession.delete(NAME_SPACE.concat("deleteConf"),confVo);

    }
}
