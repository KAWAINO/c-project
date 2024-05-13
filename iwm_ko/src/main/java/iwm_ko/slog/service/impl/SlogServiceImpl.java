package iwm_ko.slog.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import iwm_ko.slog.dao.SlogDao;
import iwm_ko.slog.model.SlogVo;
import iwm_ko.slog.service.SlogService;
import jakarta.annotation.Resource;

@Service("iwm_ko.slog.slogService")
public class SlogServiceImpl implements SlogService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="iwm_ko.slog.slogDao")
	private SlogDao slogDao;
	
	@Autowired
	private MessageSource messageSource;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter chFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	// 페이지 로딩 시 검색기간 default 설정
	@Override
	public void defaultFormatDate(SlogVo slogVo) throws Exception {
		
		LocalDate now = LocalDate.now();
        
        String defaultDate = now.format(chFormatter);
        slogVo.setDefaultDate(defaultDate);

        slogVo.setEndHour("23");
        slogVo.setEndMin("59");
        
        slogVo.setStartDate(now.format(formatter) + "000000");
        slogVo.setEndDate(now.format(formatter) + slogVo.getEndHour() + slogVo.getEndMin() + "59");	
	}
	
	// 검색 시 검색기간 default 설정
	@Override
	public void searchFormatDate(SlogVo slogVo) throws Exception {
	    
	    String startDate = slogVo.getStartDate();
	    String endDate = slogVo.getEndDate();
	    
	    slogVo.setStartDefaultDate(startDate);
	    slogVo.setEndDefaultDate(endDate);
	    
        LocalDate startDateTime = LocalDate.parse(startDate, chFormatter); 
        LocalDate endDateTime = LocalDate.parse(endDate, chFormatter); 
        
        startDate = startDateTime.format(formatter) + slogVo.getStartHour() + slogVo.getStartMin() + "00";
        endDate = endDateTime.format(formatter) + slogVo.getEndHour() + slogVo.getEndMin() + "59";

        slogVo.setStartDate(startDate);
        slogVo.setEndDate(endDate);
	}

	// 메뉴리스트 조회
	@Override
	public List<SlogVo> getMenuList(SlogVo slogVo) throws Exception {	

		List<SlogVo> resList = this.slogDao.getMenuList(slogVo);	

		if(slogVo.getLanguage().equals("eng")) {
			for(int i = 0; i < resList.size(); i++) {
				resList.get(i).setGui_name(resList.get(i).getEn_gui_name());
			}
		}

		return resList;
	}
	
	// 선박명 조회
	@Override
	public List<SlogVo> selectShipList(SlogVo slogVo) throws Exception {
		List<SlogVo> resList = this.slogDao.selectShipList(slogVo);
		return resList;
	}

	// 로그리스트 조회
	@Override
	public List<SlogVo> selectSlogList(SlogVo slogVo, Locale locale) throws Exception {
		
		List<SlogVo> resList = this.slogDao.selectSlogList(slogVo, locale);
		
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
			} 
		}
		return resList;
	}

	// 페이징
	@Override
	public int totalSlogList(SlogVo slogVo) throws Exception {
		return this.slogDao.totalSlogList(slogVo);
	}

}
