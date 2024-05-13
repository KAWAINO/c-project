package iwm_ko.crewauth.controller;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import iwm_ko.comm.b20p10.paging_10;
import iwm_ko.crewauth.model.CrewAuthVo;
import iwm_ko.crewauth.service.CrewAuthService;
import iwm_ko.shipInfo.service.ShipInfoService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/crewauth")
public class CrewAuthController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="iwm_ko.crewauth.crewAuthService")
	private CrewAuthService crewAuthService;
	
	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;
	
	@Resource(name="iwm_ko.shipInfo.ShipInfoService")
	private ShipInfoService shipInfoService;
	
	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/crewauth.do")
	public String slog(@ModelAttribute("crewAuthVo") CrewAuthVo crewAuthVo, Model model
			, HttpSession session, Locale locale) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			crewAuthVo.setScomp(scomp);
		}

		crewAuthVo.setStartrow((crewAuthVo.getPageno() -1) * crewAuthVo.getFetchrow());
		
		if("true".equals(crewAuthVo.getSearchChked())) {
			crewAuthService.searchFormatDate(crewAuthVo);
			model.addAttribute("defaultConStr", crewAuthVo.getConStartDefaultDate());
			model.addAttribute("defaultConEnd", crewAuthVo.getConEndDefaultDate());
			model.addAttribute("defaultDisconStr", crewAuthVo.getDisconStartDefaultDate());
			model.addAttribute("defaultDisconEnd", crewAuthVo.getDisconEndDefaultDAte());
		} else {
			crewAuthService.defaultFormatDate(crewAuthVo);
			model.addAttribute("defaultConStr", crewAuthVo.getDefaultDate());
			model.addAttribute("defaultConEnd", crewAuthVo.getDefaultDate());
			model.addAttribute("defaultDisconStr", crewAuthVo.getDefaultDate());
			model.addAttribute("defaultDisconEnd", crewAuthVo.getDefaultDate());
		}
//			logger.info("====================================== conn : " + crewAuthVo.getConn());
//			logger.info("====================================== disconn : " + crewAuthVo.getDisconn());

		// 페이지 로딩 리스트
		List<CrewAuthVo> crewAuthList = this.crewAuthService.selectCrewAuthList(crewAuthVo, locale);
		model.addAttribute("crewAuthList", crewAuthList);

		// comp 리스트 조회
		List<String> compList = this.shipInfoService.selectCompList(scomp);
		model.addAttribute("compList", compList);
		
		model.addAttribute("s_code", crewAuthVo.getSearchShipName());
		model.addAttribute("conn", crewAuthVo.getConn());
		model.addAttribute("disconn", crewAuthVo.getDisconn());
		
		// 페이징
		paging_10 paging = new paging_10();
		
		int totalCnt = this.crewAuthService.totalCrewAuthList(crewAuthVo);
		
		String pagingHTML = paging.getPagingStr(crewAuthVo.getPageno(), crewAuthVo.getFetchrow() , totalCnt);
		
		model.addAttribute("pagingHTML", pagingHTML);
		model.addAttribute("crewAuthVo", crewAuthVo);
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("404");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);

		return "crewauth/crewauth";
	}
}
