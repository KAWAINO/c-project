package radius.alarmHis.service;

import java.util.List;

import radius.alarmHis.model.AlarmHisVo;

public interface AlarmHisService {
	
	// alarmIdList
	List<AlarmHisVo> selectAlarmIdList(AlarmHisVo alarmHisVo) throws Exception;

	// alarmHisList
	List<AlarmHisVo> selectAlarmHisList(AlarmHisVo alarmHisVo) throws Exception;

}
