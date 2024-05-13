package iwm_ko.monitor.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import iwm_ko.monitor.dao.MonitorDao;
import iwm_ko.monitor.model.MonitorVo;
import iwm_ko.monitor.service.MonitorService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@Service("iwm_ko.monitor.monitorService")
public class MonitorServiceImpl implements MonitorService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="iwm_ko.monitor.monitorDao")
    private MonitorDao monitorDao;
    
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter chFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    Calendar cal = new GregorianCalendar();
    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");

    // 선주사 리스트 조회
	@Override
	public List<MonitorVo> getCompList(MonitorVo monitorVo) throws Exception {
		return this.monitorDao.getCompList(monitorVo);
	}
	
//	@Override
//    public List<MonitorVo> selectMenuList(MonitorVo monitorVo) throws Exception {
//        List<MonitorVo> resList = this.monitorDao.selectMenuList(monitorVo);        
//        return resList;
//    }

    // 선박명 조회
	@Override
	public List<MonitorVo> getShipList(MonitorVo monitorVo) throws Exception {
		return this.monitorDao.getShipList(monitorVo);
	}

	// 로딩 시 날짜 디폴트값 포맷
	@Override
	public void defaultFormatDate(MonitorVo monitorVo) throws Exception {		
		LocalDate now = LocalDate.now();
        
        String defaultDate = now.format(chFormatter);
        monitorVo.setDefaultDate(defaultDate);

	    monitorVo.setStartHour("00");
	    monitorVo.setEndHour("23");
        
        monitorVo.setStartDate(now.format(formatter) + monitorVo.getStartHour() + "0000");
        monitorVo.setEndDate(now.format(formatter) + monitorVo.getEndHour() + "5959");	
		
	}

	// range, unit별 날짜 포맷
	@Override
	public void processDateFormat(MonitorVo monitorVo) throws Exception {
//		logger.info("====================== searchTime : " + monitorVo.getSearchTime() );
		if(monitorVo.getSearchRange().equals("first")) {
			int diff = 0;
			String startDate = "";
			String endDate = "";
			
			if(monitorVo.getSearchUnit().equals("hour")) {
				int hour = (Integer.parseInt(monitorVo.getSearchTime())*24*(-1));
				diff = hour;
				
				cal.setTime(new Date());
				cal.add(Calendar.HOUR, -2);
				
				endDate = fmt.format(cal.getTime());
				endDate = endDate.substring(0, endDate.length() -4) + "5959";
				cal.add(Calendar.HOUR, hour);
				startDate = fmt.format(cal.getTime()).substring(0, endDate.length() -4) + "0000";
				
			} else if(monitorVo.getSearchUnit().equals("day")) {
				
				int day = (Integer.parseInt(monitorVo.getSearchTime())*(-1));
				diff = day;
				
				cal.setTime(new Date());
				cal.add(Calendar.DAY_OF_MONTH, -1); // 시간을 더한다
				 
				endDate = fmt.format(cal.getTime());
				endDate=endDate.substring(0,endDate.length()-6)+"000000";
				cal.add(Calendar.DAY_OF_MONTH, day);
				startDate=fmt.format(cal.getTime()).substring(0,endDate.length()-6)+"000000";	
				
			} else {
				
				int month = (Integer.parseInt(monitorVo.getSearchTime())*(-1));
				diff = month;
				
				cal.setTime(new Date());
				cal.add(Calendar.MONTH, -1); // 시간을 더한다
				 
				endDate = fmt.format(cal.getTime());
				endDate = endDate.substring(0,endDate.length()-8)+"01000000";
				cal.add(Calendar.MONTH, month);
				startDate = fmt.format(cal.getTime()).substring(0,endDate.length()-8)+"01000000";
			}
			
			monitorVo.setDiffTime(diff);
			monitorVo.setStartDate(startDate);
			monitorVo.setEndDate(endDate);
		} else {
			String startDate = monitorVo.getStartDate();

		    monitorVo.setStartDefaultDate(startDate);
		    
	        LocalDate startDateTime = LocalDate.parse(startDate, chFormatter);  
	       
	        if(monitorVo.getSearchUnit().equals("hour")) {
	        	startDate = startDateTime.format(formatter) + monitorVo.getStartHour() + "0000";
	        } else if(monitorVo.getSearchUnit().equals("day")) {
	        	startDate = startDateTime.format(formatter) + "000000";
	        } else {
	        	startDate = startDateTime.format(formatter).substring(0, 6) + "01000000";
	        }

	        monitorVo.setStartDate(startDate);

//	        logger.info("================================= term startDate : " + monitorVo.getStartDate());
	        
		    String endDate = monitorVo.getEndDate();  

		    monitorVo.setEndDefaultDate(endDate);
		    
	        LocalDate endDateTime = LocalDate.parse(endDate, chFormatter);  
	       
	        if(monitorVo.getSearchUnit().equals("hour")) {
	        	endDate = endDateTime.format(formatter) + monitorVo.getEndHour() + "5959";
	        } else if(monitorVo.getSearchUnit().equals("day")) {
	        	endDate = endDateTime.format(formatter) + "000000";
	        } else {
	        	endDate = endDateTime.format(formatter).substring(0, 6) + "01000000";
	        }

	        monitorVo.setEndDate(endDate);
//			logger.info("================================= term endDate : " + monitorVo.getEndDate());
			
			int diff = 0;
			Date diffStartDate;
			Date diffEndDate;
			
			diffStartDate = fmt.parse(monitorVo.getStartDate());
			diffEndDate = fmt.parse(monitorVo.getEndDate());
			long startTime = diffStartDate.getTime();
			long endTime = diffEndDate.getTime();
			
			long mills = endTime - startTime;
			long hour = 0;
			
			if(monitorVo.getSearchUnit().equals("hour")) {
				hour = mills / (60 * 60 * 1000);	
			} else if(monitorVo.getSearchUnit().equals("day")) {
				hour = mills / (24 * 60 * 60 * 1000);
			} else {
				int e_year=Integer.parseInt(monitorVo.getEndDate().substring(0,4));
				int s_year=Integer.parseInt(monitorVo.getStartDate().substring(0,4));
				int e_month=Integer.parseInt(monitorVo.getEndDate().substring(4,6));
				int s_month=Integer.parseInt(monitorVo.getStartDate().substring(4,6));
				
				hour = ((e_year - s_year) * 12) + (e_month - s_month);
			}
			diff = (int) hour ;
//			logger.info("================================= term diff : " + diff);
			monitorVo.setDiffTime(diff);
		}
		
		
	}
	
	// 테이블 조회
	@Override
	public String getTable(MonitorVo monitorVo) throws Exception {
		return this.monitorDao.getTable(monitorVo);
	}

	// getTimeChart 시간 포맷
	@Override
	public List<MonitorVo> getTimeList(MonitorVo monitorVo, String day, String hour) throws Exception {
		
		String unit = monitorVo.getSearchUnit();
		monitorVo.setSearchUnit(unit);
//		String tableName = monitorDao.getTable(monitorVo);
//		monitorVo.setTableName(tableName);
		
		// searchCompId 값에 0이 포함될 경우
		if(monitorVo.getSearchCompId().contains("0")) {
			monitorVo.setSearchCompId("");
		}		
		
		List<MonitorVo> getTimeList = this.monitorDao.getTimeList(monitorVo);		
		
		String fmtTime = "";
		for(int i = 0; i < getTimeList.size(); i++){
			if ("hour".equals(monitorVo.getSearchUnit())) {
		    	fmtTime = getTimeList.get(i).getMon_time().substring(6, getTimeList.get(i).getMon_time().length() - 6) + day + " "
		                        + getTimeList.get(i).getMon_time().substring(8, getTimeList.get(i).getMon_time().length() - 4) + hour;
		    } else if ("day".equals(monitorVo.getSearchUnit())) {
		    	fmtTime = getTimeList.get(i).getMon_time().substring(4, getTimeList.get(i).getMon_time().length() - 8) + "/"
		                        + getTimeList.get(i).getMon_time().substring(6, getTimeList.get(i).getMon_time().length() - 6);
		    } else {
		    	fmtTime = getTimeList.get(i).getMon_time().substring(0, 4) + "/" 
		                        + getTimeList.get(i).getMon_time().substring(4, getTimeList.get(i).getMon_time().length() - 8);
		    }
			getTimeList.get(i).setMon_time(fmtTime);
	    }
		
		return getTimeList;
	}

	@Override
	public List<MonitorVo> getDataList(MonitorVo monitorVo) throws Exception {
	
		// searchCompId 값에 0이 포함될 경우
		if(monitorVo.getSearchCompId().contains("0")) {
			monitorVo.setSearchCompId("");
		}
//		List<MonitorVo> getDataList = monitorDao.getDataList(monitorVo);
		List<MonitorVo> resList = new ArrayList<>();
		
		// 조회 클릭 시 선택된 s_code를 담아놓을 배열 선언
	    String[] shipCodes = monitorVo.getSearchShipCode().split(",");    
		
		for(String shipCode : shipCodes) {
	    	monitorVo.setSearchShipCode(shipCode.trim());
	    	List<MonitorVo> getDataList = monitorDao.getDataList(monitorVo);
	    	
	    	if(!getDataList.isEmpty()) {
	    		resList.addAll(getDataList);
	    	}
	    }
//		logger.info("================================== resList : " + resList.size());
		
		// searchOrder 변수의 값에 따라 오름차순 또는 내림차순으로 정렬
	    if (monitorVo.getSearchOrder().equals("DESC")) {
	        resList.sort(Comparator.comparing(MonitorVo::getVal2).reversed());
	    } else if(monitorVo.getSearchOrder().equals("ASC")){
	        resList.sort(Comparator.comparing(MonitorVo::getVal2));
	    }
	    
	    // searchCompCnt의 수량만큼만 리스트에서 가져오기
	    if (monitorVo.getSearchCompCnt() != null && !resList.isEmpty()) {
	        int count = Math.min(resList.size(), Integer.parseInt(monitorVo.getSearchCompCnt()));
	        return resList.subList(0, count);
	    }

		return resList;
	}

	@Override
	public List<MonitorVo> getDataTimeList(MonitorVo monitorVo) throws Exception {
		
		// searchCompId 값에 0이 포함될 경우
		if(monitorVo.getSearchCompId().contains("0")) {
			monitorVo.setSearchCompId("");
		}
		
		List<MonitorVo> getDataTimeList = this.monitorDao.getDataTimeList(monitorVo);	
		
		return getDataTimeList;
	}

	@Override
	public String getShipName(MonitorVo monitorVo) throws Exception {
		return this.monitorDao.getShipName(monitorVo);
	}
	
	@Override
	public void lastShipChartDateFormat(MonitorVo monitorVo) throws Exception {
	
		String shipChartStartDate = "";
		String shipChartEndtDate = "";
		
		// searchCompId 값에 0이 포함될 경우
		if(monitorVo.getSearchCompId().contains("0")) {
			monitorVo.setSearchCompId("");	
		}

		cal.setTime(new Date());
		
		cal.add(Calendar.MONTH, -1);
	    int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	    shipChartEndtDate = fmt.format(cal.getTime());
	    shipChartEndtDate = shipChartEndtDate.substring(0, shipChartEndtDate.length() -8) + lastDay + "235959";
	    shipChartStartDate = shipChartEndtDate.substring(0, shipChartEndtDate.length() -8) + "01000000";
		
	    monitorVo.setStartDate(shipChartStartDate);
		monitorVo.setEndDate(shipChartEndtDate);
	}
	
	@Override
	public void thisShipChartDateFormat(MonitorVo monitorVo) throws Exception {
	
		String shipChartStartDate = "";
		String shipChartEndtDate = "";
		
		// searchCompId 값에 0이 포함될 경우
		if(monitorVo.getSearchCompId().contains("0")) {
			monitorVo.setSearchCompId("");	
		}

		cal.setTime(new Date());
		cal.add(Calendar.HOUR, -2);
		
		shipChartEndtDate = fmt.format(cal.getTime());
		shipChartEndtDate = shipChartEndtDate.substring(0, shipChartEndtDate.length() -4) + "5959";
		shipChartStartDate = shipChartEndtDate.substring(0, shipChartEndtDate.length() -8) + "01000000";
		
		monitorVo.setStartDate(shipChartStartDate);
		monitorVo.setEndDate(shipChartEndtDate);
	}






	
}
