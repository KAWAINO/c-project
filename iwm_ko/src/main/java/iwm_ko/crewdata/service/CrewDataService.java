package iwm_ko.crewdata.service;

import java.util.List;

import iwm_ko.crewdata.model.CrewDataVo;

public interface CrewDataService {
	
	void searchFormatDate(CrewDataVo crewDataVo) throws Exception;

	void defaultFormatDate(CrewDataVo crewDataVo) throws Exception;

	// 로그리스트 조회
	List<CrewDataVo> selectLogList(CrewDataVo crewDataVo) throws Exception;

	// 페이징
	int totalCrewList(CrewDataVo crewDataVo) throws Exception;

}
