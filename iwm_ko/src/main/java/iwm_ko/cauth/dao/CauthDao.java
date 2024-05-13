package iwm_ko.cauth.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.cauth.model.CauthVo;

@Repository("iwm_ko.cauth.cauthDao")
public class CauthDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.cauth.";

	// 등급별 리스트 조회
	public List<CauthVo> selectSearchList(CauthVo cauthVo) throws Exception {
		List<CauthVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectSearchList"), cauthVo);
		return resData;
	}

	// 리스트 카운트
	public List<CauthVo> listCnt(CauthVo cauthVo) throws Exception {
		List<CauthVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("listCnt"), cauthVo);
		return resData;
	}
	
	// 등급별 권한 변경
	public int updateGrade(CauthVo cauthVo) throws Exception {
		return sqlSession.update(NAME_SPACE.concat("updateGrade"), cauthVo);
	}
}
