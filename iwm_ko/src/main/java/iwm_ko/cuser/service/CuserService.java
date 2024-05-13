package iwm_ko.cuser.service;

import java.util.HashMap;
import java.util.List;

import iwm_ko.cuser.model.CuserVo;

public interface CuserService {
	
	/**
	 * grade 리스트 조회
	 * @param cuserVo
	 * @return
	 * @throws Exception
	 */
	List<CuserVo> selectGradeList(CuserVo cuserVo) throws Exception;
	
	/**
	 * comp 리스트 조회
	 * @param cuserVo
	 * @return
	 * @throws Exception
	 */
	List<CuserVo> selectCompList(CuserVo cuserVo) throws Exception;

	/**
	 * cuser 리스트 조회
	 * @param cuserVo
	 * @return
	 * @throws Exception
	 */
	List<CuserVo> selectCuserList(CuserVo cuserVo) throws Exception;

	/**
	 * paging 위한 total 리스트 조회
	 * @param cuserVo
	 * @return
	 * @throws Exception
	 */
	int totalCuserList(CuserVo cuserVo) throws Exception;

	/**
	 * 운영자 등록 중복체크
	 * @param cuserVo
	 * @return
	 * @throws Exception
	 */
	List<CuserVo> chkInsertCuser(CuserVo cuserVo) throws Exception;
	
	/**
	 * del_flag 체크
	 * @param cuserVo
	 * @return
	 * @throws Exception
	 */
	List<CuserVo> chkDelFlag(CuserVo cuserVo) throws Exception;
	
	/**
	 * 기존 유저 수정
	 * @param cuserVo
	 * @return
	 * @throws Exception
	 */
	int changeUser(CuserVo cuserVo) throws Exception;

	/**
	 * iwm 운영자 등록 
	 * @param cuserVo
	 * @return
	 * @throws Exception
	 */
	int insertCuser(CuserVo cuserVo) throws Exception;
	
	/**
	 * Lock 추가
	 * @param cuserVo
	 * @return
	 * @throws Exception
	 */
	int insertFailCnt(CuserVo cuserVo) throws Exception;

	/**
	 * 수정 modal 이동
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CuserVo selectCuserInfo(HashMap<String, Object> map) throws Exception;

	/**
	 * iwm 운영자 수정
	 * @param cuserVo
	 * @return
	 * @throws Exception
	 */
	int updateCuser(CuserVo cuserVo) throws Exception;

	/**
	 * iwm 운영자 삭제
	 * @param cuserVo
	 * @return
	 * @throws Exception
	 */
	int deleteCuser(CuserVo cuserVo) throws Exception;
	
	/**
	 * TB_USER_INFO_CENTER_LOCK 정보 삭제
	 * @param cuserVo
	 * @return
	 * @throws Exception
	 */
	int deleteLock(CuserVo cuserVo) throws Exception;

	/**
	 * 로그인 실패 횟수 조회
	 * @return
	 * @throws Exception
	 */
	String getLockCnt() throws Exception;

	/**
	 * 계정 잠금 해제
	 * @param cuserVo
	 * @return
	 * @throws Exception
	 */
	int unLockUser(CuserVo cuserVo) throws Exception;
}
