package iwm_ko.login.dao;

import iwm_ko.login.model.LoginVo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("iwm_ko.login.loginDao")
public class LoginDao {
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.login.";
	
	public LoginVo selectAuthorizedUser(LoginVo loginVo) throws Exception{
		LoginVo resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectAuthorizedUser"), loginVo);
		return resData;
	}

    public List<LoginVo> getUserPassLimit( ) throws Exception{ 
        return this.sqlSession.selectList(NAME_SPACE.concat("getUserPassLimit")); 
    } 

    public String getUserPassChangeDate(String user_id) { 
        return this.sqlSession.selectOne(NAME_SPACE.concat("getUserPassChangeDate"),user_id); 
    } 


	public List<LoginVo> checkId(LoginVo loginVo) {
		List<LoginVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("checkId"),loginVo);
		return resList;
	}

	public int getLockCount() {
		return this.sqlSession.selectOne(NAME_SPACE.concat("getLockCount"));
	}

	public int updateFailCount(String user_id) {
		return this.sqlSession.update(NAME_SPACE.concat("updateFailCount"),user_id);
	}

	public int getFailCount(String user_id) {
		return this.sqlSession.selectOne(NAME_SPACE.concat("getFailCount"),user_id);

	}

	public int insertFailCount(String user_id) {
		return this.sqlSession.insert(NAME_SPACE.concat("insertFailCount"),user_id);
	}

	public Boolean checkMenu(LoginVo loginVo) throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("checkMenu"), loginVo);
	}
}