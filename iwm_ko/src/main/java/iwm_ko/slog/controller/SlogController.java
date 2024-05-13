package iwm_ko.slog.controller;

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

import iwm_ko.comm.b20p10.paging_10;
import iwm_ko.shipInfo.service.ShipInfoService;
import iwm_ko.slog.model.SlogVo;
import iwm_ko.slog.service.SlogService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/slog")
public class SlogController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	@Resource(name="iwm_ko.slog.slogService")
	private SlogService slogService;

	@Resource(name="iwm_ko.shipInfo.ShipInfoService")
	private ShipInfoService shipInfoService;
	
	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;
	
	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/slog.do")
	public String slog(@ModelAttribute("slogVo") SlogVo slogVo, Model model
			, HttpSession session, Locale locale) throws Exception {
		
		slogVo.setLanguage((String)session.getAttribute("language"));	
		
		String scomp = (String)session.getAttribute("comp_id");
//		logger.info("scomp ::::: " + scomp);
		if(!scomp.equals("0")) {
			slogVo.setScomp(scomp);
		}

		slogVo.setStartrow((slogVo.getPageno() -1) * slogVo.getFetchrow());

		// 페이지로딩 & 검색 시 날짜 설정
		if("true".equals(slogVo.getSearchChked())) {
			slogService.searchFormatDate(slogVo);
			model.addAttribute("defaultStartDate", slogVo.getStartDefaultDate());
			model.addAttribute("defaultEndDate", slogVo.getEndDefaultDate());
		} else {
			slogService.defaultFormatDate(slogVo);
			model.addAttribute("defaultStartDate", slogVo.getDefaultDate());
			model.addAttribute("defaultEndDate", slogVo.getDefaultDate());
		}
		
		// comp 리스트 조회
		List<String> compList = this.shipInfoService.selectCompList(scomp);
		model.addAttribute("compList", compList);
		
		// 메뉴리스트 조회
		List<SlogVo> getMenuList = this.slogService.getMenuList(slogVo);	
		model.addAttribute("getMenuList", getMenuList);
	
		// 로그리스트 조회
		List<SlogVo> slogList = this.slogService.selectSlogList(slogVo, locale);
		model.addAttribute("slogList", slogList);
		
		model.addAttribute("s_code", slogVo.getSearchShipName());
//		logger.info("=====================================" + slogVo.getSearchShipName());
			
		// 페이징
		paging_10 paging = new paging_10();
		
		int totalCnt = this.slogService.totalSlogList(slogVo);
		
		String pagingHTML = paging.getPagingStr(slogVo.getPageno(), slogVo.getFetchrow() , totalCnt);
		
		model.addAttribute("pagingHTML", pagingHTML);
		model.addAttribute("slogVo", slogVo);
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("706");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);
		
		return "slog/slog";
	}
	
	/**
	 * Excel Down
	 */
	@RequestMapping(value="slogExcelDownload.do")
	public void slogExcelDownload(HttpServletResponse response, final HttpSession session, SlogVo slogVo, Locale locale) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			slogVo.setSearchCompId(scomp);
		}
		
		String language = (String)session.getAttribute("language");
		
		try {	
		
			// 날짜 포맷
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd"); 
			DateTimeFormatter chFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
			
			String strDate = slogVo.getStartDate(); 
		    String endDate = slogVo.getEndDate(); 
		    
		    // 문자열을 LocalDateTime 객체로 변환 
		    LocalDate fmtStrDate = LocalDate.parse(strDate, chFmt); 
		    LocalDate fmtEndDate = LocalDate.parse(endDate, chFmt); 
		    
		    // yyyyMMdd 변경
		    strDate = fmtStrDate.format(fmt); 
		    endDate = fmtEndDate.format(fmt); 
		    
		    strDate += slogVo.getStartHour() + slogVo.getStartMin() + "00";
	        endDate += slogVo.getEndHour() + slogVo.getEndMin() + "59";
	        
	        slogVo.setStartDate(strDate);
	        slogVo.setEndDate(endDate);
			
	        slogVo.setStartrow(-1);
			List<SlogVo> excelList = this.slogService.selectSlogList(slogVo, locale);

			String[] l_aColumns = {
					messageSource.getMessage("time.date", null, locale), 
					messageSource.getMessage("select.shipOwner", null, locale), 
					messageSource.getMessage("select.shipName", null, locale), 
					messageSource.getMessage("list.nickName", null, locale), 
					messageSource.getMessage("list.id", null, locale), 				
					messageSource.getMessage("clog.menuMyung", null, locale), 
					messageSource.getMessage("clog.history", null, locale), 
					messageSource.getMessage("list.detail", null, locale)
			};
			
			String xlsName = messageSource.getMessage("slog.owmWorkHistory", null, locale);
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
			
			for(SlogVo vo : excelList){
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
				cell.setCellValue(vo.getS_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getUser_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getUser_id());
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
		
		String sql = "SELECT DATE_FORMAT(A.LOG_DATE, '%Y-%m-%d %H:%i:%s') LOG_DATE, A.COMP_NAME, A.S_NAME\r\n"
				+ "			 , A.USER_ID, B.USER_NAME, A.GUI_NAME, A.EN_GUI_NAME, A.JOB_NAME, A.GUI_REMARK\r\n"
				+ "		  FROM (\r\n"
				+ "			   	SELECT U.LOG_DATE\r\n"
				+ "			   		 , IFNULL(G.GUI_NAME, CASE WHEN U.JOB_NAME='로그인' THEN '로그인'\r\n"
				+ "											   WHEN U.JOB_NAME='로그아웃' THEN '로그아웃' ELSE '-' END) GUI_NAME\r\n"
				+ "					 , IFNULL(G.EN_GUI_NAME, CASE WHEN U.JOB_NAME='로그인' THEN 'Login'\r\n"
				+ "					            				  WHEN U.JOB_NAME='로그아웃' THEN 'Logout' ELSE '-' END) EN_GUI_NAME\r\n"
				+ "					 , U.GUI_CODE, U.JOB_NAME, U.GUI_REMARK, U.USER_ID, U.COMP_ID, U.S_NAME, U.COMP_NAME, U.S_CODE\r\n"
				+ "				  FROM (\r\n"
				+ "					    SELECT L.LOG_DATE, L.GUI_CODE, L.JOB_NAME, L.GUI_REMARK, L.USER_ID, V.COMP_ID, V.COMP_NAME, V.S_NAME, V.S_CODE\r\n"
				+ "					      FROM TB_USER_LOG_SHIP L, (\r\n"
				+ "												    SELECT C.COMP_ID, C.COMP_NAME, S.S_NAME, S.S_CODE\r\n"
				+ "												      FROM TB_SHIP_INFO S, TB_SCOMP_INFO C\r\n"
				+ "												     WHERE S.COMP_ID = C.COMP_ID\r\n"
				+ "					                                 ) V\r\n"
				+ "				         WHERE L.S_CODE = V.S_CODE\r\n"
				+ "			           ) U LEFT JOIN TB_GUI_INFO_SHIP G \r\n"
				+ "		                     ON G.GUI_CODE = U.GUI_CODE\r\n"
				+ "		       ) A, TB_USER_INFO_SHIP B\r\n"
				+ "		 WHERE A.USER_ID = B.USER_ID\r\n"
				+ "	       AND A.S_CODE = B.S_CODE\r\n"
				+ "	       AND A.LOG_DATE BETWEEN STR_TO_DATE(" + slogVo.getStartDate() + ", '%Y%m%d%H%i%s') \r\n"
				+ "		   AND STR_TO_DATE(" + slogVo.getEndDate() + ", '%Y%m%d%H%i%s')";
		
		if(!slogVo.getSearchCompId().isEmpty() && slogVo.getSearchCompId() != null) {
			sql += "AND A.COMP_ID = '"+ slogVo.getSearchCompId() +"' \r\n";
		}
		if(slogVo.getScomp() != "0" && slogVo.getScomp() != null && slogVo.getSearchCompId().isEmpty()) {
			sql += "AND A.COMP_ID = '"+ slogVo.getScomp() +"'\r\n";
		}
		if(!slogVo.getSearchUserName().isEmpty() && slogVo.getSearchUserName() != null) {
			sql += "AND B.USER_NAME LIKE CONCAT ('%', '"+slogVo.getSearchUserName()+"', '%')\r\n";
		}
		if(!slogVo.getSearchUserId().isEmpty() && slogVo.getSearchUserId() != null) {
			sql += "AND A.USER_ID LIKE CONCAT ('%', '"+slogVo.getSearchUserId()+"', '%')\r\n";
		}
		if(!slogVo.getSearchJobName().isEmpty() && slogVo.getSearchJobName() != null) {
			sql += "AND A.JOB_NAME = '"+ slogVo.getSearchJobName() +"'\r\n";
		}
		if(!slogVo.getSearchGuiCode().isEmpty() && slogVo.getSearchGuiCode() != null) {
			sql += "AND A.GUI_CODE = '"+ slogVo.getSearchGuiCode() +"'\r\n";
		}
		if(!slogVo.getSearchShipName().isEmpty() && slogVo.getSearchShipName() != null) {
			sql += "AND A.S_CODE = '"+ slogVo.getSearchShipName() +"'\r\n";
		}
		
		sql += "ORDER BY A.LOG_DATE DESC";
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("707");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("사유: " + slogVo.getExcelMsg());
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str(sql);
		this.userHistoryService.insertUserHistory(userHistory);
	}
}
