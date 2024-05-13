package iwm_ko.crewdata.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.crewdata.model.CrewDataVo;

@Repository("iwm_ko.crewdata.crewDataDao")
public class CrewDataDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.crewData.";

	// 로그리스트 조회
	public List<CrewDataVo> selectLogList(CrewDataVo crewDataVo) {
		List<CrewDataVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectLogList"), crewDataVo);
		return resData;
	}

	// 페이징
	public int totalCrewList(CrewDataVo crewDataVo) {
		return this.sqlSession.selectOne(NAME_SPACE.concat("totalCrewList"), crewDataVo);
	}
}
