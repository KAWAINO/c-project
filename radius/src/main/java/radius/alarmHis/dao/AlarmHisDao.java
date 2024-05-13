package radius.alarmHis.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import radius.alarmHis.model.AlarmHisVo;

@Repository("radius.alarmHis.alarmHisDao")
public class AlarmHisDao {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	private final String NAME_SPACE = "radius.alarmHis.";
	
	// alarmIdList
	public List<AlarmHisVo> selectAlarmIdList(AlarmHisVo alarmHisVo) throws Exception {
		List<AlarmHisVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectAlarmIdList"), alarmHisVo);
		return resData;
	}

	// alarmHisList
	public List<AlarmHisVo> selectAlarmHisList(AlarmHisVo alarmHisVo) throws Exception {
		List<AlarmHisVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectAlarmHisList"), alarmHisVo);
		return resData;
	}
}
