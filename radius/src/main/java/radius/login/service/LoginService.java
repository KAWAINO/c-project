package radius.login.service;

import radius.login.model.LoginVo;

public interface LoginService {
	/**
	 * id, password가 맞는 회원 조회
	 * @param loginVo
	 * @return
	 * @throws Exception
	 */
	public LoginVo selectAuthorizedUser(LoginVo loginVo) throws Exception;

}
