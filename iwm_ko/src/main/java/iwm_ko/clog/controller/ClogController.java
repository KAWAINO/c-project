package iwm_ko.clog.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import iwm_ko.clog.model.ClogVo;
import iwm_ko.clog.service.ClogService;
import iwm_ko.comm.b20p10.paging_10;
import iwm_ko.shipInfo.service.ShipInfoService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/clog")
public class ClogController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	@Resource(name="iwm_ko.clog.clogService")
	private ClogService clogService;
	
	@Resource(name="iwm_ko.shipInfo.ShipInfoService")
	private ShipInfoService shipInfoService;
	
	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;
	
	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/clog.do")
	public String clog(@ModelAttribute("clogVo") ClogVo clogVo, Model model, HttpSession session, Locale locale) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		if(!scomp.equals("0")) {
			clogVo.setScomp(scomp);
		}
		clogVo.setStartrow((clogVo.getPageno() -1) * clogVo.getFetchrow());

		// 페이지로딩 & 검색 시 날짜 설정
		if("true".equals(clogVo.getSearchChked())) {
			clogService.searchFormatDate(clogVo);
			model.addAttribute("defaultStartDate", clogVo.getStartDefaultDate());
			model.addAttribute("defaultEndDate", clogVo.getEndDefaultDate());
		} else {
			clogService.defaultFormatDate(clogVo);
			model.addAttribute("defaultStartDate", clogVo.getDefaultDate());
			model.addAttribute("defaultEndDate", clogVo.getDefaultDate());
		}
		
		List<String> compList = this.shipInfoService.selectCompList(scomp);
		model.addAttribute("compList", compList);
		
		// guiList 조회
		List<ClogVo> getMenuList = this.clogService.getMenuList(clogVo);
		model.addAttribute("getMenuList", getMenuList);
		
//		messageSource.getMessage("clog.history", null, locale);
//		messageSource.getMessage("clog.login", null, locale);
//		messageSource.getMessage("clog.logOut", null, locale);
//		messageSource.getMessage("clog.search", null, locale);
//		messageSource.getMessage("clog.add", null, locale);
//		messageSource.getMessage("clog.modify", null, locale);
//		messageSource.getMessage("clog.delete", null, locale);
//		messageSource.getMessage("clog.excel", null, locale);
//		messageSource.getMessage("clog.import", null, locale);
//		messageSource.getMessage("clog.printReport", null, locale);
		
		// logList 조회
		List<ClogVo> cLogList = this.clogService.selectClogList(clogVo, locale);
		model.addAttribute("cLogList", cLogList);
