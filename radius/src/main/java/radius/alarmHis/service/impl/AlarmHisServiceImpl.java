package radius.alarmHis.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import radius.alarmHis.dao.AlarmHisDao;
import radius.alarmHis.model.AlarmHisVo;
import radius.alarmHis.service.AlarmHisService;

@Service("radius.alarmHis.alarmHisService")
//@MapperScan("radius.alarmHis.dao")
public class AlarmHisServiceImpl implements AlarmHisService{
	
	@Resource(name = "radius.alarmHis.alarmHisDao")
	private AlarmHisDao alarmHisDao;
	
	// alarmIdList
	@Override
	public List<AlarmHisVo> selectAlarmIdList(AlarmHisVo alarmHisVo) throws Exception {
		List<AlarmHisVo> resList = this.alarmHisDao.selectAlarmIdList(alarmHisVo);
		return resList;
	}

	// alarmHisList
	@Override
	public List<AlarmHisVo> selectAlarmHisList(AlarmHisVo alarmHisVo) throws Exception {
		List<AlarmHisVo> resList = this.alarmHisDao.selectAlarmHisList(alarmHisVo);
		return resList;
	}

}
