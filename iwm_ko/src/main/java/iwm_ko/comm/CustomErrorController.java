package iwm_ko.comm;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {
	
	@RequestMapping("error.do")
	public String setError(HttpSession session, Model model) {
		
		// 세션에서 에러 메시지를 가져와 모델에 추가
        String errorMessage = (String) session.getAttribute("errorMessage");
        model.addAttribute("errorMessage", errorMessage);
        // 에러 메시지 세션에서 제거
        session.removeAttribute("errorMessage");	
        
		return  "error/error"; 
	}
}
