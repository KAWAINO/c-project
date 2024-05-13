package radius.alarmSys.service;

import java.util.List;

import radius.alarmSys.model.AlarmSysVo;

public interface AlarmSysService {

	// alarmIdList
	List<AlarmSysVo> selectAlarmIdList(AlarmSysVo alarmSysVo) throws Exception;

	// alarmSysList
	List<AlarmSysVo> selectAlarmSysList(AlarmSysVo alarmSysVo) throws Exception;

	// 페이징 카운트
	int selectAlarmSysCnt(AlarmSysVo alarmSysVo) throws Exception;
	
	List<AlarmSysVo> selectSevCntList(AlarmSysVo alarmSysVo) throws Exception;

	// insert alarmHistory
	int insertAlarmHistory(AlarmSysVo alarmSysVo) throws Exception;

	// deleteAlarmSysList
	int deleteAlarmSysList(AlarmSysVo alarmSysVo) throws Exception;



}
