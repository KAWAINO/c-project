package iwm_ko.alarmmon.controller;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import iwm_ko.alarmmon.model.AlarmMonVo;
import iwm_ko.alarmmon.service.AlarmMonService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/alarmmon")
public class AlarmMonController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="iwm_ko.alarmmon.alarmMonService")
    private AlarmMonService alarmMonService;
    
    /**
     * 페이지 로딩
     */
    @RequestMapping("alarmmon.do")
    public String dashboard(@ModelAttribute("alarmMonVo") AlarmMonVo alarmMonVo, Model model) throws Exception {
  
    	return "alarmmon/alarmmon";
    }
    
    // 알람현황 페이지 로딩
 	@RequestMapping(value="alarmmon_sys.do", method=RequestMethod.POST) 
 	public String getAlarmSys(AlarmMonVo alarmMonVo, Model model, final HttpSession session) throws Exception {
    	
 		String comp_id = (String) session.getAttribute("comp_id");
     	alarmMonVo.setComp_id(comp_id);
		
		List<AlarmMonVo> getAlarmSysList = this.alarmMonService.getAlarmSysList(alarmMonVo);
		model.addAttribute("getAlarmSysList", getAlarmSysList);
		
		List<AlarmMonVo> getSevList = this.alarmMonService.getSevList(alarmMonVo);
       	
		String critical = "0";
		String major = "0";
		String minor = "0";

		// 각 severity에 따라 값을 설정
		for (AlarmMonVo alarm : getSevList) {
		    int cnt = Integer.parseInt(alarm.getS_cnt());
		    String fmtCnt = cnt >= 100 ? "99+" : String.valueOf(cnt);

		    switch (alarm.getSeverity()) {
		        case "CRITICAL":
		            critical = fmtCnt;
		            break;
		        case "MAJOR":
		            major = fmtCnt;
		            break;
		        case "MINOR":
		            minor = fmtCnt;
		            break;
		    }
		}
		model.addAttribute("critical", critical);
		model.addAttribute("major", major);
		model.addAttribute("minor", minor);
    	
    	return "alarmmon/alarmmon_sys";
    }

 	// 알람이력 페이지 로딩
  	@RequestMapping(value="alarmmon_his.do", method=RequestMethod.POST) 
  	public String getAlarmHis(AlarmMonVo alarmMonVo, Model model, final HttpSession session) throws Exception {
     	
     	String comp_id = (String) session.getAttribute("comp_id");
     	alarmMonVo.setComp_id(comp_id);
 		
 		List<AlarmMonVo> getAlarmHisList = this.alarmMonService.getAlarmHisList(alarmMonVo);
 		model.addAttribute("getAlarmHisList", getAlarmHisList);

 		return "alarmmon/alarmmon_his";
     }
  	
	// 엑티브 로딩
  	@RequestMapping(value="alarmmon_active.do", method=RequestMethod.POST) 
  	public String getAlarmActive(AlarmMonVo alarmMonVo, Model model, final HttpSession session) throws Exception {
     	
  		String comp_id = (String) session.getAttribute("comp_id");
      	alarmMonVo.setComp_id(comp_id);
 		
 		List<AlarmMonVo> getAlarmActiveList = this.alarmMonService.getAlarmActiveList(alarmMonVo);
 		model.addAttribute("getAlarmActiveList", getAlarmActiveList);
     	
     	return "alarmmon/alarmmon_active";
     } 	
    
    // 가청 삭제
 	@ResponseBody
 	@RequestMapping(value="delSound.ajax", method=RequestMethod.POST) 
 	public HashMap<String, Object> delSound(AlarmMonVo alarmMonVo, Model model) throws Exception {
 		
 		HashMap<String, Object> resultMap = new HashMap<String, Object>();
 		
 		try {
	 		int delSound = this.alarmMonService.delSound(alarmMonVo);
	 		
	 		if(delSound > 0) {
	 			resultMap.put("result", "1");
	 		} else {
	 			resultMap.put("result", "0");
	 		}
 		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "0");
		}
 		return resultMap;
 	}
 	
 	// 알람 해제
  	@ResponseBody
  	@RequestMapping(value="delAlarm.ajax", method=RequestMethod.POST) 
  	public HashMap<String, Object> delAlarm(AlarmMonVo alarmMonVo, Model model, final HttpSession session) throws Exception {
  		
  		String user_id = (String) session.getAttribute("user_id");
  		alarmMonVo.setClr_user(user_id);
  		
  		HashMap<String, Object> resultMap = new HashMap<String, Object>();
  		
  		int result = 0;
  		
  		try {
  			
  			// alarm_flag 확인
  			int chkAlarm = this.alarmMonService.chkAlarm(alarmMonVo).size();	  			
 	 		
  			// 데이터가 존재하면
 	 		if(chkAlarm > 0) {
 	 			// 해당 알람은 업데이트
 	 			int updateAlarm = this.alarmMonService.updateAlarm(alarmMonVo);
 	 			
 	 			if(updateAlarm > 0) {
 	 				resultMap.put("result", "1");
 	 			} else {
 	 				resultMap.put("result", "0");
 	 			}	
 	 			
 	 		// 데이터가 존재하지 않는다면
 	 		} else { 			
 	 			// 해당 알람 추가
 	 			int insertAlarm = this.alarmMonService.insertAlarm(alarmMonVo);
 	 			
 	 			if(insertAlarm > 0) {
 	 				resultMap.put("result", "1");
 	 			} else {
 	 				resultMap.put("result", "0");
 	 			}
 	 		}
 	 		result = 1;
 	 		
 	 		// update 또는 insert 후 알람 이력에서 해당 알람 삭제 
 	 		if(result == 1) {
 	 			int deleteAlarm = this.alarmMonService.deleteAlarm(alarmMonVo);
 	 			
 	 			if(deleteAlarm > 0) {
 	 				resultMap.put("result", "1");
 	 			} else {
 	 				resultMap.put("result", "0");
 	 			}
 	 		} else {
 	 			resultMap.put("result", "0");
 	 		}
  		} catch (Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "0");
 		}
  		return resultMap;
  	}
  	
  	// 알람 로딩
  	@ResponseBody
   	@RequestMapping(value="getAlarm.ajax", method=RequestMethod.POST) 
   	public List<AlarmMonVo> getAlarm(AlarmMonVo alarmMonVo, Model model, final HttpSession session) throws Exception {
      	
   		String comp_id = (String) session.getAttribute("comp_id");
       	alarmMonVo.setComp_id(comp_id);
  		
       	List<AlarmMonVo> getSevList = this.alarmMonService.getSevList(alarmMonVo);
       	
       	// s_cnt가 100 이상일 경우 포맷  
       	for(AlarmMonVo vo : getSevList) {
            if(Integer.parseInt(vo.getS_cnt()) >= 100) {
                vo.setS_cnt("99+");
            }
        }
       	//logger.info("=====================================" + getSevList.get(0).getComp_id());
      	return getSevList;
    } 
  	
  	@ResponseBody
   	@RequestMapping(value="getSevSound.ajax", method=RequestMethod.POST) 
   	public List<AlarmMonVo> getSevSound(AlarmMonVo alarmMonVo, Model model, final HttpSession session) throws Exception {
      	
   		String comp_id = (String) session.getAttribute("comp_id");
       	alarmMonVo.setComp_id(comp_id);
  		
       	List<AlarmMonVo> getSevSound = this.alarmMonService.getSevSound(alarmMonVo);

       	for(AlarmMonVo vo : getSevSound) {
            vo.setComp_id(comp_id);         
        }      
      	return getSevSound;
  	} 

}
