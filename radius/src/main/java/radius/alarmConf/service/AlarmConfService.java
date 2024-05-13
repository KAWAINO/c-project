package radius.alarmConf.service;

import java.util.HashMap;
import java.util.List;

import radius.alarmConf.model.AlarmConfVo;

public interface AlarmConfService {

	/**
	 * AlarmConfList 조회
	 * @param alarmConfVo
	 * @return
	 * @throws Exception
	 */
	List<AlarmConfVo> selectAlarmConfList(AlarmConfVo alarmConfVo) throws Exception;

	/**
	 * 페이징
	 * @param alarmConfVo
	 * @return
	 * @throws Exception
	 */
	int selectAlarmConfCnt(AlarmConfVo alarmConfVo) throws Exception;
	
	/**
	 * period & auth_flag 중복검사
	 * @param alarmConfVo
	 * @return
	 * @throws Exception
	 */
	List<AlarmConfVo> chkInsertValues(AlarmConfVo alarmConfVo) throws Exception;

	/**
	 * alarmConf 등록
	 * @param alarmConfVo
	 * @return
	 * @throws Exception
	 */
	int insertAlarmConf(AlarmConfVo alarmConfVo) throws Exception;

	/**
	 * 상세페이지
	 * @param req
	 * @return
	 * @throws Exception
	 */
	AlarmConfVo selectAlarmConfInfo(HashMap<String, Object> map) throws Exception;

	/**
	 * 수정
	 * @param alarmConfVo
	 * @return
	 * @throws Exception
	 */
	int updateAlarmConf(AlarmConfVo alarmConfVo) throws Exception;

	/**
	 * 삭제
	 * @param alarmConfVo
	 * @return
	 * @throws Exception
	 */
	int deleteAlarmConf(AlarmConfVo alarmConfVo) throws Exception;



}
