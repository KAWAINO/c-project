package radius.chart.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import radius.chart.model.ChartVo;
import radius.chart.service.ChartService;

@Controller
@RequestMapping("/chart")
public class ChartController {
	
private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "radius.chart.chartService")
	private ChartService chartService;

	@RequestMapping("chart.do")
	public String chart(@ModelAttribute("chartVo") ChartVo chartVo,
			HttpServletRequest request, final HttpSession session, HttpServletResponse response, Model model) throws Exception {

		if((String)session.getAttribute("user_id") == null || (String)session.getAttribute("user_id") == "") {
			
			response.sendRedirect("/");
			
		} else {	
			
			chartVo.setStartrow((chartVo.getPageno() -1) * chartVo.getFetchrow());
			
		}
		return "chart/chart";
	}
	
	/**
	 * 인증시도추이 리스트
	 */
	@ResponseBody
	@RequestMapping(value = "accessCnt.ajax", method = RequestMethod.POST)
	public List<ChartVo> accessCnt(@ModelAttribute("chartVo") ChartVo chartVo, Model model, HttpServletRequest request) throws Exception {

		List<ChartVo> accessCnt = this.chartService.selectAccessCntList(chartVo);	
			
		model.addAttribute("accessCnt", accessCnt);

		return accessCnt;
	}
	
	/**
	 * 금일누적량 리스트
	 */
	@ResponseBody
	@RequestMapping(value = "accCnt.ajax", method = RequestMethod.POST)
	public List<ChartVo> accCnt(ChartVo chartVo, Model model, HttpServletRequest request) throws Exception {

		List<ChartVo> accCnt = this.chartService.selectAccCntList(chartVo);				
		
		model.addAttribute("accCnt", accCnt);
		
		return accCnt;
	}
	
	/**
	 * 시간별 통계 데이터
	 */
	@ResponseBody
	@RequestMapping(value = "accTable.ajax", method = RequestMethod.POST)
	public List<ChartVo> accTable(ChartVo chartVo, Model model, HttpServletRequest request) throws Exception {
		
		List<ChartVo> accTable = this.chartService.selectAccTableList(chartVo);	
		
		model.addAttribute("accTable", accTable);
		
		return accTable;
	}
}
