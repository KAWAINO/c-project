package apms.suser.service;

import java.util.HashMap;
import java.util.List;

import apms.suser.model.GradeVo;
import apms.suser.model.SuserVo;

public interface SuserService {
	/**
	 * 운영자 등급 목록 조회
	 * @param
	 * @return
	 * @throws Exception
	 */
	public List<GradeVo> selectUserGrade() throws Exception;
	
	/**
	 * 운영자 목록 조회
	 * @param suserVo
	 * @return
	 * @throws Exception
	 */
	public List<SuserVo> selectUserList(SuserVo suserVo) throws Exception;
	
	/**
	 * 운영자 등록 중복검사
	 * @param suserVo
	 * @return
	 * @throws Exception
	 */
	public List<SuserVo> chkInsertSuser(SuserVo suserVo) throws Exception;
	
	/**
	 * 운영자 등록
	 * @param suserVo
	 * @return
	 * @throws Exception
	 */
    public int insertSuser(SuserVo suserVo) throws Exception;
	
	/**
	 * 운영자 상세 정보
	 * @param HashMap<String, Object>
	 * @return
	 * @throws Exception
	 */
	public SuserVo selectSuserInfo(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 운영자 수정
	 * @param suserVo
	 * @return
	 * @throws Exception
	 */
    public int updateSuser(SuserVo suserVo) throws Exception;
	
	/**
	 * 운영자 삭제
	 * @param suserVo
	 * @return
	 * @throws Exception
	 */
    public int deleteSuser(SuserVo suserVo) throws Exception;
}
