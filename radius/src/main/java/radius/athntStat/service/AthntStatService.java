package radius.athntStat.service;

import java.util.List;

import radius.athntStat.model.AthntStatVo;

public interface AthntStatService {

	/**
	 * 테이블명 조회
	 * @param statVo
	 * @return
	 * @throws Exception
	 */
	String selectTableName(AthntStatVo athntStatVo) throws Exception;
	
	/**
	 * 
	 * @param startDate
	 * @return
	 * @throws Exception
	 */
	String selectStatWeek(String startDate) throws Exception;
	
	/**
	 * 인증 결과 조회
	 * @return
	 * @throws Exception
	 */
	List<AthntStatVo> selelctReplyList(AthntStatVo athntStatVo) throws Exception;

	/**
	 * 결과 원인 조회
	 * @return
	 * @throws Exception
	 */
	List<AthntStatVo> selectCauseList(AthntStatVo athntStatVo) throws Exception;

	/**
	 * statList 조회
	 * @param statVo
	 * @return
	 * @throws Exception
	 */
	List<AthntStatVo> selectStatList(AthntStatVo athntStatVo) throws Exception;

	/**
	 * 
	 * @param statVo
	 * @return
	 * @throws Exception
	 */
	int selectStatCnt(AthntStatVo athntStatVo) throws Exception;



	

}
