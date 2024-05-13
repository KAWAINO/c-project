package iwm_ko.sauth.service;

import java.util.List;

import iwm_ko.sauth.model.SauthVo;

public interface SauthService {

	// 선박명 조회
	List<SauthVo> selectShipList(SauthVo sauthVo) throws Exception;

	// 리스트 조회
	List<SauthVo> selectSearchList(SauthVo sauthVo) throws Exception;

	// 업데이트
	int updateGrade(SauthVo sauthVo) throws Exception;

}
