package iwm_ko.suser.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import iwm_ko.apinfo.model.ApInfoVo;
import iwm_ko.apinfo.service.ApInfoService;
import iwm_ko.comm.b15p10.paging_10;
import iwm_ko.shipInfo.service.ShipInfoService;
import iwm_ko.suser.model.SuserVo;
import iwm_ko.suser.service.SuserService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import iwm_ko.util.PasswordUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/suser")
public class SuserController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private MessageSource messageSource;
	
	@Autowired
	private PasswordUtils passwordUtils;
	
	@Resource(name="iwm_ko.suser.suserService")
	private SuserService suserService;
	
	@Resource(name="iwm_ko.shipInfo.ShipInfoService")
	private ShipInfoService shipInfoService;
	
	@Resource(name="iwm_ko.apinfo.apInfoService")
	private ApInfoService apInfoService;
	
	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;

	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/suser.do")
	public String suser(@ModelAttribute("suserVo") SuserVo suserVo, Model model, HttpSession session) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			suserVo.setScomp(scomp);
		}

		suserVo.setStartrow((suserVo.getPageno() -1) * suserVo.getFetchrow());

		// compList 조회
		List<String> compList = this.shipInfoService.selectCompList(scomp);
		model.addAttribute("compList", compList);
	
		// grade 리스트 조회
		List<SuserVo> gradeList = this.suserService.selectGradeList(suserVo);
		model.addAttribute("gradeList", gradeList);
		
		// 유저리스트 조회
		List<SuserVo> suserList = this.suserService.selectSuserList(suserVo);
		model.addAttribute("suserList", suserList);
		
		// 페이징
		paging_10 paging = new paging_10();
		
		int totalCnt = this.suserService.totalSuserList(suserVo);
		
		String pagingHTML = paging.getPagingStr(suserVo.getPageno(), suserVo.getFetchrow() , totalCnt);
		
		model.addAttribute("pagingHTML", pagingHTML);
		model.addAttribute("suserVo", suserVo);

		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("704");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);
	
		return "suser/suser";
	}

	/*
	 * OWM 운영자 등록
	 */
	@ResponseBody
	@RequestMapping(value="suserAdd.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> suserAdd(SuserVo suserVo, HttpSession session, Model model) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			//중복검사
			int chkId = this.suserService.chkInsertSuser(suserVo).size();
//			logger.info("============================================ chkId : " + chkId);
			
			if(chkId > 0) {
				resultMap.put("result", "-2");
			} else {

				// del_flag 체크
				int chkFlag = this.suserService.chkDelFlag(suserVo).size();
//				logger.info("================================= chkFlag : " + chkFlag);
				if(chkFlag > 0) {
					
					// password encoding
					String password = suserVo.getAddPassWd();
					String shaPassword = passwordUtils.encodineSha256(password);
					suserVo.setAddPassWd(shaPassword);
					
					// dle_flag가 Y이면 기존 유저 수정
					int changeUser = this.suserService.changeUser(suserVo);
//					logger.info("================================= changeUser : " + changeUser);	
					if(changeUser > 0) {
						
						// 로그 기록
						UserHistoryVo userHistory = new UserHistoryVo();
						userHistory.setGui_code("704");
						userHistory.setJob_name("추가");
						userHistory.setGui_remark("Operator ID : " + suserVo.getAddUserId() +", Operator Name : " + suserVo.getAddUserName() + ", Ship Code : " + suserVo.getAddShipName());
						userHistory.setUser_id((String) session.getAttribute("user_id"));
						userHistory.setSql_str("UPDATE TB_USER_INFO_SHIP SET\r\n"
								+ "			   PASSWD ='"+ suserVo.getAddPassWd() +"'\r\n"
								+ "			 , USER_NAME ='"+ suserVo.getAddUserName() +"'\r\n"
								+ "		     , USER_GRADE ='"+ suserVo.getAddUserGrade() +"'\r\n"
								+ "		     , TEAM_NAME = ''\r\n"
								+ "		     , PHONE = ''\r\n"
								+ "		     , MOBILE = ''\r\n"
								+ "		     , EMAIL = ''\r\n"
								+ "		     , S_CODE ='"+ suserVo.getAddShipName() +"'\r\n"
								+ "		     , DEL_FLAG = 'N'\r\n"
								+ "		     , MODIFY_DATE = NOW()\r\n"
								+ "		     , GUI_FLAG = 'Y'\r\n"
								+ "		 WHERE USER_ID ='"+ suserVo.getAddUserId() +"'\r\n"
								+ "		   AND S_CODE ='"+ suserVo.getAddShipName() +"'");
						this.userHistoryService.insertUserHistory(userHistory);
						
						resultMap.put("result", "1");
					} else {
						resultMap.put("result", "0");
					}
				} else {
					
					// password encoding
					String password = suserVo.getAddPassWd();
					String shaPassword = passwordUtils.encodineSha256(password);
					suserVo.setAddPassWd(shaPassword);

					//운영자 신규 등록
					int insResult = this.suserService.insertSuser(suserVo);
					
					if(insResult > 0) {
						
						// 로그 기록
						UserHistoryVo userHistory = new UserHistoryVo();
						userHistory.setGui_code("704");
						userHistory.setJob_name("추가");
						userHistory.setGui_remark("Operator ID : " + suserVo.getAddUserId() +", Operator Name : " + suserVo.getAddUserName() + ", Ship Code : " + suserVo.getAddShipName());
						userHistory.setUser_id((String) session.getAttribute("user_id"));
						userHistory.setSql_str("INSERT INTO TB_USER_INFO_SHIP \r\n"
								+ "		(\r\n"
								+ "			USER_ID, PASSWD, USER_NAME, USER_GRADE, TEAM_NAME, \r\n"
								+ "			PHONE, MOBILE, EMAIL, S_CODE, MODIFY_DATE, DEL_DATE\r\n"
								+ "		)\r\n"
								+ "		VALUES \r\n"
								+ "		(\r\n"
								+ "			'"+ suserVo.getAddUserId() +"', \r\n"
								+ "			'"+ suserVo.getAddPassWd() +"', \r\n"
								+ "			'"+ suserVo.getAddUserName() +"', \r\n"
								+ "			'"+ suserVo.getAddUserGrade() +"', \r\n"
								+ "			'', \r\n"
								+ "			'', \r\n"
								+ "			'', \r\n"
								+ "			'', \r\n"
								+ "			'"+ suserVo.getAddShipName() +"', \r\n"
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
	@RequestMapping(value="suserUpdateData.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> suserUpdateData(@RequestParam HashMap<String, Object> req, Model model, HttpSession session) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			SuserVo suserVo = this.suserService.selectSuserInfo(req);
			
			String sCode = suserVo.getS_code();
//			logger.info("============================================="+sCode);
			session.setAttribute("s_code", sCode);

			// 선박명 조회 ----------------  수정모달창 shipList apInfo에서 불러오는 것으로 수정
//			List<SuserVo> shipList = this.apInfoService.selectShipList(suserVo);
//			model.addAttribute("shipList", shipList);
			
			ApInfoVo apInfoVo = new ApInfoVo();
			String comp_id = suserVo.getComp_id();
			apInfoVo.setSearchCompId(comp_id);
			List<ApInfoVo> shipList = this.apInfoService.selectShipList(apInfoVo);
			model.addAttribute("shipList", shipList);

			resultMap.put("shipList", shipList);
			resultMap.put("suserVo", suserVo);
			resultMap.put("result", "success");

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
	@RequestMapping(value="suserUpdate.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> suserUpdate(SuserVo suserVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {

//			logger.info("======================================"+session.getAttribute("s_code"));
			String sCode = (String) session.getAttribute("s_code");
			suserVo.setS_code(sCode);
			
			if(suserVo.getCngPwd() != null) {
				// password encoding
				String password = suserVo.getUpdPassWd();
				String shaPassword = passwordUtils.encodineSha256(password);
				suserVo.setUpdPassWd(shaPassword);
			}
			
			//update
			int updResult = this.suserService.updateSuser(suserVo);

			if(updResult > 0) {
				
				String sql = "UPDATE TB_USER_INFO_SHIP SET\r\n";
				
				if(suserVo.getCngPwd() != null) {
					sql	+= "PASSWD = '"+ suserVo.getUpdPassWd() +"'\r\n";
				}
				
				sql +=    "			   USER_NAME = '"+ suserVo.getUpdUserName()+ "'\r\n"
						+ "		     , USER_GRADE = '"+ suserVo.getUpdUserGrade()+ "'\r\n"
						+ "		     , S_CODE = '"+ suserVo.getUpdScode()+ "'\r\n"
						+ "		     , MODIFY_DATE = NOW()\r\n"
						+ "		     , GUI_FLAG = 'Y'\r\n"
						+ "		 WHERE USER_ID = '"+ suserVo.getUpdUserId()+ "'\r\n"
						+ "		   AND S_CODE = '"+ suserVo.getS_code() +"'";
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("704");
				userHistory.setJob_name("수정");
				userHistory.setGui_remark("Operator ID : " + suserVo.getUpdUserId() + "\r\n"
					+", Operator Name : " + suserVo.getUpdUserName() + ", Ship Code : " + suserVo.getUpdScode());
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
	@RequestMapping(value="suserDelete.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> suserDelete(SuserVo suserVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
//			logger.info("======================================"+session.getAttribute("s_code"));
			String sCode = (String) session.getAttribute("s_code");
			suserVo.setS_code(sCode);
			
			int updResult = this.suserService.deleteSuser(suserVo);
			
			if(updResult > 0) {
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("704");
				userHistory.setJob_name("삭제");
				userHistory.setGui_remark("Operator ID : " + suserVo.getUser_id());
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str("UPDATE TB_USER_INFO_SHIP SET\r\n"
						+ "			   DEL_FLAG = 'Y'\r\n"
						+ "			 , DEL_DATE = NOW()\r\n"
						+ "			 , GUI_FLAG = 'Y'\r\n"
						+ "		WHERE USER_ID = '"+ suserVo.getUser_id() +"' \r\n"
						+ "		AND S_CODE = '"+ suserVo.getS_code() +"'");
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
	 * Excel Down
	 */
	@RequestMapping(value="suserExcelDownload.do")
	public void suserExcelDownload(HttpServletResponse response, final HttpSession session, SuserVo suserVo, Locale locale) throws Exception {
		
		suserVo.setScomp((String)session.getAttribute("comp_id"));
		
		String language = (String)session.getAttribute("language");
		
		try {	
			
			suserVo.setStartrow(-1);
			List<SuserVo> excelList = this.suserService.selectSuserList(suserVo);
			
			// 기존 배열을 List로 변환
			List<String> l_aColumnsList = new ArrayList<>();
			l_aColumnsList.add(messageSource.getMessage("select.shipOwner", null, locale));
			l_aColumnsList.add(messageSource.getMessage("select.shipName", null, locale));
			l_aColumnsList.add(messageSource.getMessage("cuser.nickName", null, locale));
			l_aColumnsList.add(messageSource.getMessage("cuser.operatorID", null, locale));
			l_aColumnsList.add(messageSource.getMessage("cuser.grade", null, locale));

			// "korean"일 경우 추가 항목 포함
			if("korean".equals(language)) {
			    l_aColumnsList.add("소속");
			    l_aColumnsList.add("일반전화");
			    l_aColumnsList.add("휴대전화");
			    l_aColumnsList.add("E-mail");
			} else {
				
			}

			// 필요한 경우, List를 배열로 변환할 수 있습니다.
			String[] l_aColumns = l_aColumnsList.toArray(new String[0]);
			
			String xlsName = messageSource.getMessage("suser.owmOperatorManagement", null, locale);
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
			
			for(SuserVo vo : excelList){
				row = sheet.createRow(rowIdx);

				colIdx = 0;
				
				Cell cell;
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getComp_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getS_name());
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
				
				if("korean".equals(language)) {
					cell = row.createCell(colIdx++);
					cell.setCellValue(vo.getTeam_name());
					cell.setCellStyle(csBody);
					
					cell = row.createCell(colIdx++);
					cell.setCellValue(vo.getPhone());
					cell.setCellStyle(csBody);
					
					cell = row.createCell(colIdx++);
					cell.setCellValue(vo.getMobile());
					cell.setCellStyle(csBody);
					
					cell = row.createCell(colIdx++);
					cell.setCellValue(vo.getEmail());
					cell.setCellStyle(csBody);
				}
				
//				for(int idx = 0; idx < colIdx; idx++) {
//					Cell cell = row.getCell(idx);
//					cell.setCellStyle(csBody);
//				}			
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
		
		String sql = "SELECT A.USER_ID, A.USER_NAME, B.GNAME, A.TEAM_NAME, A.PHONE\r\n"
				+ "		     , A.MOBILE, A.EMAIL, A.DEL_FLAG, A.S_CODE, A.S_NAME\r\n"
				+ "		     , A.COMP_ID, A.COMP_NAME\r\n"
				+ "	      FROM (\r\n"
				+ "		        SELECT U.USER_ID, U.USER_NAME, U.USER_GRADE, U.TEAM_NAME, U.PHONE\r\n"
				+ "			  	     , U.MOBILE, U.EMAIL, S.S_CODE, S.S_NAME, U.DEL_FLAG\r\n"
				+ "				     , S.COMP_ID, S.COMP_NAME\r\n"
				+ "			      FROM (\r\n"
				+ "					    SELECT SI.COMP_ID, CI.COMP_NAME, SI.S_NAME, SI.S_CODE\r\n"
				+ "					      FROM TB_SHIP_INFO SI, TB_SCOMP_INFO CI\r\n"
				+ "						 WHERE SI.COMP_ID=CI.COMP_ID\r\n"
				+ "				       )S, TB_USER_INFO_SHIP U\r\n"
				+ "			     WHERE U.S_CODE = S.S_CODE\r\n"
				+ "	           )A, TB_GRADE_INFO_SHIP B\r\n"
				+ "	     WHERE A.USER_GRADE = B.GRADE\r\n"
				+ "	       AND A.DEL_FLAG = 'N'\r\n";
		
		if(!suserVo.getSearchCompId().isEmpty() && suserVo.getSearchCompId() != null) {
			sql += "AND A.COMP_ID = '"+ suserVo.getSearchCompId() +"' \r\n";
		}
		if(suserVo.getScomp() != "0" && suserVo.getScomp() != null && suserVo.getSearchCompId().isEmpty()) {
			sql += "AND A.COMP_ID = '"+ suserVo.getScomp() +"'\r\n";
		}
		if(suserVo.getSearchUserName() != null && !suserVo.getSearchUserName().isEmpty()) {
			sql += "AND A.USER_NAME LIKE CONCAT ('%', '"+suserVo.getSearchUserName()+"', '%')\r\n";
		}
		if(suserVo.getSearchUserId() != null && !suserVo.getSearchUserId().isEmpty()) {
			sql += "AND A.USER_ID LIKE CONCAT ('%', '"+suserVo.getSearchUserId()+"', '%')\r\n";
		}
		if(!suserVo.getSearchGrade().isEmpty() && suserVo.getSearchGrade() != null) {
			sql += "AND A.USER_GRADE = '"+ suserVo.getSearchGrade() +"' \r\n";
		}
		if(suserVo.getSearchSname() != null && !suserVo.getSearchSname().isEmpty()) {
			sql += "AND A.S_NAME LIKE CONCAT ('%', '"+suserVo.getSearchSname()+"', '%')\r\n";
		}
		
		sql += "ORDER BY USER_ID";

		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("704");
		userHistory.setJob_name("엑셀");
		userHistory.setGui_remark("사유 : " + suserVo.getExcelMsg());
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str(sql);
		this.userHistoryService.insertUserHistory(userHistory);
	}

}