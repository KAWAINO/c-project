package iwm_ko.suser.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.suser.model.SuserVo;

@Repository("iwm_ko.suser.suserDao")
public class SuserDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.suser.";
	
	// 등급 조회
	public List<SuserVo> selectGradeList(SuserVo suserVo) throws Exception{
		List<SuserVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectGradeList"), suserVo);
		return resData;
	}

	// 유저 리스트 조회
	public List<SuserVo> selectSuserList(SuserVo suserVo) throws Exception {
		List<SuserVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectSuserList"), suserVo);
		return resData;
	}

	// 페이징
	public int totalSuserList(SuserVo suserVo) throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("totalSuserList"), suserVo);
	}

	// 중복 검사
	public List<SuserVo> chkInsertSuser(SuserVo suserVo) throws Exception {
		List<SuserVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("chkInsertSuser"), suserVo);
		return resData;
	}

	// del_flag 체크
	public List<SuserVo> chkDelFlag(SuserVo suserVo) throws Exception {
		List<SuserVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("chkDelFlag"), suserVo);
		return resData;
	}

	// dle_flag가 Y이면 기존 유저 수정
	public int changeUser(SuserVo suserVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("changeUser"), suserVo);
	}

	// 등록
	public int insertSuser(SuserVo suserVo) throws Exception {
		return this.sqlSession.insert(NAME_SPACE.concat("insertSuser"), suserVo);
	}

	// 수정모달
	public SuserVo selectSuserInfo(HashMap<String, Object> map) throws Exception {
		SuserVo reData = this.sqlSession.selectOne(NAME_SPACE.concat("selectSuserInfo"), map);
		return reData;
	}

	// 수정
	public int updateSuser(SuserVo suserVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("updateSuser"), suserVo);
	}

	// 삭제
	public int deleteSuser(SuserVo suserVo) {
		return this.sqlSession.delete(NAME_SPACE.concat("deleteSuser"), suserVo);
	}


}
