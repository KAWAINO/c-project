package radius.alarmSys.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import radius.alarmSys.dao.AlarmSysDao;
import radius.alarmSys.model.AlarmSysVo;
import radius.alarmSys.service.AlarmSysService;

@Service("radius.alarmSys.alarmSysService")
//@MapperScan("radius.alarmSys.dao")
public class AlarmSysServiceImpl implements AlarmSysService{

	@Resource(name = "radius.alarmSys.alarmSysDao")
	private AlarmSysDao alarmSysDao;

	// alarmIdList
	@Override
	public List<AlarmSysVo> selectAlarmIdList(AlarmSysVo alarmSysVo) throws Exception {
		List<AlarmSysVo> resList = this.alarmSysDao.selectAlarmIdList(alarmSysVo);
		return resList;
	}

	// alarmSysList
	@Override
	public List<AlarmSysVo> selectAlarmSysList(AlarmSysVo alarmSysVo) throws Exception {
		List<AlarmSysVo> resList = this.alarmSysDao.selectAlarmSysList(alarmSysVo);
		return resList;
	}

	// 페이징 카운트
	@Override
	public int selectAlarmSysCnt(AlarmSysVo alarmSysVo) throws Exception {
		int alarmSysCnt = this.alarmSysDao.selectAlarmSysCnt(alarmSysVo);
		return alarmSysCnt;
	}

	@Override
	public List<AlarmSysVo> selectSevCntList(AlarmSysVo alarmSysVo) throws Exception {
		List<AlarmSysVo> resList = this.alarmSysDao.selectSevCntList(alarmSysVo);
		return resList;
	}

	// insert alarmHistory
	@Override
	public int insertAlarmHistory(AlarmSysVo alarmSysVo) throws Exception {
		int insAlarmHis = this.alarmSysDao.insertAlarmHistory(alarmSysVo);
		return insAlarmHis;
	}
	
	// deleteAlarmSysList
	@Override
	public int deleteAlarmSysList(AlarmSysVo alarmSysVo) throws Exception {
		int delAlarm = this.alarmSysDao.deleteAlarmSysList(alarmSysVo);
		return delAlarm;
	}

}
