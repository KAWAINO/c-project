package iwm_ko.alarmmon.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import iwm_ko.alarmmon.dao.AlarmMonDao;
import iwm_ko.alarmmon.model.AlarmMonVo;
import iwm_ko.alarmmon.service.AlarmMonService;
import jakarta.annotation.Resource;

@Service("iwm_ko.alarmmon.alarmMonService")
public class AlarmMonServiceImpl implements AlarmMonService {

	@Resource(name="iwm_ko.alarmmon.alarmMonDao")
    private AlarmMonDao alarmMonDao;

	@Override
	public List<AlarmMonVo> getAlarmSysList(AlarmMonVo alarmMonVo) throws Exception {
		return this.alarmMonDao.getAlarmSysList(alarmMonVo);
	}
	
	@Override
	public List<AlarmMonVo> getAlarmHisList(AlarmMonVo alarmMonVo) throws Exception {
		return this.alarmMonDao.getAlarmHisList(alarmMonVo);
	}
	
	@Override
	public List<AlarmMonVo> getAlarmActiveList(AlarmMonVo alarmMonVo) throws Exception {
		return this.alarmMonDao.getAlarmActiveList(alarmMonVo);
	}

	// delSound
	@Override
	public int delSound(AlarmMonVo alarmMonVo) throws Exception {
		return this.alarmMonDao.delSound(alarmMonVo);
	}

	// chkAlarm
	@Override
	public List<AlarmMonVo> chkAlarm(AlarmMonVo alarmMonVo) throws Exception {
		return this.alarmMonDao.chkAlarm(alarmMonVo);
	}

	// updateAlarm
	@Override
	public int updateAlarm(AlarmMonVo alarmMonVo) throws Exception {
		return this.alarmMonDao.updateAlarm(alarmMonVo);
	}

	// insertAlarm
	@Override
	public int insertAlarm(AlarmMonVo alarmMonVo) throws Exception {
		return this.alarmMonDao.insertAlarm(alarmMonVo);
	}

	// deleteAlarm
	@Override
	public int deleteAlarm(AlarmMonVo alarmMonVo) throws Exception {
		return this.alarmMonDao.deleteAlarm(alarmMonVo);
	}

	// getSevList
	@Override
	public List<AlarmMonVo> getSevList(AlarmMonVo alarmMonVo) throws Exception {
		return this.alarmMonDao.getSevList(alarmMonVo);
	}

	// getSevSound
	@Override
	public List<AlarmMonVo> getSevSound(AlarmMonVo alarmMonVo) throws Exception {
		return this.alarmMonDao.getSevSound(alarmMonVo);
	}
	
	
}
