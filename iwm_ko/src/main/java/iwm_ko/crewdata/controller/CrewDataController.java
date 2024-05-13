package iwm_ko.crewdata.controller;

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
import iwm_ko.crewdata.model.CrewDataVo;
import iwm_ko.crewdata.service.CrewDataService;
import iwm_ko.shipInfo.service.ShipInfoService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/crewdata")
public class CrewDataController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	@Resource(name="iwm_ko.crewdata.crewDataService")
	private CrewDataService crewDataService;
	
	@Resource(name="iwm_ko.shipInfo.ShipInfoService")
	private ShipInfoService shipInfoService;
	
	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;

	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/crewdata.do")
	public String slog(@ModelAttribute("crewdataVo") CrewDataVo crewDataVo, Model model, HttpSession session) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			crewDataVo.setScomp(scomp);
		}
		
		crewDataVo.setStartrow((crewDataVo.getPageno() -1) * crewDataVo.getFetchrow());
		
		// 페이지로딩 & 검색 시 날짜 설정
		if("true".equals(crewDataVo.getSearchChked())) {
			crewDataService.searchFormatDate(crewDataVo);
			model.addAttribute("defaultStartDate", crewDataVo.getStartDefaultDate());
			model.addAttribute("defaultEndDate", crewDataVo.getEndDefaultDate());
		} else {
			crewDataService.defaultFormatDate(crewDataVo);
			model.addAttribute("defaultStartDate", crewDataVo.getDefaultDate());
			model.addAttribute("defaultEndDate", crewDataVo.getDefaultDate());
		}

		// comp 리스트 조회
		List<String> compList = this.shipInfoService.selectCompList(scomp);
		model.addAttribute("compList", compList);
		
		model.addAttribute("s_code", crewDataVo.getSearchShipName());
		
		List<CrewDataVo> logList = this.crewDataService.selectLogList(crewDataVo);
		model.addAttribute("logList", logList);
		
		// 페이징
		paging_10 paging = new paging_10();
		
		int totalCnt = this.crewDataService.totalCrewList(crewDataVo);
		
		String pagingHTML = paging.getPagingStr(crewDataVo.getPageno(), crewDataVo.getFetchrow() , totalCnt);
		
		model.addAttribute("pagingHTML", pagingHTML);
		model.addAttribute("crewDataVo", crewDataVo);
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("707");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);
		
		return "crewdata/crewdata";
	}
	
	/**
	 * iwm 운영자 Excel Down
	 */
	@RequestMapping(value="crewDataExcelDownload.do")
	public void crewDataExcelDownload(HttpServletResponse response, final HttpSession session
			, CrewDataVo crewDataVo, Locale locale) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			crewDataVo.setSearchCompId(scomp);
		}
		
		try {	
			
			// 날짜 포맷
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd"); 
			DateTimeFormatter chFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
			
			String strDate = crewDataVo.getStartDate(); 
		    String endDate = crewDataVo.getEndDate(); 
		    
		    // 문자열을 LocalDateTime 객체로 변환 
		    LocalDate fmtStrDate = LocalDate.parse(strDate, chFmt); 
		    LocalDate fmtEndDate = LocalDate.parse(endDate, chFmt); 
		    
		    // yyyyMMdd 변경
		    strDate = fmtStrDate.format(fmt); 
		    endDate = fmtEndDate.format(fmt); 
		    
		    strDate += crewDataVo.getStartHour() + crewDataVo.getStartMin() + "00";
	        endDate += crewDataVo.getEndHour() + crewDataVo.getEndMin() + "59";
	        
	        crewDataVo.setStartDate(strDate);
	        crewDataVo.setEndDate(endDate);
			
	        crewDataVo.setStartrow(-1);
			List<CrewDataVo> excelList = this.crewDataService.selectLogList(crewDataVo);

			String[] l_aColumns = {
					messageSource.getMessage("time.date", null, locale), 
					messageSource.getMessage("select.shipOwner", null, locale), 
					messageSource.getMessage("select.shipName", null, locale), 
					messageSource.getMessage("list.operatorID", null, locale), 
					messageSource.getMessage("list.operatorNickName", null, locale), 
					messageSource.getMessage("list.crewID", null, locale), 
					messageSource.getMessage("list.crewNickName", null, locale), 
					messageSource.getMessage("list.previousDataUsage", null, locale), 
					messageSource.getMessage("list.dataUsage", null, locale), 
					messageSource.getMessage("list.dataUsageDifference", null, locale)
					};
			
			String xlsName = messageSource.getMessage("crewdata.addedCrewUsageDetails", null, locale);
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
			
			for(CrewDataVo vo : excelList){
				row = sheet.createRow(rowIdx);

				colIdx = 0;
				
				row.createCell(colIdx++).setCellValue(vo.getLog_date());
				row.createCell(colIdx++).setCellValue(vo.getComp_name());
				row.createCell(colIdx++).setCellValue(vo.getS_name());
				row.createCell(colIdx++).setCellValue(vo.getUser_id());
				row.createCell(colIdx++).setCellValue(vo.getUser_name());
				row.createCell(colIdx++).setCellValue(vo.getCrew_id());
				row.createCell(colIdx++).setCellValue(vo.getCrew_name());
				row.createCell(colIdx++).setCellValue(vo.getPre_data() + " MB");
				row.createCell(colIdx++).setCellValue(vo.getData() + " MB");
				row.createCell(colIdx++).setCellValue(vo.getDiff_data());

				for(int idx = 0; idx < colIdx; idx++) {
					Cell cell = row.getCell(idx);
					cell.setCellStyle(csBody);
				}
				
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
		
		String sql ="SELECT DATE_FORMAT(LOG_DATE, '%Y-%m-%d %H:%i:%s') LOG_DATE\r\n"
				+ "    		 , COMP_NAME, S_NAME, D.USER_ID, USER_NAME, CREW_ID, CREW_NAME, PRE_DATA, DATA, DIFF_DATA\r\n"
				+ "		  FROM (\r\n"
				+ "			    SELECT LOG_DATE, A.COMP_ID, A.COMP_NAME, A.S_CODE, A.S_NAME, C.USER_ID, C.CREW_ID, A.CREW_NAME, C.PRE_DATA, C.DATA, C.DIFF_DATA\r\n"
				+ "				  FROM TB_DATA_CHANGE_LOG C, (\r\n"
				+ "				   							  SELECT CI.COMP_ID, COMP_NAME, SI.S_CODE, S_NAME, CREW_ID, CREW_NAME\r\n"
				+ "											    FROM TB_SCOMP_INFO CI, TB_SHIP_INFO SI, TB_CREW_INFO R\r\n"
				+ "											   WHERE CI.COMP_ID = SI.COMP_ID\r\n"
				+ "											     AND SI.S_CODE=R.S_CODE\r\n"
				+ "				   							 ) A\r\n"
				+ "				  WHERE C.S_CODE = A.S_CODE\r\n"
				+ "				    AND C.CREW_ID=A.CREW_ID\r\n"
				+ "			   ) D, TB_USER_INFO_SHIP U\r\n"
				+ "	     WHERE D.S_CODE = U.S_CODE\r\n"
				+ "	       AND D.USER_ID = U.USER_ID\r\n"
				+ "	       AND LOG_DATE BETWEEN STR_TO_DATE(" + crewDataVo.getStartDate() + ", '%Y%m%d%H%i%s') \r\n"
				+ "		   AND STR_TO_DATE(" + crewDataVo.getEndDate() + ", '%Y%m%d%H%i%s')";
		
		if(!crewDataVo.getSearchCompId().isEmpty() && crewDataVo.getSearchCompId() != null) {
			sql += "AND D.COMP_ID = '"+ crewDataVo.getSearchCompId() +"' \r\n";
		}
		if(crewDataVo.getScomp() != "0" && crewDataVo.getScomp() != null && crewDataVo.getSearchCompId().isEmpty()) {
			sql += "AND D.COMP_ID = '"+ crewDataVo.getScomp() +"'\r\n";
		}
		if(!crewDataVo.getSearchUserName().isEmpty() && crewDataVo.getSearchUserName() != null) {
			sql += "AND U.USER_NAME LIKE CONCAT ('%', '"+crewDataVo.getSearchUserName()+"', '%')\r\n";
		}
		if(!crewDataVo.getSearchUserId().isEmpty() && crewDataVo.getSearchUserId() != null) {
			sql += "AND U.USER_ID LIKE CONCAT ('%', '"+crewDataVo.getSearchUserId()+"', '%')\r\n";
		}
		if(!crewDataVo.getSearchShipName().isEmpty() && crewDataVo.getSearchShipName() != null) {
			sql += "AND D.S_CODE = '"+ crewDataVo.getSearchShipName() +"' \r\n";
		}
		
		sql += "ORDER BY D.LOG_DATE DESC";

		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("707");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("사유: " + crewDataVo.getExcelMsg());
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str(sql);
		this.userHistoryService.insertUserHistory(userHistory);
	}	
}
