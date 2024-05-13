package apms.login.service.impl;

import org.springframework.stereotype.Service;

import apms.login.dao.LoginDao;
import apms.login.model.LoginVo;
import apms.login.service.LoginService;
import jakarta.annotation.Resource;

@Service("apms.login.loginService")
public class LoginServiceImpl implements LoginService{
	@Resource(name="apms.login.loginDao")
	private LoginDao loginDao;
	
	public LoginVo selectAuthorizedUser(LoginVo loginVo) throws Exception{
		LoginVo resData = this.loginDao.selectAuthorizedUser(loginVo);
		return resData;
	}
}
