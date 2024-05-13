package radius.athntHistory.controller;

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
import radius.athntHistory.model.AthntHistoryVo;
import radius.athntHistory.service.AthntHistoryService;
import radius.comm.paging;

@Controller
@RequestMapping("/athntHistory")
public class AthntHistoryController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "radius.athntHistory.athntHistoryService")
	private AthntHistoryService athntHistoryServicce;

	/**
	 * 인증이력
	 */
	@RequestMapping("athntHistory.do")
	public String athntHistory(@ModelAttribute("athntHistoryVo") AthntHistoryVo athntHistoryVo,
			HttpServletRequest request, final HttpSession session, HttpServletResponse response, Model model) throws Exception {
		
		if((String)session.getAttribute("user_id") == null || (String)session.getAttribute("user_id") == "") {
			response.sendRedirect("/");
		} else {
		
		athntHistoryVo.setStartrow((athntHistoryVo.getPageno() -1) * athntHistoryVo.getFetchrow());
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd"); 
		DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		
		if("true".equals(athntHistoryVo.getSearchChecked())){ 
	
	        String startDate = athntHistoryVo.getStartDate();
	        String endDate = athntHistoryVo.getEndDate();
	        
	        model.addAttribute("timeDefaultStart", startDate);
	        model.addAttribute("timeDefaultEnd", endDate);
	        
	        LocalDate fmtStartDate = LocalDate.parse(startDate, fmt2);
	        LocalDate fmtEndDate = LocalDate.parse(endDate, fmt2);
	        
	        startDate = fmtStartDate.format(fmt);
	        endDate = fmtEndDate.format(fmt);
	        
	        startDate += athntHistoryVo.getSearchStartHour() + athntHistoryVo.getSearchStartMin() + "00";
	        endDate += athntHistoryVo.getSearchEndHour() + athntHistoryVo.getSearchEndMin() + "59";
	        
	        athntHistoryVo.setStartDate(startDate);
	        athntHistoryVo.setEndDate(endDate);
	       
		} else {
			
			LocalDate now = LocalDate.now();
			String searchDefaultTime = now.format(fmt);
			
			String startDate = searchDefaultTime + "000000";
			String endDate = searchDefaultTime + "235959";
			athntHistoryVo.setStartDate(startDate);
			athntHistoryVo.setEndDate(endDate);
			
			athntHistoryVo.setSearchEndHour("23");
			athntHistoryVo.setSearchEndMin("59");
			
			String timeDefault = now.format(fmt2);
			
			model.addAttribute("timeDefaultStart", timeDefault);
			model.addAttribute("timeDefaultEnd", timeDefault);
		}
		
		// 인증 결과 리스트 조회
		List<AthntHistoryVo> replyList = this.athntHistoryServicce.selelctReplyList();
		model.addAttribute("replyList", replyList);

		// 결과 원인 조회 
		List<AthntHistoryVo> causeList = this.athntHistoryServicce.selectCauseList();
		model.addAttribute("causeList", causeList);
		
		// 인증이력 리스트
		List<AthntHistoryVo> athntHistoryList = this.athntHistoryServicce.selectAthntHistoryList(athntHistoryVo);
		model.addAttribute("athntHistoryList", athntHistoryList);
		
		// paging
		paging paging = new paging();
		
		int totalCnt = this.athntHistoryServicce.selectAccessCnt(athntHistoryVo);
		
		String pagingHTML = paging.getPagingStr(athntHistoryVo.getPageno(), athntHistoryVo.getFetchrow(), totalCnt);
		model.addAttribute("pagingHTML", pagingHTML);
		
		// search data return
		model.addAttribute("athntHistoryVo", athntHistoryVo);

		}
		return "athntHistory/athntHistory";
	}
	
	/**
	 * 엑셀 다운
	 */
	@RequestMapping(value="athntHistoryExcelDownload.do")
	public void athntHistoryExcelDownload(HttpServletResponse response, final HttpSession session, AthntHistoryVo athntHistoryVo) throws Exception {
		
		try {	
			
			DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd"); 
			
			String startDate = athntHistoryVo.getStartDate();
		    String endDate = athntHistoryVo.getEndDate();
		    
	        LocalDate fmtStartDate = LocalDate.parse(startDate, fmt2);
	        LocalDate fmtEndDate = LocalDate.parse(endDate, fmt2);
	        
	        startDate = fmtStartDate.format(fmt);
	        endDate = fmtEndDate.format(fmt);
	        
	        startDate += athntHistoryVo.getSearchStartHour() + athntHistoryVo.getSearchStartMin() + "00";
	        endDate += athntHistoryVo.getSearchEndHour() + athntHistoryVo.getSearchEndMin() + "59";
	        
	        athntHistoryVo.setStartDate(startDate);
	        athntHistoryVo.setEndDate(endDate);

			athntHistoryVo.setStartrow(-1);
			List<AthntHistoryVo> excelList = this.athntHistoryServicce.selectAthntHistoryList(athntHistoryVo);

			String[] l_aColumns = {"시간", "MSISDN", "IMSI", "Username", "인증 결과", "IP Address", "NAS IP", "NAS Port", "결과 원인"};
			
			String xlsName = "RADIUS 인증 이력";
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
			
			for(AthntHistoryVo vo : excelList){
				row = sheet.createRow(rowIdx);

				colIdx = 0;
				
				row.createCell(colIdx++).setCellValue(vo.getAuthdate());
				row.createCell(colIdx++).setCellValue(vo.getMsisdn());
				row.createCell(colIdx++).setCellValue(vo.getGpp_imsi());
				row.createCell(colIdx++).setCellValue(vo.getUserName());
				row.createCell(colIdx++).setCellValue(vo.getReply());
				row.createCell(colIdx++).setCellValue(vo.getFramed_ip_address());
				row.createCell(colIdx++).setCellValue(vo.getNas_ip_address());
				row.createCell(colIdx++).setCellValue(vo.getNas_port_no());
				row.createCell(colIdx++).setCellValue(vo.getCause_name());
				
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
