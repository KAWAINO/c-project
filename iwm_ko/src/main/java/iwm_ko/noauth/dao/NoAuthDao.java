package iwm_ko.noauth.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.noauth.model.NoAuthVo;

@Repository("iwm_ko.noauth.noAuthDao")
public class NoAuthDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.noauth.";

	public List<NoAuthVo> selectAuthList(NoAuthVo noAuthVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("selectAuthList"), noAuthVo);
	}

	// 페이징
	public int totalAuthList(NoAuthVo noAuthVo) throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("totalAuthList"), noAuthVo);
	}

	// chkMac
	public List<NoAuthVo> chkMac(NoAuthVo noAuthVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkMac"), noAuthVo);
	}

	// chkName
	public List<NoAuthVo> chkName(NoAuthVo noAuthVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkName"), noAuthVo);
	}

	// chkFlag
	public List<NoAuthVo> chkFlag(NoAuthVo noAuthVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkFlag"), noAuthVo);
	}

	// chgAuth
	public int chgAuth(NoAuthVo noAuthVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("chgAuth"), noAuthVo);
	}

	// insAuth
	public int insAuth(NoAuthVo noAuthVo) throws Exception {
		return this.sqlSession.insert(NAME_SPACE.concat("insAuth"), noAuthVo);
	}

	// 수정모달이동
	public NoAuthVo selectAuthInfo(HashMap<String, Object> map) throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("selectAuthInfo"), map);
	}

	// 수정
	public int updateAuth(NoAuthVo noAuthVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("updateAuth"), noAuthVo);
	}

	// 삭제
	public int deleteAuth(NoAuthVo noAuthVo) throws Exception {
		return this.sqlSession.delete(NAME_SPACE.concat("deleteAuth"), noAuthVo);
	}

	public List<String> updChkName(NoAuthVo noAuthVo) throws Exception{
		return this.sqlSession.selectList(NAME_SPACE.concat("updChkName"), noAuthVo);
	}
}
