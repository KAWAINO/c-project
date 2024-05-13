package iwm_ko.alarmHis.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.alarmHis.model.AlarmHisVo;

@Repository("iwm_ko.alarmHis.alarmHisDao")
public class AlarmHisDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.alarmHis.";

	// 알람원인 리스트 조회
	public List<AlarmHisVo> getAlarmCauseList(AlarmHisVo alarmHisVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("getAlarmCauseList"), alarmHisVo);
	}

	// 알람이력 리스트 조회
	public List<AlarmHisVo> getAlarmHisList(AlarmHisVo alarmHisVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("getAlarmHisList"), alarmHisVo);
	}

	// 페이징
	public int totalAlarmHisList(AlarmHisVo alarmHisVo) throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("totalAlarmHisList"), alarmHisVo);
	}

}
