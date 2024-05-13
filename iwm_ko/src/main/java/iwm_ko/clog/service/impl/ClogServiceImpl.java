package iwm_ko.clog.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import iwm_ko.clog.dao.ClogDao;
import iwm_ko.clog.model.ClogVo;
import iwm_ko.clog.service.ClogService;
import jakarta.annotation.Resource;

@Service("iwm_ko.clog.clogService")
public class ClogServiceImpl implements ClogService {
	
	@Autowired
	private MessageSource messageSource;

	@Resource(name="iwm_ko.clog.clogDao")
	private ClogDao clogDao;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter chFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	// 페이지 로딩 시 검색기간 default 설정
	@Override
	public void defaultFormatDate(ClogVo clogVo) throws Exception {
		
		LocalDate now = LocalDate.now();
        
        String defaultDate = now.format(chFormatter);
        clogVo.setDefaultDate(defaultDate);

        clogVo.setEndHour("23");
        clogVo.setEndMin("59");
        
        clogVo.setStartDate(now.format(formatter) + "000000");
        clogVo.setEndDate(now.format(formatter) + clogVo.getEndHour() + clogVo.getEndMin() + "59");	
	}
	
	// 검색 시 검색기간 default 설정
	@Override
	public void searchFormatDate(ClogVo clogVo) throws Exception {
	    
	    String startDate = clogVo.getStartDate();
	    String endDate = clogVo.getEndDate();
	    
	    clogVo.setStartDefaultDate(startDate);
	    clogVo.setEndDefaultDate(endDate);
	    
        LocalDate startDateTime = LocalDate.parse(startDate, chFormatter); 
        LocalDate endDateTime = LocalDate.parse(endDate, chFormatter); 
        
        startDate = startDateTime.format(formatter) + clogVo.getStartHour() + clogVo.getStartMin() + "00";
        endDate = endDateTime.format(formatter) + clogVo.getEndHour() + clogVo.getEndMin() + "59";

        clogVo.setStartDate(startDate);
        clogVo.setEndDate(endDate);
	}
	
	// menuList 조회
	@Override
	public List<ClogVo> getMenuList(ClogVo clogVo) throws Exception {
		List<ClogVo> resList = this.clogDao.getMenuList(clogVo);
		return resList;
	}

	// logList 조회
	@Override
	public List<ClogVo> selectClogList(ClogVo clogVo, Locale locale) throws Exception {
		List<ClogVo> resList = this.clogDao.selectClogList(clogVo);
		
		for(int i = 0; i < resList.size(); i++) {
			if(resList.get(i).getJob_name().equals("로그인")) {
				resList.get(i).setJob_name(messageSource.getMessage("clog.login", null, locale));
			} else if(resList.get(i).getJob_name().equals("로그아웃")) {
				resList.get(i).setJob_name(messageSource.getMessage("clog.logOut", null, locale));
			} else if(resList.get(i).getJob_name().equals("조회") || resList.get(i).getJob_name().equals("검색")) {
				resList.get(i).setJob_name(messageSource.getMessage("clog.search", null, locale));
			} else if(resList.get(i).getJob_name().equals("추가")) {
				resList.get(i).setJob_name(messageSource.getMessage("clog.add", null, locale));
			} else if(resList.get(i).getJob_name().equals("수정")) {
				resList.get(i).setJob_name(messageSource.getMessage("clog.modify", null, locale));
			} else if(resList.get(i).getJob_name().equals("삭제")) {
				resList.get(i).setJob_name(messageSource.getMessage("clog.delete", null, locale));
			} else if(resList.get(i).getJob_name().equals("엑셀")) {
				resList.get(i).setJob_name(messageSource.getMessage("clog.list.excel", null, locale));
			} else if(resList.get(i).getJob_name().equals("Import")) {
				resList.get(i).setJob_name(messageSource.getMessage("clog.list.import", null, locale));
			} else if(resList.get(i).getJob_name().equals("보고서 출력")) {
				resList.get(i).setJob_name(messageSource.getMessage("clog.printReport", null, locale));
			}
		}
		return resList;
	}

	// 페이징
	@Override
	public int totalClogList(ClogVo clogVo) throws Exception {
		return this.clogDao.totalClogList(clogVo);
	}

}
