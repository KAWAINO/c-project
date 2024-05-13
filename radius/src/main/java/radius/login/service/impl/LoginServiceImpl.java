package radius.login.service.impl;

import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import radius.login.dao.LoginDao;
import radius.login.model.LoginVo;
import radius.login.service.LoginService;

@Service("radius.login.loginService")
//@MapperScan("radius.login.dao")
public class LoginServiceImpl implements LoginService{
	
	@Resource(name="radius.login.loginDao")
	private LoginDao loginDao;
	
	@Override
	public LoginVo selectAuthorizedUser(LoginVo loginVo) throws Exception{
		LoginVo resData = this.loginDao.selectAuthorizedUser(loginVo);
		return resData;
	}

}
