package iwm_ko.sauth.controller;

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

import iwm_ko.apinfo.model.ApInfoVo;
import iwm_ko.apinfo.service.ApInfoService;
import iwm_ko.sauth.model.SauthVo;
import iwm_ko.sauth.service.SauthService;
import iwm_ko.shipInfo.service.ShipInfoService;
import iwm_ko.suser.model.SuserVo;
import iwm_ko.suser.service.SuserService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/sauth")
public class SauthController {

private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="iwm_ko.sauth.sauthService")
	private SauthService sauthService;
	
	@Resource(name="iwm_ko.shipInfo.ShipInfoService")
	private ShipInfoService shipInfoService;
	
	@Resource(name="iwm_ko.suser.suserService")
	private SuserService suserService;
	
	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;
	
	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/sauth.do")
	public String sauth(@ModelAttribute("sauthVo") SauthVo sauthVo, Model model, HttpSession session
			, HttpServletRequest request) throws Exception {

		String scomp = (String)session.getAttribute("comp_id");	
		
		// compList 조회
		List<String> compList = this.shipInfoService.selectCompList(scomp);
		model.addAttribute("compList", compList);

		SuserVo suserVo = new SuserVo();
		
		// grade 리스트 조회
		List<SuserVo> gradeList = this.suserService.selectGradeList(suserVo);
		model.addAttribute("gradeList", gradeList);
		
		// cauth.do 페이지 접속 시 리스트 출력을 위한 grade set
		if(request.getParameter("searchGrade") == null) {	
			sauthVo.setSearchGrade("1");		
		} 

		// 등급별 리스트 조회
		List<SauthVo> searchList = this.sauthService.selectSearchList(sauthVo);
		model.addAttribute("searchList", searchList);
		
		// 선박명 selected 유지를 위해 s_code 저장
		model.addAttribute("s_code", sauthVo.getSearchScode());
//		logger.info("searchScode ::::: " + sauthVo.getSearchScode());
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("705");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);
	
		return "sauth/sauth";
	}	

	/**
	 * 등급에 따른 권한 수정
	 */
	@ResponseBody
	@RequestMapping(value="gradeUpdate.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> gradeUpdate(SauthVo sauthVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {		

			// useFlag selected 값
			String[] arr1 = sauthVo.getUpdUseFlag().split(",");
			String[] arr2 = sauthVo.getUpdGuiCode().split(",");
			
			for(int i=0; i<arr1.length; i++) {
				
				// 쿼리 실행을 위한 substring 작업
				String useFlag = arr1[i].substring(0, 1).toString();
				String guiCode = arr2[i].substring(2, 5).toString();
				
				// 작업한 값 set
				sauthVo.setUpdUseFlag(useFlag);
				sauthVo.setUpdGuiCode(guiCode);
			
				// update
				int updResult = this.sauthService.updateGrade(sauthVo);
			
				if(updResult > 0) {
					resultMap.put("result", "1");

				} else {
					resultMap.put("result", "0");
				}

			}
		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "0");
 		}	
		
		// grade 값에 따른 등급명 set
		String grade = sauthVo.getSearchGrade();
		String gname = "";
		if(grade.equals("1")) {
			gname = "Admin";
		} else if(grade.equals("2")) {
			gname = "Manager";
		} else {
			gname = "User";
		}
		logger.info("gname ::::: " + gname);
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("704");
		userHistory.setJob_name("수정");
		userHistory.setGui_remark(gname + " Grade Modify");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);
		
		return resultMap;
	}
}
