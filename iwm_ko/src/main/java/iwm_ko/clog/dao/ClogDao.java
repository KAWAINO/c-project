package iwm_ko.clog.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.clog.model.ClogVo;

@Repository("iwm_ko.clog.clogDao")
public class ClogDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.clog.";

	// menuList 조회
	public List<ClogVo> getMenuList(ClogVo clogVo) throws Exception {
		List<ClogVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("getMenuList"), clogVo);
		return resData;
	}

	// logList 조회
	public List<ClogVo> selectClogList(ClogVo clogVo) {
		List<ClogVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectClogList"), clogVo);
		return resData;
	}

	// 페이징
	public int totalClogList(ClogVo clogVo) {
		int resData =  this.sqlSession.selectOne(NAME_SPACE.concat("totalClogList"), clogVo);
		return resData;
	}
}
