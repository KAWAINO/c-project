package radius.alarmSys.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import radius.alarmHis.model.AlarmHisVo;
import radius.alarmSys.model.AlarmSysVo;
import radius.alarmSys.service.AlarmSysService;
import radius.comm.paging;

@Controller
@RequestMapping("/alarmSys")
public class AlarmSysController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "radius.alarmSys.alarmSysService")
	private AlarmSysService alarmSysService;

	/**
	 * 알람 현황
	 */
	@RequestMapping("/alarmSys.do")
	public String alarmSys(@ModelAttribute("alarmSysVo") AlarmSysVo alarmSysVo, final HttpSession session
			, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		
		if((String)session.getAttribute("user_id") == null || (String)session.getAttribute("user_id") == "") {
			response.sendRedirect("/");
		} else {

			alarmSysVo.setStartrow((alarmSysVo.getPageno() -1) * alarmSysVo.getFetchrow());
	
			// alarmIdList
			List<AlarmSysVo> alarmIdList = this.alarmSysService.selectAlarmIdList(alarmSysVo);
			model.addAttribute("alarmIdList", alarmIdList);

			// alarmSysList
			List<AlarmSysVo> alarmSysList = this.alarmSysService.selectAlarmSysList(alarmSysVo);
			model.addAttribute("alarmSysList", alarmSysList);
	
			// 페이징
			paging paging = new paging();
			
			// 리스트 카운트
			int totalCnt = this.alarmSysService.selectAlarmSysCnt(alarmSysVo);
			
			String pagingHTML = paging.getPagingStr(alarmSysVo.getPageno(), alarmSysVo.getFetchrow(), totalCnt);
			
			model.addAttribute("totalCnt", totalCnt);
			model.addAttribute("pagingHTML", pagingHTML);
			
			model.addAttribute("alarmSysVo", alarmSysVo);
		
		}
		return "alarmSys/alarmSys";	
	}
	
	// 알람 리스트
//	@ResponseBody
//	@RequestMapping(value = "getAlarmSysList.ajax", method = RequestMethod.POST)
//	public List<AlarmSysVo> getAlarmSysList(AlarmSysVo alarmSysVo, Model model
//			, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		
//		// alarmSysList
//		List<AlarmSysVo> alarmSysList = this.alarmSysService.selectAlarmSysList(alarmSysVo);
//		
//		for(int i = 0; i < alarmSysList.size(); i++) {
//			logger.info("==========================================="+alarmSysList.get(i).getSeverity());
//		}
//
//		request.setAttribute("alarmSysList", alarmSysList);
//		
//		model.addAttribute("alarmSysList", alarmSysList);
//		
//		return alarmSysList;
//	}
	
	// 알람 헤더 
	@ResponseBody
	@RequestMapping(value = "getAlarmHeader.ajax", method = RequestMethod.POST)
	public List<AlarmSysVo> getAlarmHeader(AlarmSysVo alarmSysVo, Model model
			, HttpServletRequest request, HttpServletResponse response) throws Exception {	
		
		List<AlarmSysVo> sevCntList = this.alarmSysService.selectSevCntList(alarmSysVo);
		
		model.addAttribute("sevCntList", sevCntList);
					
		return sevCntList;
	}
	
	// 알람 헤제
	@ResponseBody
	@RequestMapping(value = "deleteAlarmSysList.ajax", method = RequestMethod.POST)
	public HashMap<String, Object> deleteAlarmSysList(AlarmSysVo alarmSysVo, HttpSession session) throws Exception {
		
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {

            String[] arr = alarmSysVo.getIdx().toString().split(",");

            for(int i = 0; i < arr.length; i++) {

                alarmSysVo.setUserId((String)session.getAttribute("user_id"));

                alarmSysVo.setIdx(arr[i].toString());

                int insertResult = this.alarmSysService.insertAlarmHistory(alarmSysVo);
                logger.info("========================================="+ insertResult);
                if (insertResult == 0) {
                    resultMap.put("result", "0");
                    return resultMap;
                } else {
                	
                    int delResult = this.alarmSysService.deleteAlarmSysList(alarmSysVo);
                    logger.info("========================================="+ delResult);
                    if(delResult > 0) {
                        resultMap.put("result", "1");
                    } else {
                        resultMap.put("result", "0");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "0");
        }
        return resultMap;
    }
			
	/**
	 * 엑셀 다운
	 */
	@RequestMapping(value="alarmSysExcelDownload.do")
	public void alarmSysExcelDownload(HttpServletResponse response, final HttpSession session, AlarmSysVo alarmSysVo) throws Exception {
		
		try {	
	        
	        alarmSysVo.setStartrow(-1);
			List<AlarmSysVo> excelList = this.alarmSysService.selectAlarmSysList(alarmSysVo);
			
			String[] l_aColumns = {"발생 시간", "알람 아이디", "심각도", "알람 위치", "알람 메세지", "알람 원인", "최초 발생시간"};
			
			String xlsName = "RADIUS 알람 현황";
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
			
			for(AlarmSysVo vo : excelList){
				row = sheet.createRow(rowIdx);

				colIdx = 0;
				
				row.createCell(colIdx++).setCellValue(vo.getEvt_time());
				row.createCell(colIdx++).setCellValue(vo.getaId());
				row.createCell(colIdx++).setCellValue(vo.getSeverity());
				row.createCell(colIdx++).setCellValue(vo.getAlarmLocation());
				row.createCell(colIdx++).setCellValue(vo.getAlarm_str());
				row.createCell(colIdx++).setCellValue(vo.getAdd_str());
				row.createCell(colIdx++).setCellValue(vo.getFirst_time());
		
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
