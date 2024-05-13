package radius.suser.service;

import java.util.HashMap;
import java.util.List;

import radius.suser.model.SuserVo;

public interface SuserService {

	/**
	 * 운영자관리 리스트 조회
	 * @param suserVo
	 * @return
	 * @throws Exception
	 */
	List<SuserVo> selectSuserList(SuserVo suserVo) throws Exception;

	/**
	 * 페이징
	 * @param suserVo
	 * @return
	 * @throws Exception
	 */
	List<SuserVo> getTotalSuserList(SuserVo suserVo) throws Exception;

	/**
	 * 운영자 등록 중복검사
	 * @param suserVo
	 * @return
	 * @throws Exception
	 */
	List<SuserVo> chkInsertSuser(SuserVo suserVo) throws Exception;

	/**
	 * 운영자 등록
	 * @param suserVo
	 * @return
	 * @throws Exception
	 */
	int insertSuser(SuserVo suserVo) throws Exception;

	/**
	 * 수정모달 이동
	 * @param req
	 * @return
	 * @throws Exception
	 */
	SuserVo selectSuserInfo(HashMap<String, Object> map) throws Exception;

	/**
	 * 운영자 수정
	 * @param suserVo
	 * @return
	 * @throws Exception
	 */
	int updateSuser(SuserVo suserVo) throws Exception;

	/**
	 * 운영자 삭제
	 * @param suserVo
	 * @return
	 * @throws Exception
	 */
	int deleteSuser(SuserVo suserVo) throws Exception;

}
