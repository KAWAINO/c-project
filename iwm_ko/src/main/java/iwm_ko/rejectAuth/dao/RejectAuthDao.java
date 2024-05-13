package iwm_ko.rejectAuth.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.rejectAuth.model.RejectAuthVo;

@Repository("iwm_ko.rejectAuth.rejectAuthDao")
public class RejectAuthDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.rejectAuth.";

	public List<RejectAuthVo> selectRejectAuthList(RejectAuthVo rejectAuthVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("selectRejectAuthList"), rejectAuthVo);
	}

	// 페이징
	public int totalRejectAuthList(RejectAuthVo rejectAuthVo) throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("totalRejectAuthList"), rejectAuthVo);
	}

	// chkMac
	public List<RejectAuthVo> chkMac(RejectAuthVo rejectAuthVo) throws Exception{
		return this.sqlSession.selectList(NAME_SPACE.concat("chkMac"), rejectAuthVo);
	}

	// chgAuth
	public int insertAuth(RejectAuthVo rejectAuthVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("insertAuth"), rejectAuthVo);
	}

	// 수정모달
	public RejectAuthVo selectRejectAuthInfo(HashMap<String, Object> map) throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("selectRejectAuthInfo"), map);
	}

	// 수정
	public int updateRejectAuth(RejectAuthVo rejectAuthVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("updateRejectAuth"), rejectAuthVo);
	}

	// 삭제
	public int deleteRejectAuth(RejectAuthVo rejectAuthVo) throws Exception{
		return this.sqlSession.delete(NAME_SPACE.concat("deleteRejectAuth"), rejectAuthVo);
	}
	
}
