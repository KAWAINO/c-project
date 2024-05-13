package iwm_ko.enableTime.controller;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import iwm_ko.comm.b15p10.paging_10;
import iwm_ko.cuser.model.CuserVo;
import iwm_ko.enableTime.model.EnableTimeVo;
import iwm_ko.enableTime.service.EnableTimeService;
import iwm_ko.shipInfo.service.ShipInfoService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/enabletime")
public class EnableTimeController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="iwm_ko.enableTime.enableTimeService")
	private EnableTimeService enableTimeService;
	
	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;
	
	@Resource(name="iwm_ko.shipInfo.ShipInfoService")
	private ShipInfoService shipInfoService;
	
	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/enabletime.do")
	public String enableTime(@ModelAttribute("enableTimeVo") EnableTimeVo enableTimeVo, Model model, HttpSession session) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			enableTimeVo.setScomp(scomp);
		}

		enableTimeVo.setStartrow((enableTimeVo.getPageno() -1) * enableTimeVo.getFetchrow());
		
		// comp 리스트 조회
		List<String> compList = this.shipInfoService.selectCompList(scomp);
		model.addAttribute("compList", compList);
		
		List<EnableTimeVo> enableTimeList = this.enableTimeService.selectEnableTimeList(enableTimeVo);
		model.addAttribute("enableTimeList", enableTimeList);
		
		// 페이징
		paging_10 paging = new paging_10();
		
		int totalCnt = this.enableTimeService.totalEnableTimeList(enableTimeVo);
		
		String pagingHTML = paging.getPagingStr(enableTimeVo.getPageno(), enableTimeVo.getFetchrow() , totalCnt);
		
		model.addAttribute("pagingHTML", pagingHTML);
		model.addAttribute("enableTimeVo", enableTimeVo);
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("407");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);

		return "enabletime/enabletime";
	}
	
	/*
	 *  등록
	 */
	@ResponseBody
	@RequestMapping(value="enableTimeAdd.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> enableTimeAdd(EnableTimeVo enableTimeVo, HttpServletRequest request, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
		
			enableTimeVo.setAddFromTime(request.getParameter("addFromTime"));
			enableTimeVo.setAddToTime(request.getParameter("addToTime"));
			
			// FROM to TO time check
			String overlapTime = this.enableTimeService.getOverlabTime(enableTimeVo);

			if(overlapTime.contains("FALSE")) {
				resultMap.put("result", "-2");
			} else {
				int insertTime = this.enableTimeService.insertTime(enableTimeVo);
				
				if(insertTime > 0) {
					resultMap.put("result", "1");
					
					// 로그 기록
					UserHistoryVo userHistory = new UserHistoryVo();
					userHistory.setGui_code("407");
					userHistory.setJob_name("추가");
					userHistory.setGui_remark("from time : " + enableTimeVo.getAddFromTime() + "\n  to time : " + enableTimeVo.getAddToTime() 
												+ ", Ship Code : " + enableTimeVo.getAddShipName());
					userHistory.setUser_id((String) session.getAttribute("user_id"));
					userHistory.setSql_str("INSERT INTO TB_WIFI_ENABLE_TIME\r\n"
							+ "		(\r\n"
							+ "			S_CODE\r\n"
							+ "		  , FROM_TIME\r\n"
							+ "		  , TO_TIME\r\n"
							+ "		  , DESCR\r\n"
							+ "		  , MODIFY_DATE\r\n"
							+ "		  , GUI_FLAG\r\n"
							+ "		)\r\n"
							+ "		VALUES\r\n"
							+ "		(\r\n"
							+ "		   '"+ enableTimeVo.getAddShipName() +"'\r\n"
							+ "		  ,'"+ enableTimeVo.getAddFromTime() +"'\r\n"
							+ "		  ,'"+ enableTimeVo.getAddToTime() +"'\r\n"
							+ "		  ,'"+ enableTimeVo.getAddDescr() +"'\r\n"
							+ "		  , NOW()\r\n"
							+ "		  , 'Y'\r\n"
							+ "		)");
					this.userHistoryService.insertUserHistory(userHistory);
				} else {
					resultMap.put("result", "0");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "0");
		}
		return resultMap;
	}
	
	/*
	 * 수정모달 이동
	 */
	@ResponseBody
	@RequestMapping(value="enableTimeUpdateData.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> enableTimeUpdateData(@RequestParam HashMap<String, Object> req, Model model
			, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			EnableTimeVo enableTimeInfo = this.enableTimeService.enableTimeInfo(req);

			resultMap.put("enableTimeInfo", enableTimeInfo);
			resultMap.put("result", "success");		
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "error");
		}
		return resultMap;
	}
	
	/*
	 *  삭제
	 */
	@ResponseBody
	@RequestMapping(value="enableTimeDelete.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> enableTimeDelete(EnableTimeVo enableTimeVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			int deleteEnableTime = this.enableTimeService.deleteEnableTime(enableTimeVo);
			
			if(deleteEnableTime > 0) {
				resultMap.put("result", "1");
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("407");
				userHistory.setJob_name("삭제");
				userHistory.setGui_remark("from time : " + enableTimeVo.getFrom_time() + "\n  to time : " + enableTimeVo.getTo_time());
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str("DELETE FROM TB_WIFI_ENABLE_TIME\r\n"
						+ "		 WHERE FROM_TIME = '"+ enableTimeVo.getFrom_time() +"'");
				this.userHistoryService.insertUserHistory(userHistory);
			} else {
				resultMap.put("result", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "0");
		}
		return resultMap;
	}
}
