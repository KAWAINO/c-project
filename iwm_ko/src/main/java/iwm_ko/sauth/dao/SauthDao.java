package iwm_ko.sauth.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.sauth.model.SauthVo;

@Repository("iwm_ko.sauth.sauthDao")
public class SauthDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.sauth.";

	// 선박명 조회
	public List<SauthVo> selectShipList(SauthVo sauthVo) throws Exception {
		List<SauthVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectShipList"), sauthVo);
		return resData;
	}

	// 리스트 조회
	public List<SauthVo> selectSearchList(SauthVo sauthVo) {
		List<SauthVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectSearchList"), sauthVo);
		return resData;
	}

	// 업데이트
	public int updateGrade(SauthVo sauthVo) {
		return this.sqlSession.update(NAME_SPACE.concat("updateGrade"), sauthVo);
	}
}