//			logger.info("===================================================" + clogVo.getSearchJobName());
		
		// 페이징
		paging_10 paging = new paging_10();
		
		int totalCnt = this.clogService.totalClogList(clogVo);
		
		String pagingHTML = paging.getPagingStr(clogVo.getPageno(), clogVo.getFetchrow() , totalCnt);
		
		model.addAttribute("pagingHTML", pagingHTML);
		model.addAttribute("clogVo", clogVo);
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("703");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);	

		return "clog/clog";
	}

	/**
	 * iwm 운영자 Excel Down
	 */
	@RequestMapping(value="clogExcelDownload.do")
	public void clogExcelDownload(HttpServletResponse response, final HttpSession session, ClogVo clogVo, Locale locale) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		if(!scomp.equals("0")) {
			clogVo.setScomp(scomp);
		}
		
		String language = (String)session.getAttribute("language");
		
		try {	
			
			// 날짜 포맷
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd"); 
			DateTimeFormatter chFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
			
			String strDate = clogVo.getStartDate(); 
		    String endDate = clogVo.getEndDate(); 
		    
		    // 문자열을 LocalDateTime 객체로 변환 
		    LocalDate fmtStrDate = LocalDate.parse(strDate, chFmt); 
		    LocalDate fmtEndDate = LocalDate.parse(endDate, chFmt); 
		    
		    // yyyyMMdd 변경
		    strDate = fmtStrDate.format(fmt); 
		    endDate = fmtEndDate.format(fmt); 
		    
		    strDate += clogVo.getStartHour() + clogVo.getStartMin() + "00";
	        endDate += clogVo.getEndHour() + clogVo.getEndMin() + "59";
	        
	        clogVo.setStartDate(strDate);
	        clogVo.setEndDate(endDate);
			
			clogVo.setStartrow(-1);
			List<ClogVo> excelList = this.clogService.selectClogList(clogVo, locale);

			String[] l_aColumns = {
					messageSource.getMessage("time.date", null, locale), 
					messageSource.getMessage("select.shipOwner", null, locale), 
					messageSource.getMessage("list.id", null, locale), 
					messageSource.getMessage("list.nickName", null, locale), 
					messageSource.getMessage("clog.menuMyung", null, locale), 
					messageSource.getMessage("clog.history", null, locale), 
					messageSource.getMessage("list.detail", null, locale)
					};
			
			String xlsName = messageSource.getMessage("clog.iwmWorkHistory", null,  locale);
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
			
			for(ClogVo vo : excelList){
				row = sheet.createRow(rowIdx);

				colIdx = 0;
				
				Cell cell;
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getLog_date());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getComp_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getUser_id());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getUser_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				if(language.equals("korean")) {
					cell.setCellValue(vo.getGui_name());
				} else {
					cell.setCellValue(vo.getEn_gui_name());
				}			
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getJob_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getGui_remark());
				cell.setCellStyle(csBody);
				
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
		
		String sql = "SELECT DATE_FORMAT(A.LOG_DATE, '%Y-%m-%d %H:%i:%s') LOG_DATE\r\n"
				+ "			 , B.COMP_NAME, A.USER_ID, B.USER_NAME, A.GUI_NAME\r\n"
				+ "			 , A.EN_GUI_NAME, A.JOB_NAME, A.GUI_REMARK\r\n"
				+ "		  FROM (\r\n"
				+ "				   SELECT U.LOG_DATE \r\n"
				+ "						, IFNULL(G.GUI_NAME, CASE WHEN U.JOB_NAME = '로그인' THEN '로그인'\r\n"
				+ "												  WHEN U.JOB_NAME = '로그아웃' THEN '로그아웃' ELSE '-' END) GUI_NAME\r\n"
				+ "						, IFNULL(G.EN_GUI_NAME, CASE WHEN U.JOB_NAME = '로그인' THEN 'Login'\r\n"
				+ "												     WHEN U.JOB_NAME = '로그아웃' THEN 'Logout' ELSE '-' END) EN_GUI_NAME\r\n"
				+ "					    , U.GUI_CODE,U.JOB_NAME, U.GUI_REMARK, U.USER_ID\r\n"
				+ "				     FROM TB_USER_LOG_CENTER U \r\n"
				+ "				     LEFT JOIN TB_GUI_INFO_CENTER G\r\n"
				+ "				       ON G.GUI_CODE = U.GUI_CODE\r\n"
				+ "				) A, \r\n"
				+ "				(\r\n"
				+ "				   SELECT UI.COMP_ID, UI.USER_ID, UI.USER_NAME, UI.DEL_FLAG, SI.COMP_NAME\r\n"
				+ "				     FROM TB_USER_INFO_CENTER UI \r\n"
				+ "				     LEFT JOIN TB_SCOMP_INFO SI\r\n"
				+ "				       ON UI.COMP_ID = SI.COMP_ID\r\n"
				+ "				) B	   \r\n"
				+ "		 WHERE A.USER_ID = B.USER_ID\r\n"
				+ "	       AND A.LOG_DATE BETWEEN STR_TO_DATE('"+ clogVo.getStartDate() +"', '%Y%m%d%H%i%s') \r\n"
				+ "		   AND STR_TO_DATE('"+ clogVo.getEndDate() +"', '%Y%m%d%H%i%s')\r\n";
		
		if(!clogVo.getSearchCompId().isEmpty() && clogVo.getSearchCompId() != null) {
			sql += "AND COMP_ID = '"+ clogVo.getSearchCompId() +"' \r\n";
		}
		if(clogVo.getScomp() != "0" && clogVo.getScomp() != null && clogVo.getSearchCompId().isEmpty()) {
			sql += "AND COMP_ID = '"+ clogVo.getScomp() +"'\r\n";
		}
		if(clogVo.getSearchUserName() != null && !clogVo.getSearchUserName().isEmpty() ) {
			sql += "AND B.USER_NAME LIKE CONCAT ('%', '"+clogVo.getSearchUserName()+"', '%')\r\n";
		}
		if(clogVo.getSearchUserId() != null && !clogVo.getSearchUserId().isEmpty()) {
			sql += "AND A.USER_ID LIKE CONCAT ('%', '"+clogVo.getSearchUserId()+"', '%')\r\n";
		}
		if(!clogVo.getSearchJobName().isEmpty() && clogVo.getSearchJobName() != null) {
			sql += "AND A.JOB_NAME = '"+ clogVo.getSearchJobName() +"' \r\n";
		}
		if(!clogVo.getSearchGuiCode().isEmpty() && clogVo.getSearchGuiCode() != null) {
			sql += "AND A.GUI_CODE = '"+ clogVo.getSearchGuiCode() +"' \r\n";
		}
		
		sql += "ORDER BY A.LOG_DATE DESC";
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("703");
		userHistory.setJob_name("엑셀");
		userHistory.setGui_remark("사유: " + clogVo.getExcelMsg());
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str(sql);
		this.userHistoryService.insertUserHistory(userHistory);
	}
}
