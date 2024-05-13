package radius.radacctInfo.service;

import java.util.List;

import radius.radacctInfo.model.RadacctInfoVo;

public interface RadacctInfoService {

	/**
	 * Account Status 조회
	 * @param radacctInfoVo
	 * @return
	 * @throws Exception
	 */
	List<RadacctInfoVo> selectAcctStatusList(RadacctInfoVo radacctInfoVo) throws Exception;
	
	/**
	 * 리스트 조회
	 * @param radacctInfoVo
	 * @return
	 * @throws Exception
	 */
	List<RadacctInfoVo> selectRadacctInfoList(RadacctInfoVo radacctInfoVo) throws Exception;

	/**
	 * 페이징 카운트
	 * @param radacctInfoVo
	 * @return
	 * @throws Exception
	 */
	int selectRadacctInfoListCnt(RadacctInfoVo radacctInfoVo) throws Exception;



}
