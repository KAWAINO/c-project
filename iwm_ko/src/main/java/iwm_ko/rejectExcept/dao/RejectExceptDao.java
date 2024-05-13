package iwm_ko.rejectExcept.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.cuser.model.CuserVo;
import iwm_ko.rejectExcept.model.RejectExceptVo;

@Repository("iwm_ko.rejectExcept.rejectExceptDao")
public class RejectExceptDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.rejectExcept.";

	public List<RejectExceptVo> selectRejectExcepList(RejectExceptVo rejectExceptVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("selectRejectExceptList"), rejectExceptVo);
	}

	// 페이징
	public int totalRejectExceptList(RejectExceptVo rejectExceptVo) throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("totalRejectExceptList"), rejectExceptVo);
	}

	// chkMac
	public List<CuserVo> chkMac(RejectExceptVo rejectExceptVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkMac"), rejectExceptVo);
	}

	// 등록	
	public int insertAuth(RejectExceptVo rejectExceptVo) throws Exception {
		return this.sqlSession.insert(NAME_SPACE.concat("insertAuth"), rejectExceptVo);
	}

	// 수정모달
	public RejectExceptVo selectRejectExceptInfo(HashMap<String, Object> map) throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("selectRejectExceptInfo"), map);
	}

	// 수정
	public int updateAuth(RejectExceptVo rejectExceptVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("updateAuth"), rejectExceptVo);
	}

	// 삭제
	public int deleteAuth(RejectExceptVo rejectExceptVo) throws Exception {
		return this.sqlSession.delete(NAME_SPACE.concat("deleteAuth"), rejectExceptVo);
	}
}
