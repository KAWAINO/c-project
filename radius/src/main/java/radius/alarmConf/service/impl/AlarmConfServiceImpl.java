package radius.alarmConf.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import radius.alarmConf.dao.AlarmConfDao;
import radius.alarmConf.model.AlarmConfVo;
import radius.alarmConf.service.AlarmConfService;

@Service("radius.alarmConf.alarmConfService")
//@MapperScan("radius.alarmConf.dao")
public class AlarmConfServiceImpl implements AlarmConfService{

	@Resource(name = "radius.alarmConf.alarmConfDao")
	private AlarmConfDao alarmConfDao;

	// AlarmConfList 조회
	@Override
	public List<AlarmConfVo> selectAlarmConfList(AlarmConfVo alarmConfVo) throws Exception {
		List<AlarmConfVo> resList = this.alarmConfDao.selectAlarmConfList(alarmConfVo);
		return resList;
	}

	// 페이징
	@Override
	public int selectAlarmConfCnt(AlarmConfVo alarmConfVo) throws Exception {
		int alarmConfCnt = this.alarmConfDao.selectAlarmConfCnt(alarmConfVo);
		return alarmConfCnt;
	}
	
	// period & auth_flag 중복검사
	@Override
	public List<AlarmConfVo> chkInsertValues(AlarmConfVo alarmConfVo) throws Exception {
		List<AlarmConfVo> resList = this.alarmConfDao.chkInsertValues(alarmConfVo);
		return resList;
	}

	// alarmConf등록
	@Override
	public int insertAlarmConf(AlarmConfVo alarmConfVo) throws Exception {
		int insAlarmConf = this.alarmConfDao.insertAlarmConf(alarmConfVo);
		return insAlarmConf;
	}

	// 상세페이지
	@Override
	public AlarmConfVo selectAlarmConfInfo(HashMap<String, Object> map) throws Exception {
		AlarmConfVo modal = this.alarmConfDao.selectAlarmConfInfo(map);
		return modal;
	}

	// 수정
	@Override
	public int updateAlarmConf(AlarmConfVo alarmConfVo) throws Exception {
		int updateAlarmConf = this.alarmConfDao.updateAlarmConf(alarmConfVo);
		return updateAlarmConf;
	}

	// 삭제
	@Override
	public int deleteAlarmConf(AlarmConfVo alarmConfVo) throws Exception {
		int deleteAlarmConf = this.alarmConfDao.deleteAlarmConf(alarmConfVo);
		return deleteAlarmConf;
	}


}
