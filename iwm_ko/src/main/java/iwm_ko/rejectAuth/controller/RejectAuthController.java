package iwm_ko.rejectAuth.controller;

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
import iwm_ko.rejectAuth.model.RejectAuthVo;
import iwm_ko.rejectAuth.service.RejectAuthService;
import iwm_ko.shipInfo.service.ShipInfoService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/rejectauth")
public class RejectAuthController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MessageSource messageSource;
	
	@Resource(name="iwm_ko.rejectAuth.rejectAuthService")
	private RejectAuthService rejectAuthService;
	
	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;
	
	@Resource(name="iwm_ko.shipInfo.ShipInfoService")
	private ShipInfoService shipInfoService;
	
	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/rejectauth.do")
	public String slog(@ModelAttribute("rejectAuthVo") RejectAuthVo rejectAuthVo, Model model, HttpSession session) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			rejectAuthVo.setScomp(scomp);
		}
		
		rejectAuthVo.setStartrow((rejectAuthVo.getPageno() -1) * rejectAuthVo.getFetchrow());

		// comp 리스트 조회
		List<String> compList = this.shipInfoService.selectCompList(scomp);
		model.addAttribute("compList", compList);
		
		List<RejectAuthVo> rejectAuthList = this.rejectAuthService.selectRejectAuthList(rejectAuthVo);
		model.addAttribute("rejectAuthList", rejectAuthList);
		
		// 페이징
		paging_10 paging = new paging_10();
		
		int totalCnt = this.rejectAuthService.totalRejectAuthList(rejectAuthVo);
		
		String pagingHTML = paging.getPagingStr(rejectAuthVo.getPageno(), rejectAuthVo.getFetchrow() , totalCnt);
		
		model.addAttribute("pagingHTML", pagingHTML);
		model.addAttribute("rejectAuthVo", rejectAuthVo);
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("405");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);
	
		return "rejectauth/rejectauth";
	}

	/*
	 *  등록
	 */
	@ResponseBody
	@RequestMapping(value="rejectAuthAdd.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> rejectAuthAdd(RejectAuthVo rejectAuthVo, HttpSession session, Model model) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			int chkMac = this.rejectAuthService.chkMac(rejectAuthVo).size();
			
			if(chkMac > 0) {
				resultMap.put("result", "-2");
			} else {
				int insertAuth = this.rejectAuthService.insertAuth(rejectAuthVo);
				
				if(insertAuth > 0) {
					resultMap.put("result", "1");
					
					// 로그 기록
					UserHistoryVo userHistory = new UserHistoryVo();
					userHistory.setGui_code("405");
					userHistory.setJob_name("추가");
					userHistory.setGui_remark("MAC : " + rejectAuthVo.getAddMac() + ", Ship Code : " + rejectAuthVo.getAddShipName());
					userHistory.setUser_id((String) session.getAttribute("user_id"));
					userHistory.setSql_str("INSERT INTO TB_REJECT_AUTH_INFO\r\n"
							+ "		(\r\n"
							+ "			MAC\r\n"
							+ "		  , S_CODE\r\n"
							+ "		  , DESCR\r\n"
							+ "		  , MODIFY_DATE\r\n"
							+ "		  , GUI_FLAG\r\n"
							+ "		)\r\n"
							+ "		VALUES\r\n"
							+ "		(\r\n"
							+ "			'"+ rejectAuthVo.getAddMac() +"'\r\n"
							+ "		  , '"+ rejectAuthVo.getAddShipName()+"'\r\n"
							+ "		  , '"+ rejectAuthVo.getDescr() +"'\r\n"
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
	@RequestMapping(value="rejectAuthUpdateData.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> rejectAuthUpdateData(@RequestParam HashMap<String, Object> req, Model model, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			RejectAuthVo rejectAuthVo = this.rejectAuthService.selectRejectAuthInfo(req);

			resultMap.put("rejectAuthVo", rejectAuthVo);
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
	@RequestMapping(value="rejectAuthUpdate.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> rejectAuthUpdate(RejectAuthVo rejectAuthVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			int updateRejectAuth = this.rejectAuthService.updateRejectAuth(rejectAuthVo);
			
			if(updateRejectAuth > 0) {
				resultMap.put("result", "1");
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("405");
				userHistory.setJob_name("수정");
				userHistory.setGui_remark("Mac : " + rejectAuthVo.getU_updMac() + ", Ship Code : " + rejectAuthVo.getUpdScode());
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str("UPDATE TB_NO_AUTH_INFO SET\r\n"
						+ "			 , S_CODE = '"+ rejectAuthVo.getUpdScode() +"'\r\n"
						+ "			 , DESCR = '"+ rejectAuthVo.getUpdDescr() +"'\r\n"
						+ "			 , MODIFY_DATE = NOW()\r\n"
						+ "			 , GUI_FLAG = 'Y'\r\n"
						+ "		 WHERE MAC = '"+ rejectAuthVo.getU_updMac() +"'\r\n"
						+ "        AND S_CODE = '"+ rejectAuthVo.getUpdScode() +"'" );
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
	@RequestMapping(value="rejectAuthDelete.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> rejectAuthDelete(RejectAuthVo rejectAuthVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			int deleteRejectAuth = this.rejectAuthService.deleteRejectAuth(rejectAuthVo);
			
			if(deleteRejectAuth > 0) {
				resultMap.put("result", "1");
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("405");
				userHistory.setJob_name("삭제");
				userHistory.setGui_remark("Mac : " + rejectAuthVo.getMac());
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str("DELETE FROM TB_REJECT_AUTH_INFO\r\n"
						+ "		 WHERE MAC = '"+ rejectAuthVo.getMac() +"'\r\n"
						+ "		   AND S_CODE = '"+ rejectAuthVo.getS_code() +"'");
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
	@RequestMapping(value="rejectAuthExcelDownload.do")
	public void rejectAuthExcelDownload(HttpServletResponse response, HttpSession session
			, RejectAuthVo rejectAuthVo, Locale locale) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			rejectAuthVo.setSearchCompId(scomp);
		}
		
		try {	
			
			rejectAuthVo.setStartrow(-1);
			List<RejectAuthVo> excelList = this.rejectAuthService.selectRejectAuthList(rejectAuthVo);

			String[] l_aColumns = {
					messageSource.getMessage("select.shipOwner", null, locale), 
					messageSource.getMessage("select.shipName", null, locale), 
					"MAC", 
					messageSource.getMessage("list.description", null, locale)
			};
			
			String xlsName = messageSource.getMessage("rejectauth.unauthorizedMACmanagement", null, locale);
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
			
			for(RejectAuthVo vo : excelList){
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
				+ "				 )A, TB_REJECT_AUTH_INFO B\r\n"
				+ "		 WHERE A.S_CODE = B.S_CODE";
		
		if(!rejectAuthVo.getSearchCompId().isEmpty() && rejectAuthVo.getSearchCompId() != null) {
			sql += "AND COMP_ID = '"+ rejectAuthVo.getSearchCompId() +"' \r\n";
		}
		if(rejectAuthVo.getScomp() != "0" && rejectAuthVo.getScomp() != null && rejectAuthVo.getSearchCompId().isEmpty()) {
			sql += "AND COMP_ID = '"+ rejectAuthVo.getScomp() +"'\r\n";
		}
		if(!rejectAuthVo.getSearchShipName().isEmpty() && rejectAuthVo.getSearchShipName() != null) {
			sql += "AND S_NAME LIKE CONCAT ('%', '"+rejectAuthVo.getSearchShipName()+"', '%')\r\n";
		}
		
		sql += "ORDER BY A.COMP_NAME, A.S_NAME, B.MAC";
		
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("405");
		userHistory.setJob_name("엑셀");
		userHistory.setGui_remark("사유 : " + rejectAuthVo.getExcelMsg());
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str(sql);
		this.userHistoryService.insertUserHistory(userHistory);
	}
}
