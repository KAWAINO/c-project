package radius.login.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import radius.login.model.LoginVo;

@Repository("radius.login.loginDao")
public class LoginDao {
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "radius.login.";
	
	public LoginVo selectAuthorizedUser(LoginVo loginVo) throws Exception{
		LoginVo resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectAuthorizedUser"), loginVo);
		return resData;
	}
}