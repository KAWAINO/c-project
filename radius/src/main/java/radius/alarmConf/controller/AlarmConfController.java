package radius.alarmConf.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import radius.alarmConf.model.AlarmConfVo;
import radius.alarmConf.service.AlarmConfService;
import radius.comm.paging;

@Controller
@RequestMapping("/alarmConf")
public class AlarmConfController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "radius.alarmConf.alarmConfService")
	private AlarmConfService alarmConfService;
	
	/**
	 * 인증 알람 설정
	 */
	@RequestMapping("alarmConf.do")
	public String alarmConf(@ModelAttribute("alarmConfVo") AlarmConfVo alarmConfVo,
			HttpServletRequest request, final HttpSession session, HttpServletResponse response, Model model) throws Exception {
		
		if((String)session.getAttribute("user_id") == null || (String)session.getAttribute("user_id") == "") {
			response.sendRedirect("/");
		} else {
		
			alarmConfVo.setStartrow((alarmConfVo.getPageno() -1) * alarmConfVo.getFetchrow());
				
			// AlarmConfList 조회
			List<AlarmConfVo> alarmConfList = this.alarmConfService.selectAlarmConfList(alarmConfVo);		
			model.addAttribute("alarmConfList", alarmConfList);
			
			// 페이징
			paging paging = new paging();
			
			// 리스트 카운트
			int totalCnt = this.alarmConfService.selectAlarmConfCnt(alarmConfVo);
			
			String pagingHTML = paging.getPagingStr(alarmConfVo.getPageno(), alarmConfVo.getFetchrow(), totalCnt);
			
			model.addAttribute("totalCnt", totalCnt);
			model.addAttribute("pagingHTML", pagingHTML);
			model.addAttribute("alarmConfVo", alarmConfVo);
			
		}	
		return "alarmConf/alarmConf";
	}
	
	/**
	 * alarmConf 등록
	 */
	@ResponseBody
	@RequestMapping(value = "alarmConfAdd.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> alarmConfAdd(AlarmConfVo alarmConfVo, HttpServletRequest request
			, HttpServletResponse response) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			// period & auth_flag 중복검사
			int chkValues = this.alarmConfService.chkInsertValues(alarmConfVo).size();
			
			if(chkValues > 0) {
				resultMap.put("result", "-2");
			} else {
			
				// 등록
				int insResult = this.alarmConfService.insertAlarmConf(alarmConfVo);
	
				if(insResult > 0) {
					resultMap.put("result", "1");		
				} else {
					resultMap.put("result", "0");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			resultMap.put("result", "0");
		} 
		return resultMap;
	}
	
	/**
	 * 수정 Modal 이동
	 */
	@ResponseBody
	@RequestMapping(value = "alarmConfSetUpdateData.ajax", method = RequestMethod.POST)
	public HashMap<String, Object> alarmConfSetUpdateData(@RequestParam HashMap<String, Object> req, Model model) throws Exception{
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			// 상세페이지
			AlarmConfVo alarmConfVo = this.alarmConfService.selectAlarmConfInfo(req);
						
			resultMap.put("alarmConfVo", alarmConfVo);
			resultMap.put("result", "success");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "error");
		}
		return resultMap;
	}
	
	/**
	 * 알람설정 수정
	 */
	@ResponseBody
	@RequestMapping(value="alarmConfUpdate.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> alarmConfUpdate(AlarmConfVo alarmConfVo,HttpServletRequest request) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			//update
			int updResult = this.alarmConfService.updateAlarmConf(alarmConfVo);
			logger.info("======================================================"+updResult);
			if(updResult > 0) {
				resultMap.put("result", "1");
			}else {
				resultMap.put("result", "0");
			}
		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "0");
 		}	
		return resultMap;
	}
	
	/**
	 * 알람설정 삭제
	 */
	@ResponseBody
	@RequestMapping(value="alarmConfDelete.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> alarmConfDelete(AlarmConfVo alarmConfVo,HttpServletRequest request) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			//update
			int updResult = this.alarmConfService.deleteAlarmConf(alarmConfVo);
			if(updResult > 0) {
				resultMap.put("result", "1");
			}else {
				resultMap.put("result", "0");
			}
		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "0");
 		}
		
		return resultMap;
	}
	
	/**
	 * 알람설정 Excel
	 */
	@RequestMapping(value="alarmConfExcelDownload.do")
	public void suserExcelDownload(HttpServletResponse response, final HttpSession session, AlarmConfVo alarmConfVo) throws Exception {
		
		try {	
			
			alarmConfVo.setStartrow(-1);
			List<AlarmConfVo> excelList = this.alarmConfService.selectAlarmConfList(alarmConfVo);

			String[] l_aColumns = {"감시 주기(분)", "알람 발생 사유", "임계치", "사용 여부", "설명"};
			
			String xlsName = "알람 설정";
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
			
			for(AlarmConfVo vo : excelList){
				row = sheet.createRow(rowIdx);

				colIdx = 0;
				
				if(vo.getAuth_flag().contains("A")) {
					vo.setAuth_flag("인증 시도수 초과");
				} else if(vo.getAuth_flag().contains("F")) {
					vo.setAuth_flag("인증 실패수 초과");
				} else if(vo.getAuth_flag().contains("S")) {
					vo.setAuth_flag("인증 성공수 초과");
				}
				
				row.createCell(colIdx++).setCellValue(vo.getPeriod());
				row.createCell(colIdx++).setCellValue(vo.getAuth_flag());
				row.createCell(colIdx++).setCellValue(vo.getAuth_cnt());
				row.createCell(colIdx++).setCellValue(vo.getUse_yn());
				row.createCell(colIdx++).setCellValue(vo.getDescr());
				
				
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
