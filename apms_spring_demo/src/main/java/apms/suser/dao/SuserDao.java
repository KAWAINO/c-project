package apms.suser.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import apms.suser.model.GradeVo;
import apms.suser.model.SuserVo;

@Repository("apms.suser.suserDao")
public class SuserDao {
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "apms.suser.";
	
	public List<GradeVo> selectUserGrade() throws Exception{
		List<GradeVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectUserGrade"));
		return resData;
	}
	
	public List<SuserVo> chkInsertSuser(SuserVo suserVo) throws Exception{
		List<SuserVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("chkInsertSuser"), suserVo);
		return resData;
	}
	
	public List<SuserVo> selectUserList(SuserVo suserVo) throws Exception{
		List<SuserVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectUserList"), suserVo);
		return resData;
	}
	
    public int insertSuser(SuserVo suserVo) {
        return sqlSession.insert(NAME_SPACE.concat("insertSuser"), suserVo);
    }
	
	public SuserVo selectSuserInfo(HashMap<String, Object> map) throws Exception{
		SuserVo resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectSuserInfo"), map);
		return resData;
	}
	
    public int updateSuser(SuserVo suserVo) {
        return sqlSession.update(NAME_SPACE.concat("updateSuser"), suserVo);
    }
	
    public int deleteSuser(SuserVo suserVo) {
        return sqlSession.update(NAME_SPACE.concat("deleteSuser"), suserVo);
    }
}
