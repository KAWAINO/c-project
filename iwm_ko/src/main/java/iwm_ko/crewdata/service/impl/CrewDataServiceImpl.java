package iwm_ko.crewdata.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import iwm_ko.crewdata.dao.CrewDataDao;
import iwm_ko.crewdata.model.CrewDataVo;
import iwm_ko.crewdata.service.CrewDataService;
import jakarta.annotation.Resource;

@Service("iwm_ko.crewdata.crewDataService")
public class CrewDataServiceImpl implements CrewDataService {

	@Resource(name="iwm_ko.crewdata.crewDataDao")
	private CrewDataDao crewDataDao;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter chFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	// 페이지 로딩 시 검색기간 default 설정
	@Override
	public void defaultFormatDate(CrewDataVo crewDataVo) throws Exception {
		
		LocalDate now = LocalDate.now();
        
        String defaultDate = now.format(chFormatter);
        crewDataVo.setDefaultDate(defaultDate);

        crewDataVo.setEndHour("23");
        crewDataVo.setEndMin("59");
        
        crewDataVo.setStartDate(now.format(formatter) + "000000");
        crewDataVo.setEndDate(now.format(formatter) + crewDataVo.getEndHour() + crewDataVo.getEndMin() + "59");	
	}
	
	// 검색 시 검색기간 default 설정
	@Override
	public void searchFormatDate(CrewDataVo crewDataVo) throws Exception {
	    
	    String startDate = crewDataVo.getStartDate();
	    String endDate = crewDataVo.getEndDate();
	    
	    crewDataVo.setStartDefaultDate(startDate);
	    crewDataVo.setEndDefaultDate(endDate);
	    
        LocalDate startDateTime = LocalDate.parse(startDate, chFormatter); 
        LocalDate endDateTime = LocalDate.parse(endDate, chFormatter); 
        
        startDate = startDateTime.format(formatter) + crewDataVo.getStartHour() + crewDataVo.getStartMin() + "00";
        endDate = endDateTime.format(formatter) + crewDataVo.getEndHour() + crewDataVo.getEndMin() + "59";

        crewDataVo.setStartDate(startDate);
        crewDataVo.setEndDate(endDate);
	}

	// 로그리스트 조회
	@Override
	public List<CrewDataVo> selectLogList(CrewDataVo crewDataVo) throws Exception {
		
		String diff = "";
		
		List<CrewDataVo> resList = this.crewDataDao.selectLogList(crewDataVo);
		
		for(int i = 0; i < resList.size(); i++) {
			if(Integer.parseInt(resList.get(i).getDiff_data()) < 0) {
				diff = "-" + String.format("%,d", (Integer.parseInt(resList.get(i).getDiff_data()) * (-1) )) + " MB";
			} else {
				diff = String.format("%,d", Integer.parseInt(resList.get(i).getDiff_data())) + " MB";
			}
			resList.get(i).setDiff_data(diff);
		}
		
		return resList;
	}

	// 페이징
	@Override
	public int totalCrewList(CrewDataVo crewDataVo) throws Exception {
		return this.crewDataDao.totalCrewList(crewDataVo);
	}
}
