package iwm_ko.rejectExcept.service;

import java.util.HashMap;
import java.util.List;

import iwm_ko.cuser.model.CuserVo;
import iwm_ko.rejectExcept.model.RejectExceptVo;

public interface RejectExceptService {

	List<RejectExceptVo> selectRejectExceptList(RejectExceptVo rejectExceptVo) throws Exception;

	// 페이징
	int totalRejectExceptList(RejectExceptVo rejectExceptVo) throws Exception;

	// chkMac
	List<CuserVo> chkMac(RejectExceptVo rejectExceptVo) throws Exception;

	// 등록
	int insertAuth(RejectExceptVo rejectExceptVo) throws Exception;

	// 수정모달
	RejectExceptVo selectRejectExceptInfo(HashMap<String, Object> map) throws Exception;

	// 수정
	int updateAuth(RejectExceptVo rejectExceptVo) throws Exception;

	// 삭제
	int deleteAuth(RejectExceptVo rejectExceptVo) throws Exception;

}
