package iwm_ko.monitor.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import iwm_ko.monitor.model.MonitorVo;
import iwm_ko.monitor.service.MonitorService;
import iwm_ko.shipInfo.service.ShipInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/monitor")
public class MonitorController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private MessageSource messageSource;  

    @Resource(name="iwm_ko.monitor.monitorService")
    private MonitorService monitorService;
    
    @Resource(name="iwm_ko.shipInfo.ShipInfoService")
    private ShipInfoService shipInfoService;
        	
    /**
     * 페이지 로딩
     */
    @RequestMapping("monitor.do")
    public String dashboard(@ModelAttribute("monitorVo") MonitorVo monitorVo, HttpSession session, Model model
    		, HttpServletRequest request, @RequestParam(value = "language", required = false) String language) throws Exception {

//    	logger.info("pwdChangeCheck ::::: " + session.getAttribute("pwdChangeCheck"));
//    	logger.info("pwdLimitDays ::::: " + (String)session.getAttribute("pwdLimitDays").toString());
    	
    	// 언어변경 감지를 위한 파라미터 set
//    	logger.info("comp_id :::: " + (String)session.getAttribute("comp_id"));
//    	String language = request.getParameter("language");   	

    	if(language == null || language.isEmpty()) {
    		language = "korean";
    	}   	
    	session.setAttribute("language", language);	
        String scomp = (String)session.getAttribute("comp_id");

        monitorService.defaultFormatDate(monitorVo);
        model.addAttribute("startDefaultDate", monitorVo.getDefaultDate());
        model.addAttribute("endDefaultDate", monitorVo.getDefaultDate());
        
        // 선주사 리스트 조회
        List<String> compList = shipInfoService.selectCompList(scomp);
        model.addAttribute("compList", compList);

        return "monitor/monitor";
    }
    
//    @ResponseBody
//	@RequestMapping(value="menuList.ajax", method=RequestMethod.POST) 
//	public List<MonitorVo> menuList(MonitorVo monitorVo, Model model, HttpSession session, HttpServletRequest request
//			, @RequestParam(value = "grade", required = false) String grade) throws Exception {
//    	
////    	logger.info("grade ::::: " + grade);
////    	logger.info("URI ::::: " + request.getRequestURI());
//    	monitorVo.setLanguage((String)session.getAttribute("language"));
//    	monitorVo.setUserGrade(grade);
//    	List<MonitorVo> menuList = this.monitorService.selectMenuList(monitorVo);
//        model.addAttribute("menuList", menuList);   
//        
//        return menuList;
//    }

	// 선박명 조회
	@ResponseBody
	@RequestMapping(value="shipList.ajax", method=RequestMethod.POST) 
	public List<MonitorVo> shipList(MonitorVo monitorVo, Model model, HttpSession session) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");	
		if(!scomp.equals("0")) {
			monitorVo.setScomp(scomp);
		}
		String searchCompId	= monitorVo.getSearchCompId();

		List<MonitorVo> allShips = new ArrayList<>();
//		logger.info("searchCompId ::::: " + searchCompId);

		if(searchCompId == null || searchCompId.isEmpty()) {
			monitorVo.setSearchCompId("''");

		List<MonitorVo> shipList = monitorService.getShipList(monitorVo); 
		
			if(!shipList.isEmpty()) {
		        allShips.addAll(shipList);   
	        } 
		} else {

			// 쉼표로 구분된 ID를 배열로 변환
		    String[] compIds = searchCompId.split(","); 
	
		    for(String compId : compIds) {    	
	
		    	// 공백 제거
		        monitorVo.setSearchCompId(compId.trim()); 
		        // 선박명 조회
		        List<MonitorVo> shipList = monitorService.getShipList(monitorVo); 
		        // 결과를 전체 목록에 추가
		        if(!shipList.isEmpty()) {
			        allShips.addAll(shipList);   
		        } 
		    }  
		}
		model.addAttribute("allShips", allShips);
	    return allShips; // 전체 선박 목록 반환
		
	}
	
	// 챠트 시간 조회
	@ResponseBody
	@RequestMapping(value="getTime.ajax", method=RequestMethod.POST) 
	public List<MonitorVo> getTime(MonitorVo monitorVo, Model model, Locale locale, HttpSession session) throws Exception {	
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			monitorVo.setScomp(scomp);
		}
		
		// 날자 포멧, range = 기간일 경우 start, end, diff타임 포멧
		monitorService.processDateFormat(monitorVo);
		
		// diff_time 음수일 경우 양수로 전환
		int diffTime = monitorVo.getDiffTime();
		if(diffTime < 0) {
			monitorVo.setDiffTime(diffTime * (-1));
		}
		
		// getTableName
		String getTable = monitorService.getTable(monitorVo);
		monitorVo.setTableName(getTable);
		
		String day = messageSource.getMessage("graph.d", null, locale);
		String hour = messageSource.getMessage("graph.h", null, locale);
		
		// getTimeList
		List<MonitorVo> getTimeList = monitorService.getTimeList(monitorVo, day, hour);
