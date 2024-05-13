package radius.radacctInfo.controller;

import java.text.DecimalFormat;
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
import radius.comm.paging;
import radius.radacctInfo.model.RadacctInfoVo;
import radius.radacctInfo.service.RadacctInfoService;

@Controller
@RequestMapping("/radacctInfo")
public class RadacctInfoController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "radius.radacctInfo.radacctInfoService")
	private RadacctInfoService radacctInfoService;

	@RequestMapping("radacctInfo.do")
	public String radacctInfo(@ModelAttribute("radacctInfoVo") RadacctInfoVo radacctInfoVo,
			HttpServletRequest request, final HttpSession session, HttpServletResponse response, Model model) throws Exception {
		
		if((String)session.getAttribute("user_id") == null || (String)session.getAttribute("user_id") == "") {
			response.sendRedirect("/");
		} else {
			radacctInfoVo.setStartrow((radacctInfoVo.getPageno() -1) * radacctInfoVo.getFetchrow());
			
			// Account Status 조회
			List<RadacctInfoVo> acctStatusList = radacctInfoService.selectAcctStatusList(radacctInfoVo);
			model.addAttribute("acctStatusList", acctStatusList);
			
			// radacctInfoList 조회
			List<RadacctInfoVo> radacctInfoList = radacctInfoService.selectRadacctInfoList(radacctInfoVo);
			model.addAttribute("radacctInfoList", radacctInfoList);
			
			// 페이징
			paging paging = new paging();
			
			// 리스트 카운트
			int totalCnt = this.radacctInfoService.selectRadacctInfoListCnt(radacctInfoVo);
			
			String pagingHTML = paging.getPagingStr(radacctInfoVo.getPageno(), radacctInfoVo.getFetchrow(), totalCnt);
			
			model.addAttribute("totalCnt", totalCnt);
			model.addAttribute("pagingHTML", pagingHTML);
		}
		
		return "radacctInfo/radacctInfo";
	}

	
	/**
	 * 엑셀 다운
	 */
	@RequestMapping(value="radacctInfoExcelDownload.do")
	public void radacctInfoExcelDownload(HttpServletResponse response, final HttpSession session, RadacctInfoVo radacctInfoVo) throws Exception {
		
		try {	
			radacctInfoVo.setStartrow(-1);
			
			List<RadacctInfoVo> excelList = this.radacctInfoService.selectRadacctInfoList(radacctInfoVo);

			String[] l_aColumns = {"SessionID", "Username", "IMSI", "MSISDN", "Groupname", "Realms", "NAS IP", "NAS Port IP", "NAS Port type"
					,"Update time", "Interval", "Session time", "Authentic", "Connect Info Start", "Connect Info Stop"
					, "Input Octets", "Output Octets", "Input Packets", "Output Packets", "Called Station ID", "Calling Station ID"
					, "Status", "Terminate Cause", "Service Type", "Framed Protocol", "Framed IP", "GPP SGSN", "GPP GGSN", "GPP Charging ID"};
			
			String xlsName = "RADIUS 인증 현황";
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
			
			for(RadacctInfoVo vo : excelList){
				row = sheet.createRow(rowIdx);

				colIdx = 0;
				
				// 숫자 콤마
				DecimalFormat df = new DecimalFormat("###,###");
				
				String interval = vo.getAcctInterval();
				if(vo.getAcctInterval() != null) {
					String dfStr1 = df.format(Integer.parseInt(interval));
					vo.setAcctInterval(dfStr1);
				} 
				
				String sessionTime = vo.getAcctSessionTime();
				String dfStr2 = df.format(Integer.parseInt(sessionTime));
				vo.setAcctSessionTime(dfStr2);
				
				String inputOctets = vo.getAcctInputOctets();
				String dfStr3 = df.format(Integer.parseInt(inputOctets));
				vo.setAcctInputOctets(dfStr3);
				
				String outOctets = vo.getAcctOutputOctets();
				String dfStr4 = df.format(Integer.parseInt(outOctets));
				vo.setAcctOutputOctets(dfStr4);
				
				row.createCell(colIdx++).setCellValue(vo.getAcctSessionId());
				row.createCell(colIdx++).setCellValue(vo.getUserName());
				row.createCell(colIdx++).setCellValue(vo.getGppImsi());
				row.createCell(colIdx++).setCellValue(vo.getMsisdn());
				row.createCell(colIdx++).setCellValue(vo.getGroupName());
				row.createCell(colIdx++).setCellValue(vo.getRealm());
				row.createCell(colIdx++).setCellValue(vo.getNasIpAddress());
				row.createCell(colIdx++).setCellValue(vo.getNasPortId());
				row.createCell(colIdx++).setCellValue(vo.getNasPortType());
				row.createCell(colIdx++).setCellValue(vo.getAcctUpdateTime());
				row.createCell(colIdx++).setCellValue(vo.getAcctInterval());
				row.createCell(colIdx++).setCellValue(vo.getAcctSessionTime());
				row.createCell(colIdx++).setCellValue(vo.getAcctAuthentic());
				row.createCell(colIdx++).setCellValue(vo.getConnectInfoStart());
				row.createCell(colIdx++).setCellValue(vo.getConnectInfoStop());
				row.createCell(colIdx++).setCellValue(vo.getAcctInputOctets());
				row.createCell(colIdx++).setCellValue(vo.getAcctOutputOctets());
				row.createCell(colIdx++).setCellValue(String.valueOf(vo.getAcctInputPackets()));
				row.createCell(colIdx++).setCellValue(String.valueOf(vo.getAcctOutputPackets()));
				row.createCell(colIdx++).setCellValue(vo.getCalledStationId());
				row.createCell(colIdx++).setCellValue(vo.getCallingStationId());
				row.createCell(colIdx++).setCellValue(vo.getAcctStatus());
				row.createCell(colIdx++).setCellValue(vo.getAcctTerminateCause());
				row.createCell(colIdx++).setCellValue(vo.getServiceType());
				row.createCell(colIdx++).setCellValue(vo.getFramedProtocol());
				row.createCell(colIdx++).setCellValue(vo.getFramedIpAddress());
				row.createCell(colIdx++).setCellValue(vo.getGppSgsnAddress());
				row.createCell(colIdx++).setCellValue(vo.getGppGgsnAdress());
				row.createCell(colIdx++).setCellValue(vo.getGppChargingId());
				
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
