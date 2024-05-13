package iwm_ko.suser.service;

import java.util.HashMap;
import java.util.List;

import iwm_ko.cuser.model.CuserVo;
import iwm_ko.suser.model.SuserVo;

public interface SuserService {
	
	// 등급 조회
	List<SuserVo> selectGradeList(SuserVo suserVo) throws Exception;

	// 유저 리스트 조회
	List<SuserVo> selectSuserList(SuserVo suserVo) throws Exception;

	// 페이징
	int totalSuserList(SuserVo suserVo) throws Exception;

	// 중복검사
	List<SuserVo> chkInsertSuser(SuserVo suserVo) throws Exception;

	// del_flag 체크
	List<SuserVo> chkDelFlag(SuserVo suserVo) throws Exception;

	// dle_flag가 Y이면 기존 유저 수정
	int changeUser(SuserVo suserVo) throws Exception;

	// 등록
	int insertSuser(SuserVo suserVo) throws Exception;

	// 수정모달
	SuserVo selectSuserInfo(HashMap<String, Object> map) throws Exception;

	// 수정
	int updateSuser(SuserVo suserVo) throws Exception;

	// 삭제
	int deleteSuser(SuserVo suserVo) throws Exception;



}
