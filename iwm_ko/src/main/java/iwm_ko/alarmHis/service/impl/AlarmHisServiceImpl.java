package iwm_ko.alarmHis.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import iwm_ko.alarmHis.dao.AlarmHisDao;
import iwm_ko.alarmHis.model.AlarmHisVo;
import iwm_ko.alarmHis.service.AlarmHisService;
import jakarta.annotation.Resource;

@Service("iwm_ko.alarmHis.alarmHisService")
public class AlarmHisServiceImpl implements AlarmHisService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="iwm_ko.alarmHis.alarmHisDao")
	private AlarmHisDao alarmHisDao;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter chFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	// 페이지 로딩 시 검색기간 default 설정
	@Override
	public void defaultFormatDate(AlarmHisVo alarmHisVo) throws Exception {

		LocalDate now = LocalDate.now();
        
        String defaultDate = now.format(chFormatter);
        alarmHisVo.setDefaultDate(defaultDate);

        alarmHisVo.setEndHour("23");
        alarmHisVo.setEndMin("59");
        
        alarmHisVo.setStartDate(now.format(formatter) + "000000");
        alarmHisVo.setEndDate(now.format(formatter) + alarmHisVo.getEndHour() + alarmHisVo.getEndMin() + "59");	
	}
	
	// 검색 시 검색기간 default 설정
	@Override
	public void searchFormatDate(AlarmHisVo alarmHisVo) throws Exception {
	    
	    String startDate = alarmHisVo.getStartDate();
	    String endDate = alarmHisVo.getEndDate();
	    
	    alarmHisVo.setStartDefaultDate(startDate);
	    alarmHisVo.setEndDefaultDate(endDate);
	    
        LocalDate startDateTime = LocalDate.parse(startDate, chFormatter); 
        LocalDate endDateTime = LocalDate.parse(endDate, chFormatter); 
        
        startDate = startDateTime.format(formatter) + alarmHisVo.getStartHour() + alarmHisVo.getStartMin() + "00";
        endDate = endDateTime.format(formatter) + alarmHisVo.getEndHour() + alarmHisVo.getEndMin() + "59";

        alarmHisVo.setStartDate(startDate);
        alarmHisVo.setEndDate(endDate);
	}

	// 알람원인 리스트 조회
	@Override
	public List<AlarmHisVo> getAlarmCauseList(AlarmHisVo alarmHisVo) throws Exception {
		return this.alarmHisDao.getAlarmCauseList(alarmHisVo);
	}

	// 알람이력 리스트 조회
	@Override
	public List<AlarmHisVo> getAlarmHisList(AlarmHisVo alarmHisVo) throws Exception {
		return this.alarmHisDao.getAlarmHisList(alarmHisVo);
	}

	// 페이징
	@Override
	public int totalAlarmHisList(AlarmHisVo alarmHisVo) throws Exception {
		return this.alarmHisDao.totalAlarmHisList(alarmHisVo);
	}

}
