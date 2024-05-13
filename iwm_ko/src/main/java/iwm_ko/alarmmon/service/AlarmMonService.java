package iwm_ko.alarmmon.service;

import java.util.List;

import iwm_ko.alarmmon.model.AlarmMonVo;

public interface AlarmMonService {

	// 
	List<AlarmMonVo> getAlarmSysList(AlarmMonVo alarmMonVo) throws Exception;
	
	// alarmHisList
	List<AlarmMonVo> getAlarmHisList(AlarmMonVo alarmMonVo) throws Exception;
	
	List<AlarmMonVo> getAlarmActiveList(AlarmMonVo alarmMonVo) throws Exception;

	// delSound
	int delSound(AlarmMonVo alarmMonVo) throws Exception;

	// chkAlarm
	List<AlarmMonVo> chkAlarm(AlarmMonVo alarmMonVo) throws Exception;	
	
	// updateAlarm
	int updateAlarm(AlarmMonVo alarmMonVo) throws Exception;

	// insertAlarm
	int insertAlarm(AlarmMonVo alarmMonVo) throws Exception;

	// deleteAlarm
	int deleteAlarm(AlarmMonVo alarmMonVo) throws Exception;

	List<AlarmMonVo> getSevList(AlarmMonVo alarmMonVo) throws Exception;

	List<AlarmMonVo> getSevSound(AlarmMonVo alarmMonVo) throws Exception;





	
}
