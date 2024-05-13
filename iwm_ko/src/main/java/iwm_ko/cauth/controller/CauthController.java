package iwm_ko.cauth.controller;

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

import iwm_ko.cauth.model.CauthVo;
import iwm_ko.cauth.service.CauthService;
import iwm_ko.cuser.model.CuserVo;
import iwm_ko.cuser.service.CuserService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cauth")
public class CauthController {

private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="iwm_ko.cauth.cauthService")
	private CauthService cauthService;
	
	@Resource(name="iwm_ko.cuser.cuserService")
	private CuserService cuserService;
	
	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;

	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/cauth.do")
	public String cauth(@ModelAttribute("cauthVo") CauthVo cauthVo, Model model, HttpSession session
			, HttpServletRequest request) throws Exception {

		// cauth.do 페이지 접속 시 리스트 출력을 위한 grade set
		if(request.getParameter("searchGrade") == null) {	
			cauthVo.setSearchGrade("1");		
		}
					
		// 운영자 등급 조회
		CuserVo cuserVo = new CuserVo();
		List<CuserVo> gradeList = this.cuserService.selectGradeList(cuserVo);
		model.addAttribute("gradeList", gradeList);
		
		// 등급별 리스트 조회
		List<CauthVo> searchList = this.cauthService.selectSearchList(cauthVo);
		model.addAttribute("searchList", searchList);
				
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("702");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);

		return "cauth/cauth";
	}
	
	/**
	 * 등급에 따른 권한 수정
	 */
	@ResponseBody
	@RequestMapping(value="gradeUpdate.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> gradeUpdate(CauthVo cauthVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			// 리스트 카운트
			int cnt = this.cauthService.listCnt(cauthVo).size();

			// useFlag selected 값
			String[] arr1 = cauthVo.getUpdUseFlag().split(",");
			String[] arr2 = cauthVo.getUpdGuiCode().split(",");
			
			for(int i=0; i<arr1.length; i++) {
				
				// 쿼리 실행을 위한 substring 작업
				String useFlag = arr1[i].substring(0, 1).toString();
				String guiCode = arr2[i].substring(2, 5).toString();
				
				// 작업한 값 set
				cauthVo.setUpdUseFlag(useFlag);
				cauthVo.setUpdGuiCode(guiCode);
			
				// update
				int updResult = this.cauthService.updateGrade(cauthVo);
			
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
		String grade = cauthVo.getSearchGrade();
		logger.info("grade ::::: " + grade);
		String gname = "";
		if(grade.equals("1")) {
			gname = "Admin";
		} else if(grade.equals("2")) {
			gname = "Manager";
		} else {
			gname = "User";
		}
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("702");
		userHistory.setJob_name("수정");
		userHistory.setGui_remark(gname + " Grade Modify");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("UPDATE TB_GUI_GRADE_CENTER SET\r\n"
				+ "			   USE_FLAG = '"+ cauthVo.getUpdUseFlag() + "'\r\n"
				+ "			 , GUI_FLAG = 'Y'\r\n"
				+ "		 WHERE GUI_CODE = '"+ cauthVo.getUpdGuiCode() + "'\r\n"
				+ "		   AND GRADE = '"+ cauthVo.getSearchGrade() + "'\r\n");
		this.userHistoryService.insertUserHistory(userHistory);
		
		return resultMap;
	}	
	
}
