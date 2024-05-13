package radius.login.controller;

import java.util.HashMap;
import java.util.Map;

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
import radius.login.model.LoginVo;
import radius.login.service.LoginService;


@Controller
@RequestMapping("/")
public class LoginController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="radius.login.loginService")
	private LoginService loginService;
	
	/**
	 * 로그인 페이지 로딩
	 */
	@RequestMapping("/")
	public String login(HttpServletRequest request, HttpServletResponse response
			, HttpSession session, LoginVo loginVo) throws Exception {

		String sessionId = (String)request.getSession().getAttribute("user_id");
		
		if(sessionId != null) {
			response.sendRedirect("chart/chart.do");
		}
		
		return "login/login";
	}
	
	/**
	 * 로그인 확인
	 */
	@RequestMapping(value="loginProc.do", method = {RequestMethod.POST})
	public @ResponseBody Map<String, String> loginProc(@ModelAttribute("loginVo") LoginVo loginVo, Model model
			, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, String> result = new HashMap<String, String>();
		HttpSession session = request.getSession();
		
		if(null != loginVo) {
			
			//ID, Password check
			LoginVo checkVo = this.loginService.selectAuthorizedUser(loginVo);
			
			if(null != checkVo && null != checkVo.getUserId() && checkVo.getUserId().length() > 0) {
				result.put("code", "SUCCESS");
				
				//session에 회원 정보 저장
				session.setAttribute("user_id", checkVo.getUserId());
				session.setAttribute("user_name", checkVo.getUserName());
				
				if((String)session.getAttribute("user_id") == null) {
					response.sendRedirect("/");
				} 
			} else {
				result.put("code", "ERROR");
				}
		} else {
			result.put("code", "ERROR");
		}
		return result;	
	}
	
	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception {
		
		session.invalidate();
		
		return "redirect:/";
		
	}
	
}
