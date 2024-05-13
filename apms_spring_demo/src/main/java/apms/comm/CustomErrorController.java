package apms.comm;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
	@RequestMapping("/error/error.do")
	public String setError() {
		//////////error 시 수행할 작업 작성(ex. session delete, error 별 페이지 분리)
		
		return  "error"; 
	}
}
