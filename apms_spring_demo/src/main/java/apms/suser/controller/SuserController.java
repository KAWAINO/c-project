package apms.suser.controller;

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

import apms.comm.paging;
import apms.suser.model.GradeVo;
import apms.suser.model.SuserVo;
import apms.suser.service.SuserService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/suser")
public class SuserController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="apms.suser.suserService")
	private SuserService suserService;
	
	/**
	 * 운영자 관리 페이지 로딩
	 */
	@RequestMapping("suser.do")
	public String suser(@ModelAttribute("suserVo") SuserVo suserVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
		//운영자 등급 목록
		List<GradeVo> gradeList = this.suserService.selectUserGrade();
		model.addAttribute("gradeList", gradeList);

		//운영자 목록 리스트
		suserVo.setStartrow((suserVo.getPageno() - 1) * suserVo.getFetchrow());
		suserVo.setS_code((String)session.getAttribute("scode"));
		List<SuserVo> suserList = this.suserService.selectUserList(suserVo);
		model.addAttribute("suserList", suserList);
		
		//paging
		paging paging = new paging();
		String pagingHTML = paging.getPagingStr(suserVo.getPageno(), suserVo.getFetchrow() , suserList.size());
		model.addAttribute("pagingHTML", pagingHTML);
		
		//search data return
		model.addAttribute("suserVo", suserVo);
		
		return "suser/suser";
	}
	
	/**
	 * 운영자 등록
	 */
	@ResponseBody
	@RequestMapping(value="suserAdd.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> suserAdd(SuserVo suserVo,HttpServletRequest request, final HttpSession session,Model model) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		suserVo.setS_code((String)session.getAttribute("scode"));

		try {
			//중복검사
			int chkId = this.suserService.chkInsertSuser(suserVo).size();
			if(chkId > 0) {
				resultMap.put("result", "-2");
			}else {
				//insert
				int insResult = this.suserService.insertSuser(suserVo);
				if(insResult > 0) {
					resultMap.put("result", "1");
				}else {
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
    @RequestMapping(value = "suserSetUpdateData.ajax", method = RequestMethod.POST)
 	public HashMap<String, Object> suserSetUpdateData(@RequestParam HashMap<String, Object> req, final HttpSession session) throws Exception {
    	HashMap<String, Object> resultMap = new HashMap<String, Object>();
 		
 		try {
 			String s_code = (String)session.getAttribute("scode");
 			req.put("s_code", s_code);
 			
 			SuserVo suserVo = this.suserService.selectSuserInfo(req);
 			
 			resultMap.put("suserVo", suserVo);
 			resultMap.put("result", "success");
 		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "error");
 		}
 	    return resultMap;
 	}
	
	/**
	 * 운영자 수정
	 */
	@ResponseBody
	@RequestMapping(value="suserUpdate.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> suserUpdate(SuserVo suserVo,HttpServletRequest request, final HttpSession session,Model model) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		suserVo.setS_code((String)session.getAttribute("scode"));

		try {
			//update
			int updResult = this.suserService.updateSuser(suserVo);
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
	 * 운영자 삭제
	 */
	@ResponseBody
	@RequestMapping(value="suserDelete.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> suserDelete(SuserVo suserVo,HttpServletRequest request, final HttpSession session,Model model) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		suserVo.setS_code((String)session.getAttribute("scode"));

		try {
			//update
			int updResult = this.suserService.deleteSuser(suserVo);
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
	 * 운영자 Excel
	 */
	@RequestMapping(value="suserExcelDownload.do")
	public void suserExcelDownload(HttpServletResponse response, final HttpSession session, SuserVo suserVo) throws Exception {
		
		try {
			suserVo.setS_code((String)session.getAttribute("scode"));
			suserVo.setStartrow(-1);
			List<SuserVo> excelList = this.suserService.selectUserList(suserVo);

			String[] l_aColumns = {"운영자 ID", "이름", "등급"};
			
			String xlsName = "OWM 운영자 관리";
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
			
			for(SuserVo vo : excelList){
				row = sheet.createRow(rowIdx);

				colIdx = 0;
				
				row.createCell(colIdx++).setCellValue(vo.getUser_id());
				row.createCell(colIdx++).setCellValue(vo.getUser_name());
				row.createCell(colIdx++).setCellValue(vo.getGname());
				
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
