package radius.athntHistory.service;

import java.util.List;

import radius.athntHistory.model.AthntHistoryVo;
import radius.athntHistory.model.CauseInfoVo;

public interface AthntHistoryService {

	/**
	 * 인증 결과 조회
	 * @param athntHistoryVo
	 * @return
	 * @throws Exception
	 */
	public List<AthntHistoryVo> selelctReplyList()throws Exception;

	/**
	 * 결과 원인 조회
	 * @param athntHistoryVo
	 * @return
	 * @throws Exception
	 */
	public List<AthntHistoryVo> selectCauseList() throws Exception;

	/**
	 * 인증 이력 리스트 조회
	 * @param athntHistoryVo
	 * @return
	 * @throws Exception
	 */
	public List<AthntHistoryVo> selectAthntHistoryList(AthntHistoryVo athntHistoryVo) throws Exception;

	/**
	 * 
	 * @param athntHistoryVo
	 * @return
	 * @throws Exception
	 */
	public int selectAccessCnt(AthntHistoryVo athntHistoryVo) throws Exception;
	
}
