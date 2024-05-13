package iwm_ko.band.dao;

import iwm_ko.band.model.BandVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper
@Repository("iwm_ko.band.bandDao")
public class BandDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.band.";


    public List<BandVo> selectBandList(BandVo bandVo) throws Exception{
        List<BandVo>resList = this.sqlSession.selectList(NAME_SPACE.concat("selectBandList"),bandVo);
        return resList;
    }

    public List<BandVo> chkBand(BandVo bandVo) throws Exception {
        List<BandVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("chkBand"),bandVo);
        return resList;
    }

    public int insertBand(BandVo bandVo) throws Exception{
        return this.sqlSession.insert(NAME_SPACE.concat("insertBand"),bandVo);
    }

    public int deleteBand(BandVo bandVo) throws Exception{
        return this.sqlSession.delete(NAME_SPACE.concat("deleteBand"),bandVo);
    }
}
