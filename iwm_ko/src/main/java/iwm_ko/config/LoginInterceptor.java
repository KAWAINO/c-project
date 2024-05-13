package iwm_ko.config;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import iwm_ko.login.model.LoginVo;
import iwm_ko.login.service.LoginService;

@Component
public class LoginInterceptor implements HandlerInterceptor {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="iwm_ko.login.loginService")
	private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response
    		, Object handler) throws Exception {
    	
        HttpSession session = request.getSession();
        
        String language = (String)session.getAttribute("language");
        String scomp = (String)session.getAttribute("comp_id");
        
        // url 정보
        String url = request.getRequestURI();
        
        // "user_id" 세션 키를 체크하여 로그인 여부를 확인
        if (session.getAttribute("user_id") == null) {
            // 로그인 상태가 아니라면 로그인 페이지로 리다이렉트
            response.sendRedirect(request.getContextPath() + "/login/login.do");
            return false;
        }
        
        LoginVo vo = new LoginVo();
        
        // 사용자 권한 정보 
        vo.setGrade((String) session.getAttribute("user_grade"));        
        
        String[] urlArr = url.split("/");
        String groupCode = urlArr[1];
        vo.setGroupCode(urlArr[1]);   
        
//		logger.info("groupCode ::::: " + groupCode);        
//		logger.info("    scomp ::::: " + scomp);        

		if((!groupCode.equals("monitor") && !groupCode.equals("autoupdate") && !groupCode.equals("crewstat"))
		        || (!scomp.equals("0") && groupCode.equals("alarmmon"))){
        	
        	Boolean checkMenu = this.loginService.checkMenu(vo);

        	// comp_id가 0임에도 로직이 실행돼서 checkMenu의 결과가 null 발생하므로 해당 로직 추가하였음
        	if(checkMenu == null && scomp.equals("0")) {
        		checkMenu = true;
        	} else if(checkMenu == null && !scomp.equals("0")){
//        		logger.info(" checkMenu ::::: " + checkMenu);        
//        		logger.info("groupCode2 ::::: " + groupCode);        
//        		logger.info("    scomp2 ::::: " + scomp);    
        		checkMenu = false;
        	}
        	
        	if(false == checkMenu) {
        		if(language.equals("korean")) {
	        		// 에러 메시지를 세션에 저장
	                session.setAttribute("errorMessage", "이 페이지를 볼 수 있는 권한이 없습니다.");
	                // 에러 페이지로 리다이렉트
	                response.sendRedirect(request.getContextPath() + "/error/error.do");
	                return false;
        		} else {
	                session.setAttribute("errorMessage", "Do not have permission to view this page.");
	                response.sendRedirect(request.getContextPath() + "/error/error.do");
	                return false;
        		}
        	}       	  	
        }
		
//		logger.info("groupCode ::::: " + groupCode);
		
		// 기존 여객선 페이지 접근 시 로그인페이지로 이동
		if(groupCode.equals("passinfo") || groupCode.equals("passcdr") || groupCode.equals("passwap")
				|| groupCode.equals("passstat") || groupCode.equals("passrange")) {
			
			response.sendRedirect(request.getContextPath() + "/login/login.do");
			return false;
		}
		
        return true;
    }
}
