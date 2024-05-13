package iwm_ko.apinfo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import iwm_ko.apinfo.model.ApInfoVo;

public interface ApInfoService {

	// 대역폭 리스트 조회
	List<ApInfoVo> selectBandList(ApInfoVo apInfoVo) throws Exception;
	
	// apinfo 리스트 조회
	List<ApInfoVo> selectApInfoList(ApInfoVo apInfoVo, Locale locale) throws Exception;

	// 페이징
	int totalApInfoList(ApInfoVo apInfoVo) throws Exception;
	
	List<ApInfoVo> selectShipList(ApInfoVo apInfoVo) throws Exception;

	// mac 중복검사
	List<ApInfoVo> chkMac(ApInfoVo apInfoVo) throws Exception;

	// ap_name 중복검사
	List<ApInfoVo> chkName(ApInfoVo apInfoVo) throws Exception;
	
	// flag 체크
	List<ApInfoVo> chkFlag(ApInfoVo apInfoVo) throws Exception;

	// 기존 sp 데이터 수정
	int changeAp(ApInfoVo apInfoVo) throws Exception;

	// apinfo 신규등록
	int insertApInfo(ApInfoVo apInfoVo) throws Exception;
	
	void connTimeFormat(ApInfoVo apInfoVo) throws Exception;

	// 수정모달
	ApInfoVo selectApInfo(HashMap<String, Object> map) throws Exception;

	// subdata 중복검사
	List<ApInfoVo> chkSubData(ApInfoVo apInfoVo) throws Exception;

	// 기존 data 수정 
	int changeApSub(ApInfoVo apInfoVo) throws Exception;

	// subdata 신규등록
	int insertApSub(ApInfoVo apInfoVo) throws Exception;

	// update
	int updateApInfo(ApInfoVo apInfoVo) throws Exception;
	
	// subdata 중복검사2
	List<ApInfoVo> chkSubData2(ApInfoVo apInfoVo) throws Exception;
	
	int updateApSubY(ApInfoVo apInfoVo) throws Exception;
	
	int insertApSubY(ApInfoVo apInfoVo) throws Exception;
	
	int updateApSubN(ApInfoVo apInfoVo) throws Exception;

	int insertApSubN(ApInfoVo apInfoVo) throws Exception;
	
	// 삭제
	int deleteApInfo(ApInfoVo apInfoVo) throws Exception;

	int reboot(ApInfoVo apInfoVo) throws Exception;

	List<String> updChkName(ApInfoVo apInfoVo) throws Exception;

	



	

	



}
