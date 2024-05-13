package iwm_ko.cuser.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.cuser.model.CuserVo;

@Repository("iwm_ko.cuser.cuserDao")
public class CuserDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.cuser.";
	
	// grade 리스트 조회
	public List<CuserVo> selectGradeList(CuserVo cuserVo) throws Exception {
		List<CuserVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectGradeList"), cuserVo);
		return resData;
	}
	
	// comp 리스트 조회
	public List<CuserVo> selectCompList(CuserVo cuserVo) throws Exception  {
		List<CuserVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectCompList"), cuserVo);
		return resData;
	}

	// cuser 리스트 조회
	public List<CuserVo> selectCuserList(CuserVo cuserVo) throws Exception  {
		List<CuserVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectCuserList"), cuserVo);
		return resData;
	}

	// paging 위한 리스트 조회
	public int totalCuserList(CuserVo cuserVo) throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("totalCuserList"), cuserVo);
	}

	// 운영자등록 중복검사
	public List<CuserVo> chkInsertCuser(CuserVo cuserVo) throws Exception {
		List<CuserVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("chkInsertCuser"), cuserVo);
		return resData;
	}
	
	// del_flag 체크
	public List<CuserVo> chkDelFlag(CuserVo cuserVo) {
		List<CuserVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("chkDelFlag"), cuserVo);
		return resData;
	}

	// iwm 운영자 등록
	public int insertCuser(CuserVo cuserVo) throws Exception {
		return this.sqlSession.insert(NAME_SPACE.concat("insertCuser"), cuserVo);
	}
	
	// 기존 유저 수정
	public int changeUser(CuserVo cuserVo) throws Exception{
		return this.sqlSession.update(NAME_SPACE.concat("changeUser"), cuserVo);
	}
	
	// Lock 추가
	public int insertFailCnt(CuserVo cuserVo) {
		return this.sqlSession.insert(NAME_SPACE.concat("insertFailCnt"), cuserVo);
	}

	// 수정모달 이동
	public CuserVo selectCuserInfo(HashMap<String, Object> map) throws Exception {
		CuserVo resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectCuserInfo"),map );
		return resData;
	}

	// iwm 운영자 수정
	public int updateCuser(CuserVo cuserVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("updateCuser"), cuserVo);
	}

	// iwm 운영자 삭제
	public int deleteCuser(CuserVo cuserVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("deleteCuser"), cuserVo);
	}
	
	// TB_USER_INFO_CENTER_LOCK 정보 삭제
	public int deleteLock(CuserVo cuserVo) throws Exception {
		return this.sqlSession.delete(NAME_SPACE.concat("deleteLock"), cuserVo);
	}

	// 로그인 실패 횟수 조회
	public String getLockCnt() throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("getLockCnt"));
	}

	// 계정 잠금 해제
	public int unLockUser(CuserVo cuserVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("unLockUser"), cuserVo);
	}
}
