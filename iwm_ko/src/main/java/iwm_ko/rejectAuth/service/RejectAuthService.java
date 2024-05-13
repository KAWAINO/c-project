package iwm_ko.rejectAuth.service;

import java.util.HashMap;
import java.util.List;

import iwm_ko.rejectAuth.model.RejectAuthVo;

public interface RejectAuthService {

	List<RejectAuthVo> selectRejectAuthList(RejectAuthVo rejectAuthVo) throws Exception;

	// 페이징
	int totalRejectAuthList(RejectAuthVo rejectAuthVo) throws Exception;

	// chkMac
	List<RejectAuthVo> chkMac(RejectAuthVo rejectAuthVo) throws Exception;

	// chgAuth
	int insertAuth(RejectAuthVo rejectAuthVo) throws Exception;

	// 수정모달
	RejectAuthVo selectRejectAuthInfo(HashMap<String, Object> map) throws Exception;

	// 수정
	int updateRejectAuth(RejectAuthVo rejectAuthVo) throws Exception;

	// 삭제
	int deleteRejectAuth(RejectAuthVo rejectAuthVo) throws Exception;

}
