package apms.login.controller;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import apms.login.model.LoginVo;
import apms.login.service.LoginService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="apms.login.loginService")
	private LoginService loginService;
	
	/**
	 * 로그인 페이지 로딩
	 */
	@RequestMapping("login.do")
	public String login() {
		return "login/login";
	}
	
	/**
	 * 로그인 확인
	 */
	@RequestMapping(value="loginProc.do", method = {RequestMethod.POST})
	public @ResponseBody Map<String, String> loginProc(@ModelAttribute("loginVo") LoginVo loginVo, HttpServletRequest request, Model model) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		HttpSession session = request.getSession();
		
		if(null != loginVo) {
			//ID, Password check
			LoginVo checkVo = this.loginService.selectAuthorizedUser(loginVo);
			if(null != checkVo && null != checkVo.getUser_id() && checkVo.getUser_id().length() > 0) {
				result.put("code", "SUCCESS");
				
				//session에 회원 정보 저장
				session.setAttribute("user_id", checkVo.getUser_id());
				session.setAttribute("user_name", checkVo.getUser_name());
				session.setAttribute("user_grade", checkVo.getUser_grade());
				session.setAttribute("grade_name", checkVo.getUser_name());
				session.setAttribute("scode", checkVo.getScode());
				
			}else {
				result.put("code", "ERROR");
			}
		}else {
			result.put("code", "ERROR");
		}
		
		return result;
	}
}
