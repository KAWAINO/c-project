package iwm_ko.crewauth.service;

import java.util.List;
import java.util.Locale;

import iwm_ko.crewauth.model.CrewAuthVo;

public interface CrewAuthService {

	// 페이지 로딩 리스트
	List<CrewAuthVo> selectCrewAuthList(CrewAuthVo crewAuthVo, Locale locale) throws Exception;
	
	void defaultFormatDate(CrewAuthVo crewAuthVo) throws Exception;
	
	void searchFormatDate(CrewAuthVo crewAuthVo) throws Exception;

	// 페이징
	int totalCrewAuthList(CrewAuthVo crewAuthVo) throws Exception;
}
