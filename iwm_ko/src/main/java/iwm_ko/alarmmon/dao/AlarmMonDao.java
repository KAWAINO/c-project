package iwm_ko.alarmmon.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.alarmmon.model.AlarmMonVo;

//@Mapper
@Repository("iwm_ko.alarmmon.alarmMonDao")
public class AlarmMonDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.alarmmon.";

	public List<AlarmMonVo> getAlarmSysList(AlarmMonVo alarmMonVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("getAlarmSysList"), alarmMonVo);
	}
	
	public List<AlarmMonVo> getAlarmHisList(AlarmMonVo alarmMonVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("getAlarmHisList"), alarmMonVo);
	}
	
	public List<AlarmMonVo> getAlarmActiveList(AlarmMonVo alarmMonVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("getAlarmActiveList"), alarmMonVo);
	}

	// delSound
	public int delSound(AlarmMonVo alarmMonVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("delSound"), alarmMonVo);
	}

	// chkAlarm
	public List<AlarmMonVo> chkAlarm(AlarmMonVo alarmMonVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkAlarm"), alarmMonVo);
	}

	// updateAlarm
	public int updateAlarm(AlarmMonVo alarmMonVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("updateAlarm"), alarmMonVo);
	}

	// insertAlarm
	public int insertAlarm(AlarmMonVo alarmMonVo) throws Exception {
		return this.sqlSession.insert(NAME_SPACE.concat("insertAlarm"), alarmMonVo);
	}

	// deleteAlarm
	public int deleteAlarm(AlarmMonVo alarmMonVo) throws Exception {
		return this.sqlSession.delete(NAME_SPACE.concat("deleteAlarm"), alarmMonVo);
	}

	// getSevList
	public List<AlarmMonVo> getSevList(AlarmMonVo alarmMonVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("getSevList"), alarmMonVo);
	}

	public List<AlarmMonVo> getSevSound(AlarmMonVo alarmMonVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("getSevSound"), alarmMonVo);
	}




}
