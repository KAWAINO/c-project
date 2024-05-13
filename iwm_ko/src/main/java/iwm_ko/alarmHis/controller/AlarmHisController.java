package iwm_ko.alarmHis.controller;

import java.text.SimpleDateFormat;
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

import iwm_ko.alarmHis.model.AlarmHisVo;
import iwm_ko.alarmHis.service.AlarmHisService;
import iwm_ko.comm.b20p10.paging_10;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/alarmhis")
public class AlarmHisController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	@Resource(name="iwm_ko.alarmHis.alarmHisService")
	private AlarmHisService alarmHisService;
	
	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;
	
	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/alarmhis.do")
	public String slog(@ModelAttribute("alarmHisVo") AlarmHisVo alarmHisVo, Model model, HttpSession session) throws Exception {
		
		alarmHisVo.setStartrow((alarmHisVo.getPageno() -1) * alarmHisVo.getFetchrow());
		
		String comp_id = (String) session.getAttribute("comp_id");
		alarmHisVo.setComp_id(comp_id);
		
		// 페이지로딩 & 검색 시 날짜 설정
		if("true".equals(alarmHisVo.getSearchChked())) {
			alarmHisService.searchFormatDate(alarmHisVo);
			model.addAttribute("defaultStartDate", alarmHisVo.getStartDefaultDate());
			model.addAttribute("defaultEndDate", alarmHisVo.getEndDefaultDate());
		} else {
			alarmHisService.defaultFormatDate(alarmHisVo);
			model.addAttribute("defaultStartDate", alarmHisVo.getDefaultDate());
			model.addAttribute("defaultEndDate", alarmHisVo.getDefaultDate());
		}			
		
        // 알람원인 리스트 조회
        List<AlarmHisVo> alarmCauseList = this.alarmHisService.getAlarmCauseList(alarmHisVo);
        model.addAttribute("alarmCauseList", alarmCauseList);
        
        // 알람이력 리스트 조회
        List<AlarmHisVo> alarmHisList = this.alarmHisService.getAlarmHisList(alarmHisVo);
        model.addAttribute("alarmHisList", alarmHisList);
        
        // 페이징
		paging_10 paging = new paging_10();
		
		int totalCnt = this.alarmHisService.totalAlarmHisList(alarmHisVo);
		
		String pagingHTML = paging.getPagingStr(alarmHisVo.getPageno(), alarmHisVo.getFetchrow() , totalCnt);
		
		model.addAttribute("pagingHTML", pagingHTML);
		model.addAttribute("alarmHisVo", alarmHisVo);
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("601");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);
        
		return "alarmhis/alarmhis";
	}
	
	/**
	 * Excel Down
	 */
	@RequestMapping(value="alarmHisExcelDownload.do")
	public void alarmHisExcelDownload(final HttpSession session, AlarmHisVo alarmHisVo
			, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		try {	

			alarmHisService.searchFormatDate(alarmHisVo);
			
			String comp_id = (String) session.getAttribute("comp_id");
			alarmHisVo.setComp_id(comp_id);
			
			alarmHisVo.setStartrow(-1);
			List<AlarmHisVo> excelList = this.alarmHisService.getAlarmHisList(alarmHisVo);

			String[] l_aColumns = {
					messageSource.getMessage("list.eventTime", null, locale), 
					messageSource.getMessage("list.severity", null, locale), 
					messageSource.getMessage("list.location", null, locale), 
					messageSource.getMessage("list.alarmMessage", null, locale), 
					messageSource.getMessage("list.firstTime", null, locale), 
					messageSource.getMessage("list.duplicates", null, locale), 
					messageSource.getMessage("list.actionAlarmhis", null, locale), 
					messageSource.getMessage("list.clearTime", null, locale), 
					messageSource.getMessage("list.operator", null, locale)
			};
			
			String xlsName = messageSource.getMessage("alarmhis.alarmHistory", null, locale);
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
			
			for(AlarmHisVo vo : excelList){
				row = sheet.createRow(rowIdx);

				colIdx = 0;
				
				Cell cell;
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getEvt_time());
				cell.setCellStyle(csBody);

				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getSeverity());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getLoc());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getAlarm_str());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getFirst_time());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getDup_cnt());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getClr_flag());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getClr_time());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getClr_user());
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
		
		String sql = "SELECT DATE_FORMAT(STR_TO_DATE(EVT_TIME, '%Y%m%d%H%i%s'), '%Y-%m-%d %H:%i:%s') EVT_TIME\r\n"
				+ "    		 , AID\r\n"
				+ "		     , (CASE WHEN SEVERITY = 'C' THEN 'CRITICAL' \r\n"
				+ "		  		     WHEN SEVERITY = 'M' THEN 'MAJOR' \r\n"
				+ "					 WHEN SEVERITY = 'N' THEN 'MINOR' END) SEVERITY\r\n"
				+ "		     , CONCAT(CASE WHEN LOC_A = 'OWM' THEN FNC_GET_SNAME(LOC_B) ELSE IFNULL(LOC_B,'') END,\r\n"
				+ "					  CASE WHEN LOC_C IS NOT NULL THEN CONCAT(', ', LOC_C) ELSE '' END) LOC\r\n"
				+ "		     , ALARM_STR\r\n"
				+ "		     , DATE_FORMAT(STR_TO_DATE(FIRST_TIME, '%Y%m%d%H%i%s'), '%Y-%m-%d %H:%i:%s') FIRST_TIME\r\n"
				+ "		     , DUP_CNT\r\n"
				+ "		     , (CASE WHEN CLR_FLAG = 'M' THEN 'User clear'  \r\n"
				+ "		  	         WHEN CLR_FLAG = 'A' THEN 'Auto clear' END) CLR_FLAG\r\n"
				+ "		     , DATE_FORMAT(STR_TO_DATE(CLR_TIME, '%Y%m%d%H%i%s'), '%Y-%m-%d %H:%i:%s') CLR_TIME\r\n"
				+ "		     , CLR_USER\r\n"
				+ "	      FROM TB_ALARM_SYS_HIS\r\n"
				+ "	     WHERE EVT_TIME BETWEEN '"+ alarmHisVo.getStartDate() +"' AND '"+ alarmHisVo.getEndDate() +"'\r\n";
		
		if(!alarmHisVo.getSearchAlarmCause().equals("") && alarmHisVo.getSearchAlarmCause() != null) {
			sql += "AND AID = '"+ alarmHisVo.getSearchAlarmCause() +"'\r\n";
		}
		if(!alarmHisVo.getComp_id().equals("0") && alarmHisVo.getComp_id() != null) {
			sql += "AND LOC_B IN ( \r\n"
					+ "	  	   		SELECT CC.S_CODE FROM TB_SCOMP_INFO BB, TB_SHIP_INFO CC\r\n"
					+ "				 WHERE BB.COMP_ID = CC.COMP_ID \r\n"
					+ "				   AND BB.COMP_ID = '"+ alarmHisVo.getComp_id() +"'\r\n"
					+ "			 )\r\n";
		}
		
		sql += "ORDER BY EVT_TIME DESC";
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("601");
		userHistory.setJob_name("엑셀");
		userHistory.setGui_remark("사유: " + alarmHisVo.getExcelMsg());
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str(sql);
		this.userHistoryService.insertUserHistory(userHistory);
	}
}
