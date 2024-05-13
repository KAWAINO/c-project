package iwm_ko.login.service.impl;

import iwm_ko.login.dao.LoginDao;
import iwm_ko.login.model.LoginVo;
import iwm_ko.login.service.LoginService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("iwm_ko.login.loginService")
public class LoginServiceImpl implements LoginService{
	@Resource(name="iwm_ko.login.loginDao")
	private LoginDao loginDao;
	
	public LoginVo selectAuthorizedUser(LoginVo loginVo) throws Exception{
		LoginVo resData = this.loginDao.selectAuthorizedUser(loginVo);
		return resData;
	}

    @Override 
    public List<LoginVo> getUserPassLimit( ) throws Exception { 
        return this.loginDao.getUserPassLimit(); 
    } 

    @Override 
    public String getUserPassChangeDate(String user_id) throws Exception { 
        return this.loginDao.getUserPassChangeDate(user_id); 
    } 

	@Override
	public List<LoginVo> checkId(LoginVo loginVo) {
		List<LoginVo> resList = this.loginDao.checkId(loginVo);
		return resList;
	}

	@Override
	public int getLockCount() {
		return this.loginDao.getLockCount();
	}

	@Override
	public int updateFailCount(String user_id) {
		return this.loginDao.updateFailCount(user_id);
	}

	@Override
	public int getFailCount(String user_id) {
		return this.loginDao.getFailCount(user_id);
	}

	@Override
	public int insertFailCount(String user_id) {
		return this.loginDao.insertFailCount(user_id);
	}

	@Override
	public Boolean checkMenu(LoginVo loginVo) throws Exception {
		return this.loginDao.checkMenu(loginVo);
	}


}
