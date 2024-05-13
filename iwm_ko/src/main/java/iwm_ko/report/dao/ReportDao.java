package iwm_ko.report.dao;

import iwm_ko.report.model.ReportVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
//@Mapper
@Repository("iwm_ko.report.reportDao")
public class ReportDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.report.";


    public List<ReportVo> selectCompList(ReportVo reportVo) throws Exception{
        List<ReportVo>resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCompList"),reportVo);
        return resList;

    }

    public List<ReportVo> selectShipNameList(String compId) throws Exception{
        List<ReportVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipNameList"),compId  );
        return resList;
    }

    public String chkTable(ReportVo reportVo) throws Exception{
        return this.sqlSession.selectOne(NAME_SPACE.concat("chkTable"),reportVo);
    }

    public List<ReportVo> getDataList(ReportVo reportVo) throws Exception{
        List<ReportVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("getDataList"),reportVo);
        return resList;
    }

    public String preTable(ReportVo reportVo) throws Exception{
        return this.sqlSession.selectOne(NAME_SPACE.concat("preTable"),reportVo);
    }

    public String getShipName(ReportVo reportVo) {
        return this.sqlSession.selectOne(NAME_SPACE.concat("getShipName"),reportVo);
    }

    public List<ReportVo> selectShipList(ReportVo reportVo) {
        List<ReportVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipList"),reportVo);
        return resList;
    }
}
