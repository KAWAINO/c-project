package iwm_ko.rejectExcept.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import iwm_ko.comm.b15p10.paging_10;
import iwm_ko.rejectExcept.model.RejectExceptVo;
import iwm_ko.rejectExcept.service.RejectExceptService;
import iwm_ko.shipInfo.service.ShipInfoService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/reject_except")
public class RejectExceptController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MessageSource messageSource;
	
	@Resource(name="iwm_ko.rejectExcept.rejectExceptService")
	private RejectExceptService rejectExceptService;
	
	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;
	
	@Resource(name="iwm_ko.shipInfo.ShipInfoService")
	private ShipInfoService shipInfoService;
	
	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/reject_except.do")
	public String rejectExcept(@ModelAttribute("rejectExceptVo") RejectExceptVo rejectExceptVo, Model model, HttpSession session) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			rejectExceptVo.setScomp(scomp);
		}

		rejectExceptVo.setStartrow((rejectExceptVo.getPageno() -1) * rejectExceptVo.getFetchrow());
		
		// comp 리스트 조회
		List<String> compList = this.shipInfoService.selectCompList(scomp);
		model.addAttribute("compList", compList);
		
		List<RejectExceptVo> rejectExceptList = this.rejectExceptService.selectRejectExceptList(rejectExceptVo);
		model.addAttribute("rejectExceptList", rejectExceptList);
		
		// 페이징
		paging_10 paging = new paging_10();
		
		int totalCnt = this.rejectExceptService.totalRejectExceptList(rejectExceptVo);
		
		String pagingHTML = paging.getPagingStr(rejectExceptVo.getPageno(), rejectExceptVo.getFetchrow() , totalCnt);
		
		model.addAttribute("pagingHTML", pagingHTML);
		model.addAttribute("rejectExceptVo", rejectExceptVo);
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("406");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);
		
		return "reject_except/reject_except";
	}

	/*
	 *  등록
	 */
	@ResponseBody
	@RequestMapping(value="rejectExceptAdd.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> rejectExceptAdd(RejectExceptVo rejectExceptVo, HttpSession session, Model model) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			int chkMac = this.rejectExceptService.chkMac(rejectExceptVo).size();
			
			if(chkMac > 0) {
				resultMap.put("result", "-2");
			} else {
				int insertAuth = this.rejectExceptService.insertAuth(rejectExceptVo);
				
				if(insertAuth > 0) {
					resultMap.put("result", "1");
					
					// 로그 기록
					UserHistoryVo userHistory = new UserHistoryVo();
					userHistory.setGui_code("406");
					userHistory.setJob_name("추가");
					userHistory.setGui_remark("MAC : " + rejectExceptVo.getAddMac() + ", Ship Code : " + rejectExceptVo.getAddShipName());
					userHistory.setUser_id((String) session.getAttribute("user_id"));
					userHistory.setSql_str("INSERT INTO TB_REJECT_EXCEPT_INFO\r\n"
							+ "		(\r\n"
							+ "			MAC\r\n"
							+ "		  , S_CODE\r\n"
							+ "		  , DESCR\r\n"
							+ "		  , MODIFY_DATE\r\n"
							+ "		  , GUI_FLAG\r\n"
							+ "		)\r\n"
							+ "		VALUES\r\n"
							+ "		(\r\n"
							+ "			'"+ rejectExceptVo.getAddMac() +"'\r\n"
							+ "		  , '"+ rejectExceptVo.getAddShipName() +"'\r\n"
							+ "		  , '"+ rejectExceptVo.getAddDescr() +"'\r\n"
							+ "		  , NOW()\r\n"
							+ "		  , 'Y'\r\n"
							+ "		)");
					this.userHistoryService.insertUserHistory(userHistory);
				} else {
					resultMap.put("result", "0");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "0");
		}
		return resultMap;
	}
	
	/*
	 * 수정모달 이동
	 */
	@ResponseBody
	@RequestMapping(value="rejectExceptUpdateData.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> rejectExceptUpdateData(@RequestParam HashMap<String, Object> req, Model model, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			RejectExceptVo rejectExceptVo = this.rejectExceptService.selectRejectExceptInfo(req);

			resultMap.put("rejectExceptVo", rejectExceptVo);
			resultMap.put("result", "success");		
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "error");
		}
		return resultMap;
	}
	
	/*
	 *  수정
	 */
	@ResponseBody
	@RequestMapping(value="rejectExceptUpdate.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> rejectExceptUpdate(RejectExceptVo rejectExceptVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			int updateAuth = this.rejectExceptService.updateAuth(rejectExceptVo);
			
			if(updateAuth > 0) {
				resultMap.put("result", "1");
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("406");
				userHistory.setJob_name("수정");
				userHistory.setGui_remark("Mac : " + rejectExceptVo.getU_updMac() + ", Ship Code : " + rejectExceptVo.getUpdScode());
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str("UPDATE TB_NO_AUTH_INFO SET \r\n"
						+ "			 , S_CODE = '"+ rejectExceptVo.getUpdScode() +"'\r\n"
						+ "			 , DESCR = '"+ rejectExceptVo.getUpdDescr() +"'\r\n"
						+ "			 , MODIFY_DATE = NOW()\r\n"
						+ "			 , GUI_FLAG = 'Y'\r\n"
						+ "		 WHERE MAC = '"+ rejectExceptVo.getU_updMac() + "'\r\n"
						+ "        AND S_CODE = '"+ rejectExceptVo.getUpdScode() +"'");
				this.userHistoryService.insertUserHistory(userHistory);
			} else {
				resultMap.put("result", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "0");
		}
		return resultMap;
	}
	
	/**
	 *  삭제
	 */
	@ResponseBody
	@RequestMapping(value="rejectExceptDelete.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> rejectExceptDelete(RejectExceptVo rejectExceptVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			int deleteAuth = this.rejectExceptService.deleteAuth(rejectExceptVo);
			
			if(deleteAuth > 0) {
				resultMap.put("result", "1");
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("406");
				userHistory.setJob_name("삭제");
				userHistory.setGui_remark("Mac : " + rejectExceptVo.getMac());
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str("DELETE FROM TB_REJECT_AUTH_INFO\r\n"
						+ "		 WHERE MAC = '"+ rejectExceptVo.getMac() + "'\r\n"
						+ "		   AND S_CODE = '"+ rejectExceptVo.getS_code() +"'");
				this.userHistoryService.insertUserHistory(userHistory);
			} else {
				resultMap.put("result", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "0");
		}
		return resultMap;
	}
	
	/**
	 * Excel Down
	 */
	@RequestMapping(value="rejectExceptExcelDownload.do")
	public void rejectExceptExcelDownload(HttpServletResponse response,  HttpSession session
			, RejectExceptVo rejectExceptVo, Locale locale) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			rejectExceptVo.setSearchCompId(scomp);
		}
		
		try {	
			
			rejectExceptVo.setStartrow(-1);
			List<RejectExceptVo> excelList = this.rejectExceptService.selectRejectExceptList(rejectExceptVo);

			String[] l_aColumns = {
					messageSource.getMessage("select.shipOwner", null, locale), 
					messageSource.getMessage("select.shipName", null, locale), 
					"MAC", 
					messageSource.getMessage("list.description", null, locale)
			};
			
			String xlsName = messageSource.getMessage("rejectexcept.managementOfMACexceptions", null, locale);
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
			
			for(RejectExceptVo vo : excelList){
				row = sheet.createRow(rowIdx);

				colIdx = 0;
				
				Cell cell;
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getComp_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getS_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getMac());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getDescr());
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
		
		String sql = "SELECT B.MAC, A.COMP_NAME, A.S_NAME, A.S_CODE, B.DESCR\r\n"
				+ "		  FROM (\r\n"
				+ "				   SELECT S.COMP_ID, C.COMP_NAME, S.S_NAME, S.S_CODE\r\n"
				+ "				   FROM TB_SHIP_INFO S, TB_SCOMP_INFO C\r\n"
				+ "				   WHERE S.COMP_ID = C.COMP_ID\r\n"
				+ "				 )A, TB_REJECT_EXCEPT_INFO B\r\n"
				+ "		 WHERE A.S_CODE = B.S_CODE";
		
		if(!rejectExceptVo.getSearchCompId().isEmpty() && rejectExceptVo.getSearchCompId() != null) {
			sql += "AND COMP_ID = '"+ rejectExceptVo.getSearchCompId() +"' \r\n";
		}
		if(rejectExceptVo.getScomp() != "0" && rejectExceptVo.getScomp() != null && rejectExceptVo.getSearchCompId().isEmpty()) {
			sql += "AND COMP_ID = '"+ rejectExceptVo.getScomp() +"'\r\n";
		}
		if(!rejectExceptVo.getSearchShipName().isEmpty() && rejectExceptVo.getSearchShipName() != null) {
			sql += "AND S_NAME LIKE CONCAT ('%', '"+rejectExceptVo.getSearchShipName()+"', '%')\r\n";
		}
		
		sql += "ORDER BY A.COMP_NAME, A.S_NAME, B.MAC";
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("406");
		userHistory.setJob_name("엑셀");
		userHistory.setGui_remark("사유 : " + rejectExceptVo.getExcelMsg());
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str(sql);
		this.userHistoryService.insertUserHistory(userHistory);
	}
}
