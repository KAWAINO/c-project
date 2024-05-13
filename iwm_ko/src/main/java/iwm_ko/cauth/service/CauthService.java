package iwm_ko.cauth.service;

import java.util.List;

import iwm_ko.cauth.model.CauthVo;
import iwm_ko.cuser.model.CuserVo;

public interface CauthService {

	// 등급별 리스트 조회
	List<CauthVo> selectSearchList(CauthVo cauthVo) throws Exception;

	// 리스트 카운트
	List<CauthVo> listCnt(CauthVo cauthVo) throws Exception;
	
	// 등급별 권한 변경
	int updateGrade(CauthVo cauthVo) throws Exception;
}
