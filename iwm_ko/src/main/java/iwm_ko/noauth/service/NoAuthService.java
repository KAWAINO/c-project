package iwm_ko.noauth.service;

import java.util.HashMap;
import java.util.List;

import iwm_ko.noauth.model.NoAuthVo;

public interface NoAuthService {

	// authList
	List<NoAuthVo> selectAuthList(NoAuthVo noAuthVo) throws Exception;

	// 페이징
	int totalAuthList(NoAuthVo noAuthVo) throws Exception;

	// chkMac
	List<NoAuthVo> chkMac(NoAuthVo noAuthVo) throws Exception;

	// chkName
	List<NoAuthVo> chkName(NoAuthVo noAuthVo) throws Exception;

	// chkFlag
	List<NoAuthVo> chkFlag(NoAuthVo noAuthVo) throws Exception;

	// chgAuth
	int chgAuth(NoAuthVo noAuthVo) throws Exception;

	// insAuth
	int insAuth(NoAuthVo noAuthVo) throws Exception;

	// 수정모달이동
	NoAuthVo selectAuthInfo(HashMap<String, Object> map) throws Exception;

	// 수정
	int updateAuth(NoAuthVo noAuthVo) throws Exception;

	// 삭제
	int deleteAuth(NoAuthVo noAuthVo) throws Exception;

	List<String> updChkName(NoAuthVo noAuthVo) throws Exception;

}
