package radius.suser.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import radius.suser.model.SuserVo;

@Repository("radius.suser.suserDao")
public class SuserDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "radius.suser.";
	
	// 운영자관리 르스트 조회
	public List<SuserVo> selectSuserList(SuserVo suserVo) throws Exception {
		List<SuserVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectSuserList"), suserVo);
		return resData;
	}

	// 페이징
	public List<SuserVo> getTotalSuserList(SuserVo suserVo) throws Exception {
		List<SuserVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("getTotalSuserList"), suserVo);
		return resData;
	}

	// 중복검사
	public List<SuserVo> chkInsertSuser(SuserVo suserVo) throws Exception {
		List<SuserVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("chkInsertSuser"), suserVo);
		return resData;
	}

	// 운영자 등록
	public int insertSuser(SuserVo suserVo) throws Exception {
		return sqlSession.insert(NAME_SPACE.concat("insertSuser"), suserVo);
	}

	// 수정모달 이동
	public SuserVo selectSuserInfo(HashMap<String, Object> map) throws Exception {
		SuserVo resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectSuserInfo"), map);
		return resData;
	}

	// 운영자 수정
	public int updateSuser(SuserVo suserVo) throws Exception {
		return sqlSession.update(NAME_SPACE.concat("updateSuser"), suserVo);
	}

	// 운영자 삭제
	public int deleteSuser(SuserVo suserVo) throws Exception {
		return sqlSession.update(NAME_SPACE.concat("deleteSuser"), suserVo);
	}

}
