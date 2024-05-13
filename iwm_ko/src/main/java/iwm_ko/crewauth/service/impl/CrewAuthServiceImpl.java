package iwm_ko.crewauth.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import iwm_ko.crewauth.dao.CrewAuthDao;
import iwm_ko.crewauth.model.CrewAuthVo;
import iwm_ko.crewauth.service.CrewAuthService;
import jakarta.annotation.Resource;

@Service("iwm_ko.crewauth.crewAuthService")
public class CrewAuthServiceImpl implements CrewAuthService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MessageSource messageSource;
	
	@Resource(name="iwm_ko.crewauth.crewAuthDao")
	private CrewAuthDao crewAuthDao;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter chFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // 페이지 로딩 시 검색기간 default 설정
 	@Override
 	public void defaultFormatDate(CrewAuthVo crewAuthVo) throws Exception {
 		
 		LocalDate now = LocalDate.now();
         
        String defaultDate = now.format(chFormatter);
        crewAuthVo.setDefaultDate(defaultDate);
        
        crewAuthVo.setConEndHour("23");
        crewAuthVo.setConEndMin("59");
        crewAuthVo.setDisconEndHour("23");
        crewAuthVo.setDisconEndMin("59");

        crewAuthVo.setConStrDate(now.format(formatter) + "000000");
        crewAuthVo.setConEndDate(now.format(formatter) + crewAuthVo.getConEndHour() + crewAuthVo.getConEndMin() + "59");
        crewAuthVo.setDisconStrDate(now.format(formatter) + "000000");
        crewAuthVo.setDisconEndDate(now.format(formatter) + crewAuthVo.getDisconEndHour() + crewAuthVo.getDisconEndMin() + "59");      
 	}
 	
 	// 검색 시 검색기간 default 설정
 	@Override
 	public void searchFormatDate(CrewAuthVo crewAuthVo) throws Exception {
 		
 		String conStartDate = crewAuthVo.getConStrDate();
 		String conEndDate = crewAuthVo.getConEndDate();
 		String disconStartDate = crewAuthVo.getDisconStrDate();
 		String disconEndDate = crewAuthVo.getDisconEndDate();
 		
 	    crewAuthVo.setConStartDefaultDate(conStartDate);
 	    crewAuthVo.setConEndDefaultDate(conEndDate);
 	    crewAuthVo.setDisconStartDefaultDate(disconStartDate);
	    crewAuthVo.setDisconEndDefaultDAte(disconEndDate);	     	    
 	    
        LocalDate conStartDateTime = LocalDate.parse(conStartDate, chFormatter); 
        LocalDate conEndDateTime = LocalDate.parse(conEndDate, chFormatter); 
        LocalDate disconStartDateTime = LocalDate.parse(disconStartDate, chFormatter); 
        LocalDate disconEndDateTime = LocalDate.parse(disconEndDate, chFormatter); 
        
        conStartDate = conStartDateTime.format(formatter) + crewAuthVo.getConStrHour() + crewAuthVo.getConStrMin() + "00";
        conEndDate = conEndDateTime.format(formatter) + crewAuthVo.getConEndHour() + crewAuthVo.getConEndMin() + "59";
        disconStartDate = disconStartDateTime.format(formatter) + crewAuthVo.getDisconStrHour() + crewAuthVo.getDisconStrMin() + "00";
        disconEndDate = disconEndDateTime.format(formatter) + crewAuthVo.getDisconEndHour() + crewAuthVo.getDisconEndMin() + "59";
        
        crewAuthVo.setConStrDate(conStartDate);
        crewAuthVo.setConEndDate(conEndDate);
        crewAuthVo.setDisconStrDate(disconStartDate);
        crewAuthVo.setDisconEndDate(disconEndDate);
 	}

	// 페이지 로딩 리스트
	@Override
	public List<CrewAuthVo> selectCrewAuthList(CrewAuthVo crewAuthVo, Locale locale) throws Exception{
		
		List<CrewAuthVo> result = this.crewAuthDao.selectCrewAuthList(crewAuthVo, locale);

		for(int i = 0; i < result.size(); i++) {
			
			String status = result.get(i).getStatus();
			String flag = result.get(i).getAuth_flag();
			String disconFlag = result.get(i).getDisconn_flag();

			String str = "";
			String str2 = "";
			str2 = messageSource.getMessage("select.using", null, locale);
			
			if(status.equals("1")) {
				result.get(i).setStatus(messageSource.getMessage("select.online", null, locale));
			} else if(status.equals("2")) {
				result.get(i).setStatus(messageSource.getMessage("select.offline", null, locale));
			} else {
				result.get(i).setStatus(messageSource.getMessage("select.blocked", null, locale));
			}
			
			if(flag.equals("Y")) {
				result.get(i).setAuth_flag(messageSource.getMessage("select.using", null, locale));
			} else {
				result.get(i).setAuth_flag(messageSource.getMessage("select.disconnectionByUse", null, locale));
			}
			
			if(disconFlag == null) {
				str = messageSource.getMessage("select.normal", null, locale);
			} else if(disconFlag.equals("1")) {
				str = messageSource.getMessage("select.normal", null, locale);
			} else if(disconFlag.equals("2")) {
				str = messageSource.getMessage("select.deliveryExceeded", null, locale);
			} else if(disconFlag.equals("3")) {
				str = messageSource.getMessage("select.limitedExceeded", null, locale);
			} else if(disconFlag.equals("4")) {
				str = messageSource.getMessage("select.notInUseTime", null, locale);
			} else if(disconFlag.equals("5")) {
				str = messageSource.getMessage("select.noDayOfUse", null, locale);
			} else if(disconFlag.equals("6")) {
				str = messageSource.getMessage("select.notInUsePeriod", null, locale);
			} else if(disconFlag.equals("7")) {
				str = messageSource.getMessage("select.usageExceeded", null, locale);
			} else if(disconFlag.equals("8")) {
				str = messageSource.getMessage("select.useTimeout", null, locale);
			} else if(disconFlag.equals("9")) {
				str = messageSource.getMessage("select.disconnectionByUse", null, locale);
			} else if(disconFlag.equals("10")) {
				str = messageSource.getMessage("select.disconnectionByOperator", null, locale);
			} else if(disconFlag.equals("11")) {
				str = messageSource.getMessage("select.deauthorization", null, locale);
			} else if(disconFlag.equals("12")) {
				str = messageSource.getMessage("select.concurrentUsersExceeded", null, locale);
			} else {
				str = messageSource.getMessage("select.blockWIFIuse", null, locale);
			}
//			logger.info("============== str : " + str);
			
			if(result.get(i).getStatus().equals("1")) {
				result.get(i).setAuth_flag(str2);
			} else {
				result.get(i).setAuth_flag(str);
			}
			
		}
		return result;
	}

	// 페이징 
	@Override
	public int totalCrewAuthList(CrewAuthVo crewAuthVo) throws Exception {
		return this.crewAuthDao.totalCrewAuthList(crewAuthVo);
	}

}