//		logger.info("=============================== getTimeList : " + getTimeList);
		
		return getTimeList;
	}
	
	// 챠트 데이터 조회	
	@ResponseBody
	@RequestMapping(value="getTimeChartData.ajax", method=RequestMethod.POST) 
	public HashMap<String, Object> getTimeChartData(MonitorVo monitorVo, Model model, HttpSession session) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			monitorVo.setScomp(scomp);
		}
//		logger.info("searchShipCode ::::: "+ monitorVo.getSearchShipCode());

		// 최초 선택된 선박의 s_code 담아놓을 변수 선언
		String searchShipCode[] = monitorVo.getSearchShipCode().split(", ");

		// 날짜 포멧
		monitorService.processDateFormat(monitorVo);
		
		//monitorVo.setSearchUnit("hour");
		String tableName = monitorService.getTable(monitorVo);
		monitorVo.setTableName(tableName);
		
		HashMap<String, Object> resultMap = new HashMap<>();
		
		// diff_time 음수일 경우 양수로 전환
		int diffTime = monitorVo.getDiffTime();
		if(diffTime < 0) {
			monitorVo.setDiffTime(diffTime * (-1));
		}

//		if(searchShipCode != null) {
		if(Integer.parseInt(monitorVo.getSearchCompCnt()) < searchShipCode.length) {
			
			List<MonitorVo> getDataList = this.monitorService.getDataList(monitorVo);
			
			searchShipCode = new String[getDataList.size()];
			for(int i = 0; i < getDataList.size(); i++) {
				searchShipCode[i] = getDataList.get(i).getS_code();
			}
//				logger.info("===================== getDataList : " + getDataList.size());
		}
//			logger.info("===================== searchShipCode : " + searchShipCode.length);
		for(String shipCode : searchShipCode) {				
			monitorVo.setSearchShipCode(shipCode.trim());	
			
			List<MonitorVo> getDataTimeList = monitorService.getDataTimeList(monitorVo);
			
			// 각 선박 코드에 대한 val2 값들을 저장할 리스트
            List<String> val2List = new ArrayList<>();
            
            for (MonitorVo data : getDataTimeList) {
                val2List.add(data.getVal2());
            }

            String getShipName = monitorService.getShipName(monitorVo);
			resultMap.put(getShipName, val2List);

//				logger.info(monitorService.getShipName(monitorVo) + " : " + val2List);			
		}
//		}
//		logger.info("diffTime :::: " + monitorVo.getDiffTime());
		// getShipName null일경우 처리
		resultMap.entrySet().removeIf(entry -> entry.getKey() == null);
		return resultMap;
	}
	
	// 바 그래프 전월 데이터 조회
	@ResponseBody
	@RequestMapping(value = "getLastShipChart.ajax", method = RequestMethod.POST)
	public List<MonitorVo> getLastShipChart(MonitorVo monitorVo, Model model, HttpSession session) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			monitorVo.setScomp(scomp);
		}
		
		monitorService.lastShipChartDateFormat(monitorVo);
		
		String getTable = monitorService.getTable(monitorVo);
	    monitorVo.setTableName(getTable);
		
		List<MonitorVo> getLastShipChart = this.monitorService.getDataList(monitorVo);
//	    logger.info("=============================== getLastShipChart : " + getLastShipChart);
	    
	    return getLastShipChart;
	}
	
	// 바 그래프 금월 데이터 조회
	@ResponseBody
	@RequestMapping(value = "getThisShipChart.ajax", method = RequestMethod.POST)
	public List<MonitorVo> getThisShipChart(MonitorVo monitorVo, Model model, HttpSession session) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			monitorVo.setScomp(scomp);
		}
		
		monitorService.thisShipChartDateFormat(monitorVo);
		
		String getTable = monitorService.getTable(monitorVo);
	    monitorVo.setTableName(getTable);

	    List<MonitorVo> getThisShipChart = this.monitorService.getDataList(monitorVo);
//	    logger.info("=============================== getThisShipChart : " + getThisShipChart);
	    
	    return getThisShipChart;
	}
	
}
