package iwm_ko.login.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import iwm_ko.login.model.LoginVo;
import iwm_ko.login.recaptcha.RecaptchaConfig;
import iwm_ko.login.service.LoginService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import iwm_ko.util.PasswordUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
@PropertySource("classpath:config/application.properties")
public class LoginController {

	@Value("${recaptcha.secretKey}")
	private String secretKey;
	
	@Value("${recaptcha.sitekey}")
	private String recaptchaSiteKey;

//	private final static String mSalt = "apms";

	@Autowired
	private UserHistoryService userHistoryService;
	
	private final PasswordUtils passwordUtils;
	
    @Autowired
    public LoginController(PasswordUtils passwordUtils) {
        this.passwordUtils = passwordUtils;
    }

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="iwm_ko.login.loginService")
	private LoginService loginService;

	/**
	 * 로그인 페이지 로딩
	 */
	@RequestMapping("login.do")
	public String login(Model model) {
		model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
//		logger.info("recaptchaSiteKey ::::: " + recaptchaSiteKey);
		return "login/login";
	}


		@RequestMapping(value="loginProc.do", method = {RequestMethod.POST})
		public @ResponseBody Map<String, String> loginProc(@ModelAttribute("loginVo") LoginVo loginVo, HttpServletRequest request
				, @RequestParam(name = "g-recaptcha-response") String recaptchaResponse) throws Exception {
			Map<String, String> result = new HashMap<String, String>();
			HttpSession session = request.getSession();

			//  리캡차 검증
			try {
//				logger.info("secretKey ::::: " + secretKey);
//				logger.info("recaptchaResponse ::::: " + recaptchaResponse);
				RecaptchaConfig.setSecretKey(secretKey);
				Boolean verify = RecaptchaConfig.verify(recaptchaResponse);
//				logger.info("verify ::::: " + verify);
				// 검증 실패 시
				if(!verify){
					result.put("code", "recaptcha error");

					return result;
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			loginVo.setPasswd256(passwordUtils.encodineSha256(loginVo.getPasswd()));

			// ID, Password check 및 보안 필터 적용
			String userid = loginVo.getUser_id().replace("'","").replace("\"","").replace("-","").replace("(","").replace(")","").replace("<","").replace(">","");
			String userpwd = loginVo.getPasswd().replace("'","").replace("\"","").replace("-","").replace("(","").replace(")","").replace("<","").replace(">","");

			loginVo.setUser_id(userid);
			loginVo.setPasswd(userpwd);

			LoginVo checkVo = loginService.selectAuthorizedUser(loginVo);

			// 로그인 성공 시
			if(checkVo != null && checkVo.getUser_id() != null && !checkVo.getUser_id().isEmpty()) {
				// 성공 로직
				String ipAddr = request.getHeader("X-Real-IP");
				if (ipAddr == null) {
					ipAddr = request.getRemoteAddr();
				}

				//session에 회원 정보 저장
				session.setAttribute("user_id", checkVo.getUser_id());
				session.setAttribute("user_name", checkVo.getUser_name());
				session.setAttribute("user_grade", checkVo.getUser_grade());
				session.setAttribute("grade_name", checkVo.getGname());
				session.setAttribute("comp_id", checkVo.getComp_id());
				session.setAttribute("ipAddr", ipAddr);
				session.setAttribute("pwdChangeCheck", true); // session에 pwd 변경일 확인 값 넣었음. true는 기본 값


				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("login");
				userHistory.setJob_name("로그인");
				userHistory.setGui_remark("ID : " + session.getAttribute("user_id") +", IP: " + session.getAttribute("ipAddr") );
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str("select USER_ID, PASSWD, USER_NAME, COMP_ID, USER_GRADE, TB.GNAME GNAME"+
						" from TB_USER_INFO_CENTER TA, TB_GRADE_INFO TB "+
						"where TA.USER_GRADE=TB.GRADE and USER_ID=  " + loginVo.getUser_id() +" and ( PASSWD=SHA1(" + loginVo.getPasswd() + " ) or PASSWD=(" + loginVo.getPasswd()+"))"+
						 " and DEL_FLAG='N'");

				userHistoryService.insertUserHistory(userHistory);
				result.put("code", "SUCCESS");
				
				// 비밀번호 변경 확인
				int userPwdLimitDays = getUserPassLimit();
				
				LocalDate userPwdChangeDate = getUserPassChangeDate(session.getAttribute("user_id").toString()); // 비밀번호 마지막 변경 일자
				session.setAttribute("pwdLimitDays", Integer.toString(userPwdLimitDays)); // 비밀 번호 변경 제한 일

				// null or 경과 일 때 monitor에 넘어가서 alert 발생을 위해서
				// session에 값을 넣어 둠. 
				if (userPwdChangeDate == null) { // 초기 데이터인 null이면
				    session.setAttribute("pwdChangeCheck", "false"); 
				} else { // 값이 있으면 계산
				    LocalDate currentDate = LocalDate.now();
				    long limitOverDays = ChronoUnit.DAYS.between(userPwdChangeDate, currentDate);
//				    logger.info("Days since last password change ::::: " + limitOverDays);

				    if (limitOverDays >= userPwdLimitDays) {
				        session.setAttribute("pwdChangeCheck", "false");
				    } else {
				        session.setAttribute("pwdChangeCheck", "true");
				    }
				}

				return result;
			}

			// 로그인 실패 처리
			int chkId = loginService.checkId(loginVo).size();
			int lockCnt = loginService.getLockCount();

			if(chkId > 0) {
				loginService.updateFailCount(userid);
				int failCnt = loginService.getFailCount(userid);

				if(lockCnt <= failCnt) {
					result.put("code", "ACCOUNT_LOCKED");
					result.put("message", lockCnt + "회 이상 로그인에 실패하여 계정이 잠금처리 되었습니다.");
				} else {
					result.put("code", "LOGIN_FAILED");
					result.put("message", "로그인에 실패하였습니다(" + failCnt + "/" + lockCnt + "회). 아이디와 비밀번호를 확인해주세요. Caps Lock 키가 켜져있는 경우 꺼주십시오.");
				}
			} else {
				loginService.insertFailCount(userid);
				result.put("code", "LOGIN_FAILED_FIRST_TIME");
				result.put("message", "로그인에 실패하였습니다. 아이디와 비밀번호를 확인해주세요. Caps Lock 키가 켜져있는 경우 꺼주십시오.");
			}

			return result;
		}
		
		// 로그인한 유저의 PWD_CHANGE_DATE get
		private LocalDate getUserPassChangeDate(String user_id) throws Exception {
		    LocalDate date = null;

		    String getUserPwdChangeDate = this.loginService.getUserPassChangeDate(user_id);

		    if (getUserPwdChangeDate != null && !getUserPwdChangeDate.isEmpty()) {
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		        LocalDateTime dateTime = LocalDateTime.parse(getUserPwdChangeDate, formatter);
		        date = dateTime.toLocalDate(); // LocalDateTime에서 LocalDate로 변환
		    }
		    
//		    logger.info("date ::::: " + date);
		    return date;
		}

		// limit_days get
		private int getUserPassLimit() throws Exception {
			
			int result = 0;
			
			try {
		        List<LoginVo> getUserPassLimit = this.loginService.getUserPassLimit();

		        if (!getUserPassLimit.isEmpty()) { 
		            result = getUserPassLimit.get(0).getLimit_days(); 
		        }
		    } catch (Exception e) {  
//		    	logger.error("getUserPassLimit() 실행 중 예외 발생", e);
		        e.printStackTrace();
		    }

			return result;
		}
		
		

//	public String encodineSha256(String str) {
//		String result = "";
//
//		byte[] strByte = str.getBytes();
//		byte[] salt = mSalt.getBytes();
//		byte[] bytes = new byte[strByte.length + salt.length];
//
//		System.arraycopy(strByte, 0, bytes, 0, strByte.length);
//		System.arraycopy(salt, 0, bytes, strByte.length, salt.length);
//
//
//		try {
//			MessageDigest md = MessageDigest.getInstance("SHA-256");
//			// md.update((str).getBytes());
//			md.update(bytes);
//
//			byte[] bytedata = md.digest();
//			StringBuffer sb = new StringBuffer();
//
//
//			for(byte b : bytedata) {
//				sb.append(String.format("%02x", b));
//			}
//
//			result = sb.toString();
//
//		}
//		catch (NoSuchAlgorithmException e) {
//			throw new RuntimeException(e);
//		}
//		return result;
//
//	}

	@RequestMapping(value="logout.do", method=RequestMethod.GET)
	public String logout(HttpSession session) throws Exception {


		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("logout");
		userHistory.setJob_name("로그아웃");
		userHistory.setGui_remark("ID : " + session.getAttribute("user_id"));
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");

		userHistoryService.insertUserHistory(userHistory);

		session.invalidate();
		return "redirect:/login/login.do";
	}


		public class SHA256Hashing {
			public static String encodeSha256(String input) {
				try {
					MessageDigest md = MessageDigest.getInstance("SHA-256");
					byte[] hashedBytes = md.digest(input.getBytes("UTF-8"));

					// 바이트 배열을 16진수 문자열로 변환
					StringBuilder hexString = new StringBuilder();
					for (byte b : hashedBytes) {
						String hex = Integer.toHexString(0xff & b);
						if (hex.length() == 1) {
							hexString.append('0');
						}
						hexString.append(hex);
					}
					return hexString.toString();
				} catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
		}
}
