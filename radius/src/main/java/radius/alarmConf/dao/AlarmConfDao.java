package radius.alarmConf.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import radius.alarmConf.model.AlarmConfVo;

@Repository("radius.alarmConf.alarmConfDao")
public class AlarmConfDao {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	private final String NAME_SPACE = "radius.alarmConf.";

	// AlarmConfList 조회
	public List<AlarmConfVo> selectAlarmConfList(AlarmConfVo alarmConfVo) throws Exception {
		List<AlarmConfVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectAlarmConfList"), alarmConfVo);
		return resData;
	}

	// 페이징
	public int selectAlarmConfCnt(AlarmConfVo alarmConfVo) throws Exception {
		int resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectAlarmConfCnt"), alarmConfVo);
		return resData;
	}
	
	// period & auth_flag 중복검사
	public List<AlarmConfVo> chkInsertValues(AlarmConfVo alarmConfVo) throws Exception {
		List<AlarmConfVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("chkInsertValues"), alarmConfVo);
		return resData;
	}

	// alarmConf 등록
	public int insertAlarmConf(AlarmConfVo alarmConfVo) throws Exception {
		int resData = this.sqlSession.insert(NAME_SPACE.concat("insertAlarmConf"), alarmConfVo);
		return resData;
	}

	// 상세페이지
	public AlarmConfVo selectAlarmConfInfo(HashMap<String, Object> map) throws Exception {
		AlarmConfVo resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectAlarmConfInfo"), map);
		return resData;
	}

	// 수정
	public int updateAlarmConf(AlarmConfVo alarmConfVo) throws Exception {
		int updateAlarmConf = this.sqlSession.update(NAME_SPACE.concat("updateAlarmConf"), alarmConfVo);
		return updateAlarmConf;
	}

	// 수정
	public int deleteAlarmConf(AlarmConfVo alarmConfVo) {
		int deleteAlarmConf = this.sqlSession.delete(NAME_SPACE.concat("deleteAlarmConf"), alarmConfVo);
		return deleteAlarmConf;
	}


}
