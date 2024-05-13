package radius.alarmHis.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import radius.alarmHis.model.AlarmHisVo;
import radius.alarmHis.service.AlarmHisService;

@Controller
@RequestMapping("/alarmHis")
public class AlarmHisController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "radius.alarmHis.alarmHisService")
	private AlarmHisService alarmHisService;
	
	/**
	 * 알람 이력
	 */
	@RequestMapping("/alarmHis.do")
	public String alarmSys(@ModelAttribute("alarmHisVo") AlarmHisVo alarmHisVo, final HttpSession session
			, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		
		if((String)session.getAttribute("user_id") == null || (String)session.getAttribute("user_id") == "") {
			response.sendRedirect("/");
		} else {

		alarmHisVo.setStartrow((alarmHisVo.getPageno() -1) * alarmHisVo.getFetchrow());
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd"); 
		DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		
		if("true".equals(alarmHisVo.getSearchChked())){ 
			
			String startDate = alarmHisVo.getStartDate();
	        String endDate = alarmHisVo.getEndDate();
	        
	        model.addAttribute("timeDefaultStart", startDate);
	        model.addAttribute("timeDefaultEnd", endDate);
	        
	        LocalDate fmtStartDate = LocalDate.parse(startDate, fmt2);
	        LocalDate fmtEndDate = LocalDate.parse(endDate, fmt2);
	        
	        startDate = fmtStartDate.format(fmt);
	        endDate = fmtEndDate.format(fmt);
	        
	        startDate += alarmHisVo.getSearchStartHour() + alarmHisVo.getSearchStartMin() + "00";
	        endDate += alarmHisVo.getSearchEndHour() + alarmHisVo.getSearchEndMin() + "59";
	        
	        alarmHisVo.setStartDate(startDate);
	        alarmHisVo.setEndDate(endDate);

		} else {
			
			LocalDate now = LocalDate.now();
			String searchDefaultTime = now.format(fmt);
			
			String startDate = searchDefaultTime + "000000";
			String endDate = searchDefaultTime + "235959";
			
			alarmHisVo.setStartDate(startDate);
			alarmHisVo.setEndDate(endDate);
			
			alarmHisVo.setSearchEndHour("23");
			alarmHisVo.setSearchEndMin("59");
			
			String timeDefault = now.format(fmt2);
			
			model.addAttribute("timeDefaultStart", timeDefault);
			model.addAttribute("timeDefaultEnd", timeDefault);
		}
		
		// alarmIdList
		List<AlarmHisVo> alarmIdList = this.alarmHisService.selectAlarmIdList(alarmHisVo);
		model.addAttribute("alarmIdList", alarmIdList);
		
		// alarmHisList
		List<AlarmHisVo> alarmHisList = this.alarmHisService.selectAlarmHisList(alarmHisVo);
		model.addAttribute("alarmHisList", alarmHisList);
		
		}
		return "alarmHis/alarmHis";
	}
	
	/**
	 * 엑셀 다운
	 */
	@RequestMapping(value="alarmHisExcelDownload.do")
	public void alarmHisExcelDownload(HttpServletResponse response, final HttpSession session, AlarmHisVo alarmHisVo) throws Exception {
		
		try {	
			
			DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd"); 
			
			String startDate = alarmHisVo.getStartDate();
		    String endDate = alarmHisVo.getEndDate();
		    
	        LocalDate fmtStartDate = LocalDate.parse(startDate, fmt2);
	        LocalDate fmtEndDate = LocalDate.parse(endDate, fmt2);
	        
	        startDate = fmtStartDate.format(fmt);
	        endDate = fmtEndDate.format(fmt);
	        
	        startDate += alarmHisVo.getSearchStartHour() + alarmHisVo.getSearchStartMin() + "00";
	        endDate += alarmHisVo.getSearchEndHour() + alarmHisVo.getSearchEndMin() + "59";
	        
	        alarmHisVo.setStartDate(startDate);
	        alarmHisVo.setEndDate(endDate);
	        
	        alarmHisVo.setStartrow(-1);
			List<AlarmHisVo> excelList = this.alarmHisService.selectAlarmHisList(alarmHisVo);
			
			String[] l_aColumns = {"발생 시간", "알람 아이디", "심각도", "알람 위치", "알람 메세지", "알람 원인", "최초 발생시간", "해제 시간", "사용자"};
			
			String xlsName = "RADIUS 알람 이력";
			SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		    String today = formatter.format(new java.util.Date());
			String fileName	 = xlsName+"_"+ today+".xlsx";
			
			// Workbook 생성
			Workbook wb = new XSSFWorkbook();

			// Sheet 생성
			Sheet sheet = wb.createSheet(xlsName);
			
			// 포맷
			//header
			CellStyle header_format = wb.createCellStyle();
			header_format.setAlignment(HorizontalAlignment.CENTER);
			header_format.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			header_format.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			header_format.setBorderTop(BorderStyle.THIN);
			header_format.setBorderBottom(BorderStyle.THIN);
			header_format.setBorderLeft(BorderStyle.THIN);
			header_format.setBorderRight(BorderStyle.THIN);
			
			//align center
			CellStyle  format_c	= wb.createCellStyle();
			format_c.setAlignment(HorizontalAlignment.CENTER);
			format_c.setBorderTop(BorderStyle.THIN);
			format_c.setBorderBottom(BorderStyle.THIN);
			format_c.setBorderLeft(BorderStyle.THIN);
			format_c.setBorderRight(BorderStyle.THIN);
			
			//align right
			CellStyle  format_r	= wb.createCellStyle();
			format_r.setAlignment(HorizontalAlignment.RIGHT);
			format_r.setBorderTop(BorderStyle.THIN);
			format_r.setBorderBottom(BorderStyle.THIN);
			format_r.setBorderLeft(BorderStyle.THIN);
			format_r.setBorderRight(BorderStyle.THIN);
			
			// 항목명
			int rowIdx = 0;
			int colIdx = 0;
			Row row = sheet.createRow(rowIdx);
			
			for(int i = 0; i < l_aColumns.length; i++ ) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(header_format);
				cell.setCellValue(l_aColumns[i]);
			}
			
			rowIdx++;
			
			for(AlarmHisVo vo : excelList){
				row = sheet.createRow(rowIdx);

				colIdx = 0;
				
				row.createCell(colIdx++).setCellValue(vo.getEvt_time());
				row.createCell(colIdx++).setCellValue(vo.getaId());
				row.createCell(colIdx++).setCellValue(vo.getSeverity());
				row.createCell(colIdx++).setCellValue(vo.getAlarmLocation());
				row.createCell(colIdx++).setCellValue(vo.getAlarm_str());
				row.createCell(colIdx++).setCellValue(vo.getAdd_str());
				row.createCell(colIdx++).setCellValue(vo.getFirst_time());
				row.createCell(colIdx++).setCellValue(vo.getClr_time());
				row.createCell(colIdx++).setCellValue(vo.getClr_user());
				
		
				for(int idx = 0; idx < colIdx; idx++) {
					Cell cell = row.getCell(idx);
					cell.setCellStyle(format_c);
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
		
	}

}
