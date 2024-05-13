package iwm_ko.dport.dao;

import iwm_ko.dport.model.DportVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper
@Repository("iwm_ko.dport.dportDao")
public class DportDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.dport.";


    public List<DportVo> selectDportList(DportVo dportVo) throws Exception {
        List<DportVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectDportList"),dportVo);
        return resList;
    }

    public List<DportVo> chkDport(DportVo dportVo) throws Exception {
        List<DportVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("chkDport"),dportVo);
        return resList;
    }

    public int insertDport(DportVo dportVo) throws Exception{
        return this.sqlSession.insert(NAME_SPACE.concat("insertDport"),dportVo);
    }

    public int deleteDport(DportVo dportVo) throws Exception {
        return this.sqlSession.delete(NAME_SPACE.concat("deleteDport"),dportVo);

    }
}
