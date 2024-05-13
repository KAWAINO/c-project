package iwm_ko.cuser.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import iwm_ko.comm.paging;
import iwm_ko.cuser.model.CuserVo;
import iwm_ko.cuser.service.CuserService;
import iwm_ko.shipInfo.service.ShipInfoService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import iwm_ko.util.PasswordUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cuser")
public class CuserController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private MessageSource messageSource;
	
	@Autowired
	private PasswordUtils passwordUtils;
	
	@Resource(name="iwm_ko.cuser.cuserService")
	private CuserService cuserService;
	
	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;

//	@Resource(name="iwm_ko.shipInfo.shipInfoService")
//	private ShipInfoService shipInfoService;
	
	@Resource(name = "iwm_ko.shipInfo.ShipInfoService")
    private ShipInfoService shipInfoService;
	
	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/cuser.do")
	public String cuser(@ModelAttribute("cuserVo") CuserVo cuserVo, Model model, HttpSession session, Locale locale) throws Exception {

		String scomp = (String)session.getAttribute("comp_id");
		if(!scomp.equals("0")) {
			cuserVo.setScomp(scomp);
		}
		
		cuserVo.setStartrow((cuserVo.getPageno() -1) * cuserVo.getFetchrow());
		
		// grade 리스트 조회
		List<CuserVo> gradeList = this.cuserService.selectGradeList(cuserVo);
		model.addAttribute("gradeList", gradeList);
		
		// compList 조회
		List<String> compList = this.shipInfoService.selectCompList(scomp);
		model.addAttribute("compList", compList);
				
		// iwm 운영자 리스트 조회
		List<CuserVo> cuserList = this.cuserService.selectCuserList(cuserVo);
		model.addAttribute("cuserList", cuserList);
		
		// message
//		model.addAttribute("msg1", messageSource.getMessage("cuser, null, locale)) 
//		logger.info("user_id ::::: " + (String)session.getAttribute("user_id"));
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("701");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String)session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);

		// 페이징
		paging paging = new paging();
		
		int totalCnt = this.cuserService.totalCuserList(cuserVo);
		
		String pagingHTML = paging.getPagingStr(cuserVo.getPageno(), cuserVo.getFetchrow() , totalCnt);
		
		model.addAttribute("pagingHTML", pagingHTML);
		model.addAttribute("cuserVo", cuserVo);		
	
		return "cuser/cuser";
	}
	
	/*
	 * iwm 운영자 등록
	 */
	@ResponseBody
	@RequestMapping(value="cuserAdd.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> cuserAdd(CuserVo cuserVo, HttpSession session) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			//중복검사
			int chkId = this.cuserService.chkInsertCuser(cuserVo).size();
			
			if(chkId > 0) {
				resultMap.put("result", "-2");
			} else {

				// del_flag 체크
				int chkFlag = this.cuserService.chkDelFlag(cuserVo).size();
//				logger.info("================================= chkFlag : "+chkFlag);
				if(chkFlag > 0) {
					
					// password encoding
					String password = cuserVo.getAddPassWd();
					String shaPassword = passwordUtils.encodineSha256(password);
					cuserVo.setAddPassWd(shaPassword);
//					logger.info("password ::::: " + shaPassword);
					
					// 기존 유저 수정
					int changeUser = this.cuserService.changeUser(cuserVo);
						changeUser += this.cuserService.insertFailCnt(cuserVo);
						
					if(changeUser > 0) {					
						
						// 로그 기록
//						String sql = xmlQueryService.extractSqlFromXml("cuser", "changeUser");
						UserHistoryVo userHistory = new UserHistoryVo();
						userHistory.setGui_code("701");
						userHistory.setJob_name("추가");
						userHistory.setGui_remark("Operator ID : " + cuserVo.getAddUserId() +", Operator Name : " + cuserVo.getAddUserName());
						userHistory.setUser_id((String) session.getAttribute("user_id"));
						userHistory.setSql_str("UPDATE TB_USER_INFO_CENTER SET\r\n"
								+ "			   PASSWD ='"+ cuserVo.getAddPassWd() +"'\r\n"
								+ "			 , USER_NAME ='"+ cuserVo.getAddUserName() +"'\r\n"
								+ "		     , USER_GRADE ='"+ cuserVo.getAddUserGrade() +"'\r\n"
								+ "		     , TEAM_NAME = ''\r\n"
								+ "		     , PHONE = ''\r\n"
								+ "		     , MOBILE = ''\r\n"
								+ "		     , EMAIL = ''\r\n"
								+ "		     , COMP_ID ='"+ cuserVo.getAddCompId() +"'\r\n"
								+ "		     , DEL_FLAG = 'N'\r\n"
								+ "		     , MODIFY_DATE = NOW()\r\n"
								+ "		     , GUI_FLAG = 'Y'\r\n"
								+ "		     , PWD_CHANGE_DATE = NOW()\r\n"
								+ "		 WHERE USER_ID ='"+ cuserVo.getAddUserId() +"'");
						this.userHistoryService.insertUserHistory(userHistory);
						
						resultMap.put("result", "1");
					} else {
						resultMap.put("result", "0");
					}
				} else {
					
					// password encoding
					String password = cuserVo.getAddPassWd();
					String shaPassword = passwordUtils.encodineSha256(password);
					cuserVo.setAddPassWd(shaPassword);
				
					//운영자 신규 등록
					int insResult = this.cuserService.insertCuser(cuserVo);
						insResult += this.cuserService.insertFailCnt(cuserVo);
					
					if(insResult > 0) {
	
						// 로그 기록
	//					String sql = xmlQueryService.extractSqlFromXml("cuser", "changeUser");
						UserHistoryVo userHistory = new UserHistoryVo();
						userHistory.setGui_code("701");
						userHistory.setJob_name("추가");
						userHistory.setGui_remark("Operator ID : " + cuserVo.getAddUserId() +", Operator Name : " + cuserVo.getAddUserName());
						userHistory.setUser_id((String) session.getAttribute("user_id"));
						userHistory.setSql_str("INSERT INTO TB_USER_INFO_CENTER \r\n"
								+ "		(\r\n"
								+ "			USER_ID, PASSWD, USER_NAME, USER_GRADE, TEAM_NAME, \r\n"
								+ "			PHONE, MOBILE, EMAIL, COMP_ID, MODIFY_DATE, \r\n"
								+ "			DEL_DATE, PWD_CHANGE_DATE\r\n"
								+ "		)\r\n"
								+ "		VALUES \r\n"
								+ "		(\r\n"
								+ 			"'"+ cuserVo.getAddUserId()+"', \r\n"
								+ 			"'"+ cuserVo.getAddPassWd()+"', \r\n"
								+ 			"'"+ cuserVo.getAddUserName()+"', \r\n"
								+ 			"'"+ cuserVo.getAddUserGrade()+"', \r\n"
								+ "			'', \r\n"
								+ "			'', \r\n"
								+ "			'', \r\n"
								+ "			'', \r\n"
								+ 			"'"+ cuserVo.getAddCompId()+"', \r\n"
								+ "			NOW(), \r\n"
								+ "			NOW(), \r\n"
								+ "			NOW()\r\n"
								+ "		)");
						this.userHistoryService.insertUserHistory(userHistory);
					
						resultMap.put("result", "1");
					} else {
						resultMap.put("result", "0");
					}
				}
			}
		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "0");
 		}
		
		return resultMap;
	}

	/*
	 * 수정모달 이동
	 */
	@ResponseBody
	@RequestMapping(value="cuserUpdateData.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> cuserUpdateData(@RequestParam HashMap<String, Object> req, Model model) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {

			CuserVo cuserVo = this.cuserService.selectCuserInfo(req);
			
			String failCnt = this.cuserService.getLockCnt();
			
			resultMap.put("cuserVo", cuserVo);
			resultMap.put("result", "success");
			resultMap.put("failCnt", Integer.parseInt(failCnt));

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "error");
		}
		return resultMap;
	}
	
	/*
	 * iwm 운영자 수정
	 */
	@ResponseBody
	@RequestMapping(value="cuserUpdate.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> cuserUpdate(CuserVo cuserVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			if(cuserVo.getCngPwd() != null) {
				// password encoding
				String password = cuserVo.getUpdPassWd();
				String shaPassword = passwordUtils.encodineSha256(password);
				cuserVo.setUpdPassWd(shaPassword);
			}
			
			//update
			int updResult = this.cuserService.updateCuser(cuserVo);

			if(updResult > 0) {
				
				String sql = "UPDATE TB_USER_INFO_CENTER SET\r\n";
				
				if(cuserVo.getCngPwd() != null) {
					sql	+= "PASSWD = '"+ cuserVo.getUpdPassWd() +"'\r\n";
				}
				
			    sql += ", USER_NAME = '"+ cuserVo.getUpdUserName() +"'\r\n"
				    + ", USER_GRADE = '"+ cuserVo.getUpdUserGrade()+"'\r\n"
				    + ", COMP_ID = '"+ cuserVo.getUpdCompId()+"'\r\n"
				    + ", MODIFY_DATE = NOW()\r\n"
				    + ", GUI_FLAG = 'Y'\r\n"
				    + ", PWD_CHANGE_DATE = NOW()\r\n"
				    + "WHERE USER_ID = '"+cuserVo.getUpdUserId() +"'";
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("701");
				userHistory.setJob_name("수정");
				userHistory.setGui_remark("Operator ID : " + cuserVo.getUpdUserId() +", Operator Name : " + cuserVo.getUpdUserName());
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str(sql);
				this.userHistoryService.insertUserHistory(userHistory);
				
				resultMap.put("result", "1");	
			} else {
				resultMap.put("result", "0");
			}
		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "0");
 		}	
		return resultMap;
	}
	
	/**
	 * 운영자 삭제
	 */
	@ResponseBody
	@RequestMapping(value="cuserDelete.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> cuserDelete(CuserVo cuserVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			int updResult = this.cuserService.deleteCuser(cuserVo);
				updResult += this.cuserService.deleteLock(cuserVo);
			
			if(updResult > 0) {
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("701");
				userHistory.setJob_name("삭제");
				userHistory.setGui_remark("Operator ID : " + cuserVo.getUser_id());
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str("UPDATE TB_USER_INFO_CENTER SET\r\n"
						+ "			   DEL_FLAG = 'Y'\r\n"
						+ "			 , DEL_DATE = NOW()\r\n"
						+ "			 , GUI_FLAG = 'Y'\r\n"
						+ "		WHERE USER_ID = '"+ cuserVo.getUser_id() +"'");
				this.userHistoryService.insertUserHistory(userHistory);
				
				resultMap.put("result", "1");
		
			} else {
				resultMap.put("result", "0");
			}
		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "0");
 		}
		return resultMap;
	}
	
	/**
	 * 계정 잠금 해제
	 */
	@ResponseBody
	@RequestMapping(value="cuserUnLock.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> cuserUnLock(CuserVo cuserVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			int lockCencel = this.cuserService.unLockUser(cuserVo);
			
			if(lockCencel > 0) {
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("701");
				userHistory.setJob_name("수정");
				userHistory.setGui_remark("Operator ID : " + cuserVo.getUpdUserId() + ", 계정 잠금 해제");
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str("UPDATE TB_USER_INFO_CENTER SET\r\n"
						+ "			   DEL_FLAG = 'Y'\r\n"
						+ "			 , DEL_DATE = NOW()\r\n"
						+ "			 , GUI_FLAG = 'Y'\r\n"
						+ "		WHERE USER_ID = '"+ cuserVo.getUser_id() +"'");
				this.userHistoryService.insertUserHistory(userHistory);
				
				resultMap.put("result", "1");
				
			} else {
				resultMap.put("result", "0");
			}
		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "0");
 		}
		return resultMap;
	}
	
	/**
	 * iwm 운영자 Excel Down
	 */
	@RequestMapping(value="cuserExcelDownload.do")
	public void cuserExcelDownload(HttpServletResponse response, HttpSession session, CuserVo cuserVo, Locale locale) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		if(!scomp.equals("0")) {
			cuserVo.setScomp(scomp);
		}
		
		try {	
			
			cuserVo.setStartrow(-1);
			List<CuserVo> excelList = this.cuserService.selectCuserList(cuserVo);

			String[] l_aColumns = {
					messageSource.getMessage("select.shipOwner", null, locale), 
					messageSource.getMessage("cuser.nickName", null, locale), 
					messageSource.getMessage("cuser.operatorID", null, locale), 
					messageSource.getMessage("cuser.grade", null, locale)
					};
			
			String xlsName = messageSource.getMessage("cuser.iwmOperatorManagement", null, locale);
			SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		    String today = formatter.format(new java.util.Date());
			String fileName	 = xlsName+"_"+ today+".xlsx";
			
			// Workbook 생성
			Workbook wb = new XSSFWorkbook();

			// Sheet 생성
			Sheet sheet = wb.createSheet(xlsName);
			
			/* 포맷 */

			//header
			XSSFColor color = new XSSFColor(new java.awt.Color(223, 236, 240), new DefaultIndexedColorMap());
			
			// 헤더 폰트 생성
			Font headerFont = wb.createFont();
			headerFont.setFontName("Verdana");
			headerFont.setFontHeightInPoints((short) 9); // 글꼴 크기 설정
			headerFont.setBold(true); // 굵은 글꼴 설정			
	
			CellStyle csHeader = wb.createCellStyle();
			
			// 폰트 적용
			csHeader.setFont(headerFont);
			
			// 배경색 적용
			((XSSFCellStyle) csHeader).setFillForegroundColor(color); 	
			csHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	
			// 정렬방식 설정
			csHeader.setVerticalAlignment(VerticalAlignment.CENTER);
			csHeader.setAlignment(HorizontalAlignment.CENTER);	
			
			// 헤더 테두리 설정
			csHeader.setBorderTop(BorderStyle.THIN);
			csHeader.setBorderBottom(BorderStyle.THIN);
			csHeader.setBorderLeft(BorderStyle.THIN);
			csHeader.setBorderRight(BorderStyle.THIN);

			// 바디 폰트 생성
			Font bodyFont = wb.createFont();
			bodyFont.setFontName("Verdana");
			bodyFont.setFontHeightInPoints((short) 10);
			
			CellStyle csBody = wb.createCellStyle();	
			
			// 바디 폰트 적용
			csBody.setFont(bodyFont);
			
			// 바디 정력방식 설정
			csBody.setVerticalAlignment(VerticalAlignment.CENTER);
			csBody.setAlignment(HorizontalAlignment.CENTER);
			
			// 바디 테두리 설정
			csBody.setBorderTop(BorderStyle.THIN);
			csBody.setBorderBottom(BorderStyle.THIN);
			csBody.setBorderLeft(BorderStyle.THIN);
			csBody.setBorderRight(BorderStyle.THIN);
			
			// 항목명
			int rowIdx = 0;
			int colIdx = 0;
			Row row = sheet.createRow(rowIdx);
			row.setHeightInPoints(20);
			
			for(int i = 0; i < l_aColumns.length; i++ ) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(csHeader);
				cell.setCellValue(l_aColumns[i]);
			}
			rowIdx++;
			
			for(CuserVo vo : excelList){
				row = sheet.createRow(rowIdx);

				colIdx = 0;
				
				Cell cell;
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getComp_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getUser_name());	
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getUser_id());	
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getGname());	
				cell.setCellStyle(csBody);
							
				rowIdx++;
			}
			
			for(int i=0; i < l_aColumns.length; i++) {
				sheet.autoSizeColumn(i);
				sheet.setColumnWidth(i, (sheet.getColumnWidth(i))+1024);
			}
			
			String outputFileName = new String(fileName.getBytes("UTF-8"), "8859_1");
			response.setContentType("ms-vnd/excel; utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + outputFileName);
	        ServletOutputStream servletOutputStream = response.getOutputStream();
	        
			wb.write(servletOutputStream);
			wb.close();
	        servletOutputStream.flush();
	        servletOutputStream.close();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String sql = "SELECT A.USER_ID, A.COMP_NAME, A.USER_NAME, A.GNAME\r\n"
				+ "			 , A.TEAM_NAME, A.PHONE, A.MOBILE, A.EMAIL\r\n"
				+ "		  FROM (\r\n"
				+ "		  			SELECT U.USER_ID, U.USER_NAME, U.USER_GRADE, G.GNAME\r\n"
				+ "		  				 , U.TEAM_NAME, U.PHONE, U.MOBILE, U.EMAIL\r\n"
				+ "		  				 , U.DEL_FLAG, U.COMP_ID, U.COMP_NAME\r\n"
				+ "				      FROM (\r\n"
				+ "				   		   		SELECT UI.COMP_ID, UI.USER_ID, UI.USER_NAME, UI.USER_GRADE\r\n"
				+ "				   		   			 , UI.TEAM_NAME, UI.PHONE, UI.MOBILE, UI.EMAIL\r\n"
				+ "				   		   			 , UI.DEL_FLAG, SI.COMP_NAME\r\n"
				+ "				   		          FROM TB_USER_INFO_CENTER UI \r\n"
				+ "				   			      LEFT JOIN TB_SCOMP_INFO SI\r\n"
				+ "				   					ON UI.COMP_ID=SI.COMP_ID\r\n"
				+ "				   			) U, TB_GRADE_INFO_CENTER G\r\n"
				+ "				     WHERE U.USER_GRADE = G.GRADE\r\n"
				+ "				) A\r\n"
				+ "		  WHERE A.DEL_FLAG = 'N'\r\n";

		if(!cuserVo.getSearchCompId().isEmpty() && cuserVo.getSearchCompId() != null) {
			sql += "AND COMP_ID = '"+ cuserVo.getSearchCompId() +"' \r\n";
		}
		if(cuserVo.getScomp() != "0" && cuserVo.getScomp() != null && cuserVo.getSearchCompId().isEmpty()) {
			sql += "AND A.COMP_ID = '"+ cuserVo.getScomp() +"'\r\n";
		}
		if(cuserVo.getSearchUserName() != null && !cuserVo.getSearchUserName().isEmpty() ) {
			sql += "AND A.USER_NAME LIKE CONCAT ('%', '"+cuserVo.getSearchUserName()+"', '%')\r\n";
		}
		if(cuserVo.getSearchUserId() != null && !cuserVo.getSearchUserId().isEmpty()) {
			sql += "AND A.USER_ID LIKE CONCAT ('%', '"+cuserVo.getSearchUserId()+"', '%')\r\n";
		}
		if(!cuserVo.getSearchGrade().isEmpty() && cuserVo.getSearchGrade() != null) {
			sql += "AND USER_GRADE = '"+ cuserVo.getSearchGrade() +"' \r\n";
		}
		
		sql += "ORDER BY USER_ID";
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("701");
		userHistory.setJob_name("엑셀");
		userHistory.setGui_remark("사유 : " + cuserVo.getExcelMsg());
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str(sql);
		this.userHistoryService.insertUserHistory(userHistory);
	}
}
