package radius.athntStat.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import radius.athntStat.model.AthntStatVo;
import radius.athntStat.service.AthntStatService;
import radius.comm.paging;

@Controller
@RequestMapping("/athntStat")
public class AthntStatController {

private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "radius.athntStat.athntStatService")
	private AthntStatService athntStatService;
	
	/**
	 * 인증 통계
	 */
	@RequestMapping("athntStat.do")
	public String stat(@ModelAttribute("athntStatVo") AthntStatVo athntStatVo,
			HttpServletRequest request, final HttpSession session, HttpServletResponse response, Model model) throws Exception {
		
		
		if((String)session.getAttribute("user_id") == null || (String)session.getAttribute("user_id") == "") {
			response.sendRedirect("/");
		} else {
		
		athntStatVo.setStartrow((athntStatVo.getPageno() -1) * athntStatVo.getFetchrow());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); 
		DateTimeFormatter hyphenFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		
		//검색일 경우 시간 설정 
		if("true".equals(athntStatVo.getSearchChecked())){ 

		    String startDate = athntStatVo.getStartDate(); 
		    String endDate = athntStatVo.getEndDate(); 

		    model.addAttribute("timeDefaultStart", startDate); 
		    model.addAttribute("timeDefaultEnd", endDate); 

		    // 문자열을 LocalDateTime 객체로 변환 
		    LocalDate formatterStartDate = LocalDate.parse(startDate, hyphenFormatter); 
		    LocalDate foramtterEndDate = LocalDate.parse(endDate, hyphenFormatter); 

		    startDate = formatterStartDate.format(formatter); 
		    endDate = foramtterEndDate.format(formatter); 

		    //검색 조건 별 시간 설정 
		    String searchUnits = athntStatVo.getSearchUnits(); 
		    if(searchUnits.equals("min")){ 
		        startDate += athntStatVo.getSearchStartHour() + athntStatVo.getSearchStartMin() + "00"; 
		        endDate += athntStatVo.getSearchEndHour() + athntStatVo.getSearchEndMin() + "59"; 
		    } else if (searchUnits.equals("hour")) { 
		        startDate += athntStatVo.getSearchStartHour() + "0000"; 
		        endDate += athntStatVo.getSearchEndHour() + "5959"; 
		    } else if (athntStatVo.getSearchUnits().equals("day")) { 
		        startDate += "000000"; 
		        endDate += "235959"; 
		    } else if (searchUnits.equals("week")) { 
		        startDate = this.athntStatService.selectStatWeek(startDate) + "000000"; 
		        endDate += "235959"; 
		    } else if (searchUnits.equals("month")) { 
		        startDate = startDate.substring(0, 6); 
		        endDate = endDate.substring(0, 6); 
		        startDate += "01"; 
		        endDate += "31"; 
		    }else { 
		        return "athntStat/athntStat"; 
		    } 
		    athntStatVo.setStartDate(startDate); 
		    athntStatVo.setEndDate(endDate);	
		    
		    // 동적 테이블
		    if (!athntStatVo.getSearchStatType().contains("인증결과")){
		    	athntStatVo.setChkedReplyList(false);
            }
            if (!athntStatVo.getSearchStatType().contains("NAS IP")) {
            	athntStatVo.setChkedNasIpList(false);
            } 
            if (!athntStatVo.getSearchStatType().contains("결과원인")) {
            	athntStatVo.setChkedCauseList(false);
            }        
            if(athntStatVo.getSearchStatType().contains("전체 항목")){
            	athntStatVo.setChkedReplyList(true);
            	athntStatVo.setChkedNasIpList(true);
            	athntStatVo.setChkedCauseList(true);
            }
		} else {
			// 페이지 초기 상태
			athntStatVo.setSearchUnits("hour");
			athntStatVo.setSearchStatType("인증결과, NAS IP, 결과원인");
			athntStatVo.setChkedReplyList(true);
			athntStatVo.setChkedNasIpList(true);
			athntStatVo.setChkedCauseList(true);			
			
			//검색 날짜
            LocalDate nowTime = LocalDate.now();
            String searchDefaultTime = nowTime.format(formatter);

            String startDate = searchDefaultTime + "000000";
            String endDate = searchDefaultTime + "235959";
            athntStatVo.setStartDate(startDate);
            athntStatVo.setEndDate(endDate);
            
            //페이징 처리를 위한 기본값 설정
            athntStatVo.setSearchEndHour("23");
            athntStatVo.setSearchEndMin("59");

            DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String timeDefault = nowTime.format(defaultFormatter);

            model.addAttribute("timeDefaultStart", timeDefault);
            model.addAttribute("timeDefaultEnd", timeDefault);
		}
		
		// 테이블명 조회
		String tableName = this.athntStatService.selectTableName(athntStatVo);

		if(tableName == null || tableName.equals("( )")) {
			return "athentStat/athentStat";
		} else 
			athntStatVo.setTableName(tableName);
		
		// 인증 결과 조회
		List<AthntStatVo> replyList = this.athntStatService.selelctReplyList(athntStatVo);
		model.addAttribute("replyList", replyList);
				
		// 결과 원인 조회 
		List<AthntStatVo> causeList = this.athntStatService.selectCauseList(athntStatVo);
		model.addAttribute("causeList", causeList);
				
		// statList 조회
		List<AthntStatVo> statList = this.athntStatService.selectStatList(athntStatVo);
		model.addAttribute("statList", statList);

		// 페이징 
		paging paging = new paging();
		
		int totalCnt = this.athntStatService.selectStatCnt(athntStatVo);
		
		String pagingHTML = paging.getPagingStr(athntStatVo.getPageno(), athntStatVo.getFetchrow(), totalCnt);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("pagingHTML", pagingHTML);
		
		if(totalCnt == 0) {
			athntStatVo.setChkedReplyList(true);
        	athntStatVo.setChkedNasIpList(true);
        	athntStatVo.setChkedCauseList(true);
		}
		
		// search data return
		model.addAttribute("athntStatVo", athntStatVo);
		
		}
		return "athntStat/athntStat";
	}
	
	/**
	 * 엑셀 다운
	 */
	@RequestMapping(value="athntStatExcelDownload.do")
	public void athntStatExcelDownload(HttpServletResponse response, final HttpSession session, AthntStatVo athntStatVo) throws Exception {
		
		try {	
			
			// 날짜 포맷 설정
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); 
			DateTimeFormatter hyphenFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			String startDate = athntStatVo.getStartDate();
            String endDate = athntStatVo.getEndDate();
            
            // 문자열을 LocalDateTime 객체로 변환 
		    LocalDate formatterStartDate = LocalDate.parse(startDate, hyphenFormatter); 
		    LocalDate foramtterEndDate = LocalDate.parse(endDate, hyphenFormatter); 

		    // yyyyMMdd 변경
		    startDate = formatterStartDate.format(formatter); 
		    endDate = foramtterEndDate.format(formatter); 
		    
		    String searchUnits = athntStatVo.getSearchUnits(); 
		    if(searchUnits.equals("min")){ 
		        startDate += athntStatVo.getSearchStartHour() + athntStatVo.getSearchStartMin() + "00"; 
		        endDate += athntStatVo.getSearchEndHour() + athntStatVo.getSearchEndMin() + "59"; 
		    } else if (searchUnits.equals("hour")) { 
		        startDate += athntStatVo.getSearchStartHour() + "0000"; 
		        endDate += athntStatVo.getSearchEndHour() + "5959"; 
		    } else if (athntStatVo.getSearchUnits().equals("day")) { 
		        startDate += "000000"; 
		        endDate += "235959"; 
		    } else if (searchUnits.equals("week")) { 
		        startDate = this.athntStatService.selectStatWeek(startDate) + "000000"; 
		        endDate += "235959"; 
		    } else if (searchUnits.equals("month")) { 
		        startDate = startDate.substring(0, 6); 
		        endDate = endDate.substring(0, 6); 
		        startDate += "01"; 
		        endDate += "31"; 
		    }

		    if (!athntStatVo.getSearchStatType().contains("인증결과")){
		    	athntStatVo.setChkedReplyList(false);
            }
            if (!athntStatVo.getSearchStatType().contains("NAS IP")) {
            	athntStatVo.setChkedNasIpList(false);
            }
            if (!athntStatVo.getSearchStatType().contains("결과원인")) {
            	athntStatVo.setChkedCauseList(false);
            }
            if(athntStatVo.getSearchStatType().contains("전체 항목")){
            	athntStatVo.setChkedReplyList(true);
            	athntStatVo.setChkedNasIpList(true);
            	athntStatVo.setChkedCauseList(true);
            }
			
			athntStatVo.setStartDate(startDate);
			athntStatVo.setEndDate(endDate);
			athntStatVo.setStartrow(-1);
			List<AthntStatVo> excelList = this.athntStatService.selectStatList(athntStatVo);
			
			List<String> col = new ArrayList<>();
			col.add("시간");
			if(athntStatVo.getChkedReplyList()) {
				col.add("인증 결과");
			}
			if(athntStatVo.getChkedNasIpList()) {
				col.add("NAS IP");
			}
			if(athntStatVo.getChkedCauseList()) {
				col.add("결과 원인");
			}
			col.add("인증 시도수");
			col.add("성공수");
			col.add("실패수");

			String[] l_aColumns = col.toArray(new String[0]);
			
			String xlsName = "RADIUS 인증 통계";
			SimpleDateFormat excelFormatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		    String today = excelFormatter.format(new java.util.Date());
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
			
			for(AthntStatVo vo : excelList){
				row = sheet.createRow(rowIdx);

				colIdx = 0;
				
				row.createCell(colIdx++).setCellValue(vo.getStat_time());
				
				if(athntStatVo.getChkedReplyList()) {
					row.createCell(colIdx++).setCellValue(vo.getReply());
				}
				if(athntStatVo.getChkedNasIpList()) {
					row.createCell(colIdx++).setCellValue(vo.getNas_ip_address());
				}
				if(athntStatVo.getChkedCauseList()) {
					row.createCell(colIdx++).setCellValue(vo.getCause_name());
				}
				
				row.createCell(colIdx++).setCellValue(String.valueOf(vo.getAuth_cnt()));
				row.createCell(colIdx++).setCellValue(String.valueOf(vo.getSuccess_cnt()));
				row.createCell(colIdx++).setCellValue(String.valueOf(vo.getFail_cnt()));

				
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
