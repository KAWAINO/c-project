package iwm_ko.login.service;

import iwm_ko.login.model.LoginVo;

import java.util.Date;
import java.util.List;

public interface LoginService {
	/**
	 * id, password가 맞는 회원 조회
	 * @param loginVo
	 * @return
	 * @throws Exception
	 */
	public LoginVo selectAuthorizedUser(LoginVo loginVo) throws Exception;

    List<LoginVo> getUserPassLimit( ) throws Exception; 

    String getUserPassChangeDate(String user_id) throws Exception; 

	List<LoginVo> checkId(LoginVo loginVo);

	int getLockCount();

	int updateFailCount(String user_id);

	int getFailCount(String user_id);

	int insertFailCount(String user_id);

	public Boolean checkMenu(LoginVo loginVo) throws Exception;
}
