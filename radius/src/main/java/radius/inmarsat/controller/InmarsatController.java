package radius.inmarsat.controller;

import java.text.DecimalFormat;
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
import radius.comm.paging;
import radius.inmarsat.model.InmarsatVo;
import radius.inmarsat.service.InmarsatService;

@Controller
@RequestMapping("/inmarsat")
public class InmarsatController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
		
	@Resource(name = "radius.inmarsat.inmarsatService")
	private InmarsatService inmarsatService;
	
	/**
	 * inmarsat 관리
	 */
	@RequestMapping("inmarsat.do")
	public String alarmConf(@ModelAttribute("inmarsatVo") InmarsatVo inmarsatVo,
			HttpServletRequest request, final HttpSession session, HttpServletResponse response, Model model) throws Exception {
		
		if((String)session.getAttribute("user_id") == null || (String)session.getAttribute("user_id") == "") {
			response.sendRedirect("/");
		} else {
		
			inmarsatVo.setStartrow((inmarsatVo.getPageno() -1) * inmarsatVo.getFetchrow());
			
			// inmarsatList
			List<InmarsatVo> inmarsatList = this.inmarsatService.selectInmarsatList(inmarsatVo);
			model.addAttribute("inmarsatList", inmarsatList);
			
			// 페이징
			paging paging = new paging();
					
			// 리스트 카운트
			int totalCnt = this.inmarsatService.selectInmarsatCnt(inmarsatVo);
					
			String pagingHTML = paging.getPagingStr(inmarsatVo.getPageno(), inmarsatVo.getFetchrow(), totalCnt);
					
			model.addAttribute("totalCnt", totalCnt);
			model.addAttribute("pagingHTML", pagingHTML);
					
			model.addAttribute("inmarsatVo", inmarsatVo);
		}
		
		return "inmarsat/inmarsat";
	}
	
	/**
	 * 등록
	 */
	@ResponseBody
	@RequestMapping(value = "inmarsatAdd.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> inmarsatAdd(InmarsatVo inmarsatVo) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {		
			// 중복검사
			int chkValues = this.inmarsatService.chkInsertValues(inmarsatVo).size();
			
			if(chkValues > 0) {
				resultMap.put("result", "-2");
			} else {
			
				// 등록
				int insResult = this.inmarsatService.insertInmarsat(inmarsatVo);
	
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
	@RequestMapping(value = "inmarsatSetUpdateData.ajax", method = RequestMethod.POST)
	public HashMap<String, Object> inmarsatSetUpdateData(@RequestParam HashMap<String, Object> req, Model model) throws Exception{
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			// 상세페이지
			InmarsatVo inmarsatVo = this.inmarsatService.selectInmarsatInfo(req);
						
			resultMap.put("inmarsatVo", inmarsatVo);
			resultMap.put("result", "success");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "error");
		}
		return resultMap;
	}
	
	/**
	 * 수정
	 */
	@ResponseBody
	@RequestMapping(value="inmarsatUpdate.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> inmarsatUpdate(InmarsatVo inmarsatVo, HttpServletRequest request) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			//update
			int updResult = this.inmarsatService.updateInmarsat(inmarsatVo);
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
	 * 삭제
	 */
	@ResponseBody
	@RequestMapping(value="inmarsatDelete.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> inmarsatDelete(InmarsatVo inmarsatVo,HttpServletRequest request) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			//update
			int updResult = this.inmarsatService.deleteInmarsat(inmarsatVo);
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
	 * Excel
	 */
	@RequestMapping(value="inmarsatExcelDownload.do")
	public void inmarsatExcelDownload(HttpServletResponse response, final HttpSession session, InmarsatVo inmarsatVo) throws Exception {
		
		try {	
			
			inmarsatVo.setStartrow(-1);
			List<InmarsatVo> excelList = this.inmarsatService.selectInmarsatList(inmarsatVo);

			String[] l_aColumns = {"Groupname", "Attribute", "OP", "Value"};
			
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
			
			for(InmarsatVo vo : excelList){
				row = sheet.createRow(rowIdx);

				colIdx = 0;
				
				DecimalFormat df = new DecimalFormat("###,###");
				
				String interval = vo.getValue();
				
				if(vo.getValue() != null) {
					String dfStr = df.format(Integer.parseInt(interval));
					vo.setValue(dfStr);
				}
				
				row.createCell(colIdx++).setCellValue(vo.getGroupName());
				row.createCell(colIdx++).setCellValue(vo.getAttribute());
				row.createCell(colIdx++).setCellValue(vo.getOp());
				row.createCell(colIdx++).setCellValue(vo.getValue());

				
				
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