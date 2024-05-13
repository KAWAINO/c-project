package radius.alarmSys.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import radius.alarmSys.model.AlarmSysVo;

@Repository("radius.alarmSys.alarmSysDao")
public class AlarmSysDao {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	private final String NAME_SPACE = "radius.alarmSys.";

	// alarmIdList
	public List<AlarmSysVo> selectAlarmIdList(AlarmSysVo alarmSysVo) throws Exception {
		List<AlarmSysVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectAlarmIdList"), alarmSysVo);
		return resData;
	}

	// alarmSysList
	public List<AlarmSysVo> selectAlarmSysList(AlarmSysVo alarmSysVo) throws Exception {
		List<AlarmSysVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectAlarmSysList"), alarmSysVo);
		return resData;
	}

	// 페이징 카운트
	public int selectAlarmSysCnt(AlarmSysVo alarmSysVo) throws Exception {
		int resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectAlarmSysCnt"), alarmSysVo);
		return resData;
	}

	public List<AlarmSysVo> selectSevCntList(AlarmSysVo alarmSysVo) throws Exception {
		List<AlarmSysVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectSevCntList"), alarmSysVo);
		return resData;
	}

	public int insertAlarmHistory(AlarmSysVo alarmSysVo) throws Exception{
		int resData = this.sqlSession.insert(NAME_SPACE.concat("insertAlarmHistory"), alarmSysVo);
		return resData;
	}

	// deleteAlarmSysList
	public int deleteAlarmSysList(AlarmSysVo alarmSysVo) throws Exception{
		int resData = this.sqlSession.delete(NAME_SPACE.concat("deleteAlarmSysList"), alarmSysVo);
		return resData;
	}


}
