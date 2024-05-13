package iwm_ko.enableTime.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.enableTime.model.EnableTimeVo;

@Repository("iwm_ko.enableTime.enableTimeDao")
public class EnableTimeDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.enableTime.";

	public List<EnableTimeVo> selectEnableTimeList(EnableTimeVo enableTimeVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("selectEnableTimeList"), enableTimeVo);
	}

	// FROM to TO time check
	public List<EnableTimeVo> chkTime(EnableTimeVo enableTimeVo) {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkTime"), enableTimeVo);
	}

	// 추가
	public int insertTime(EnableTimeVo enableTimeVo) throws Exception {
		return this.sqlSession.insert(NAME_SPACE.concat("insertTime"), enableTimeVo);
	}

	// 페이징
	public int totalEnableTimeList(EnableTimeVo enableTimeVo) throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("totalEnableTimeList"), enableTimeVo);
	}

	// 수정모달이동
	public EnableTimeVo enableTimeInfo(HashMap<String, Object> req) {
		return this.sqlSession.selectOne(NAME_SPACE.concat("enableTimeInfo"), req);
	}

	// 삭제
	public int deleteEnableTime(EnableTimeVo enableTimeVo) throws Exception {
		return this.sqlSession.delete(NAME_SPACE.concat("deleteEnableTime"), enableTimeVo);
	}
}
