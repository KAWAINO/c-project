package iwm_ko.crewauth.dao;

import java.util.List;
import java.util.Locale;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.crewauth.model.CrewAuthVo;

@Repository("iwm_ko.crewauth.crewAuthDao")
public class CrewAuthDao {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.crewauth.";

	// 페이지로딩리스트
	public List<CrewAuthVo> selectCrewAuthList(CrewAuthVo crewAuthVo, Locale locale) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("selectCrewAuthList"), crewAuthVo);
	}

	// 페이징
	public int totalCrewAuthList(CrewAuthVo crewAuthVo) {
		return this.sqlSession.selectOne(NAME_SPACE.concat("totalCrewAuthList"), crewAuthVo);
	}

}
