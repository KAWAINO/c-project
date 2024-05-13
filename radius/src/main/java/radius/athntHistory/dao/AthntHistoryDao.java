package radius.athntHistory.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import radius.athntHistory.model.AthntHistoryVo;

@Repository("radius.athntHistory.athntHistoryDao")
public class AthntHistoryDao {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "radius.athntHistory.";
	
	// 인증 결과 조회
	public List<AthntHistoryVo> selelctReplyList() throws Exception {
		List<AthntHistoryVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selelctReplyList"));
		return resData;
	}

	// 결과 원인 조회
	public List<AthntHistoryVo> selectCauseList() throws Exception {
		List<AthntHistoryVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectCauseList"));
		return resData;
	}

	// 인증 이력 리스트 조회
	public List<AthntHistoryVo> selectAthntHistoryList(AthntHistoryVo athntHistoryVo) throws Exception {
		List<AthntHistoryVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectAthntHistoryList"), athntHistoryVo);
		return resData;
	}

	public int selectAccessCnt(AthntHistoryVo athntHistoryVo) {
		int resCnt = this.sqlSession.selectOne(NAME_SPACE.concat("selectAccessCnt"), athntHistoryVo);
		return resCnt;
	}

}
