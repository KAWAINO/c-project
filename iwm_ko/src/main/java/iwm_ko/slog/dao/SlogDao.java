package iwm_ko.slog.dao;

import java.util.List;
import java.util.Locale;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.slog.model.SlogVo;

@Repository("iwm_ko.slog.slogDao")
public class SlogDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.slog.";

	// 메뉴리스트 조회
	public List<SlogVo> getMenuList(SlogVo slogVo) throws Exception {
		List<SlogVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("getMenuList"), slogVo);
		return resData;
	}
	
	// 선박명 조회
	public List<SlogVo> selectShipList(SlogVo slogVo) throws Exception {
		List<SlogVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectShipList"), slogVo);
		return resData;
	}

	// 로그리스트 조회
	public List<SlogVo> selectSlogList(SlogVo slogVo, Locale locale) {
		List<SlogVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectSlogList"), slogVo);
		return resData;
	}

	// 페이징
	public int totalSlogList(SlogVo slogVo) {
		return this.sqlSession.selectOne(NAME_SPACE.concat("totalSlogList"), slogVo);
	}

}
