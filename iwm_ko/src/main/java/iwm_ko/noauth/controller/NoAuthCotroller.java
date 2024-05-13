package iwm_ko.noauth.controller;

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
import iwm_ko.noauth.model.NoAuthVo;
import iwm_ko.noauth.service.NoAuthService;
import iwm_ko.shipInfo.service.ShipInfoService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/noauth")
public class NoAuthCotroller {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	@Resource(name="iwm_ko.noauth.noAuthService")
	private NoAuthService noAuthService;
	
	@Resource(name="iwm_ko.shipInfo.ShipInfoService")
	private ShipInfoService shipInfoService;
	
	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;

	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/noauth.do")
	public String slog(@ModelAttribute("noAuthVo") NoAuthVo noAuthVo, Model model, HttpSession session) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			noAuthVo.setScomp(scomp);
		}

		noAuthVo.setStartrow((noAuthVo.getPageno() -1) * noAuthVo.getFetchrow());

		// comp 리스트 조회
		List<String> compList = this.shipInfoService.selectCompList(scomp);
		model.addAttribute("compList", compList);
		
		// authList(페이지로딩리스트)
		List<NoAuthVo> authList = this.noAuthService.selectAuthList(noAuthVo);
		model.addAttribute("authList", authList);
		
		// 페이징
		paging_10 paging = new paging_10();
		
		int totalCnt = this.noAuthService.totalAuthList(noAuthVo);
		
		String pagingHTML = paging.getPagingStr(noAuthVo.getPageno(), noAuthVo.getFetchrow() , totalCnt);
		
		model.addAttribute("pagingHTML", pagingHTML);
		model.addAttribute("apInfoVo", noAuthVo);
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("403");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);
	
		return "noauth/noauth";
	}
	
	/*
	 * noAuthAdd 등록
	 */
	@ResponseBody
	@RequestMapping(value="noAuthAdd.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> noAuthAdd(NoAuthVo noAuthVo, HttpSession session, Model model) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			int chkMac = this.noAuthService.chkMac(noAuthVo).size();
//			logger.info("================================= chkMac : " + chkMac);
			if(chkMac > 0) {
				resultMap.put("result", "-2");
			} else {
				int chkName = this.noAuthService.chkName(noAuthVo).size();
//				logger.info("================================= chkName : " + chkName);
				if(chkName > 0) {
					resultMap.put("result", "-3");
				} else {
					int chkFlag = this.noAuthService.chkFlag(noAuthVo).size();
//					logger.info("================================= chkFlag : " + chkFlag);
					if(chkFlag > 0) {
						int chgAuth = this.noAuthService.chgAuth(noAuthVo);
//						logger.info("================================= chkFlag : " + chkFlag);
						if(chgAuth > 0) {
							resultMap.put("result", "1");
							// 로그 기록
							UserHistoryVo userHistory = new UserHistoryVo();
							userHistory.setGui_code("403");
							userHistory.setJob_name("추가");
							userHistory.setGui_remark("Mac : " + noAuthVo.getAddMac() + ", Device Name : " + noAuthVo.getAddTermName() 
								+ ", Ship Code : " + noAuthVo.getAddShipName());
							userHistory.setUser_id((String) session.getAttribute("user_id"));
							userHistory.setSql_str("UPDATE TB_NO_AUTH_INFO SET\r\n"
									+ "			   TERM_NAME = '"+ noAuthVo.getAddTermName() +"'\r\n"
									+ "			 , S_CODE = '"+ noAuthVo.getAddShipName() +"'\r\n"
									+ "			 , DESCR = '"+ noAuthVo.getAddDescr() +"'\r\n"
									+ "			 , GUI_FLAG = 'Y'\r\n"
									+ "			 , DEL_FLAG = 'N'\r\n"
									+ "			 , MODIFY_DATE = NOW()\r\n"
									+ "		 WHERE MAC = '"+ noAuthVo.getAddMac() +"'\r\n");
							this.userHistoryService.insertUserHistory(userHistory);
						} else {
							resultMap.put("result", "-2");
						}
					} else {
						int insAuth = this.noAuthService.insAuth(noAuthVo);
//						logger.info("================================= insAuth : " + insAuth);
						if(insAuth > 0) {
							resultMap.put("result", "1");
							// 로그 기록
							UserHistoryVo userHistory = new UserHistoryVo();
							userHistory.setGui_code("403");
							userHistory.setJob_name("추가");
							userHistory.setGui_remark("Mac : " + noAuthVo.getAddMac() + ", Device Name : " + noAuthVo.getAddTermName() 
								+ ", Ship Code : " + noAuthVo.getAddShipName());
							userHistory.setUser_id((String) session.getAttribute("user_id"));
							userHistory.setSql_str("INSERT INTO TB_NO_AUTH_INFO\r\n"
									+ "		(\r\n"
									+ "			MAC\r\n"
									+ "		  , S_CODE\r\n"
									+ "		  , TERM_NAME\r\n"
									+ "		  , DESCR\r\n"
									+ "		  , MODIFY_DATE\r\n"
									+ "		  , DEL_DATE\r\n"
									+ "		)\r\n"
									+ "		VALUES\r\n"
									+ "		(\r\n"
									+ "			'"+ noAuthVo.getAddMac() +"'\r\n"
									+ "		  , '"+ noAuthVo.getAddShipName() +"'\r\n"
									+ "		  , '"+ noAuthVo.getAddTermName() +"'\r\n"
									+ "		  , '"+ noAuthVo.getAddDescr() +"'\r\n"
									+ "		  , NOW()\r\n"
									+ "		  , NOW()\r\n"
									+ "		)");
							this.userHistoryService.insertUserHistory(userHistory);
						} else {
							resultMap.put("result", "-2");
						}
					}
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
	@RequestMapping(value="noAuthUpdateData.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> noAuthUpdateData(@RequestParam HashMap<String, Object> req, Model model, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			NoAuthVo noAuthVo = this.noAuthService.selectAuthInfo(req);

			resultMap.put("noAuthVo", noAuthVo);
			resultMap.put("result", "success");		
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "error");
		}
		return resultMap;
	}
	
	/*
	 * apinfo 운영자 수정
	 */
	@ResponseBody
	@RequestMapping(value="noAuthUpdate.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> noAuthUpdate(NoAuthVo noAuthVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
//			logger.info("=================================" + noAuthVo.getUpdMac());
//			logger.info("=================================" + noAuthVo.getUpdScode());
			
			int chkName = this.noAuthService.updChkName(noAuthVo).size();
			
			if(chkName > 0) {
				resultMap.put("result", "-3");
			} else {

				int updAuth = this.noAuthService.updateAuth(noAuthVo);
				
				if(updAuth > 0) {
					resultMap.put("result", "1");
					
					// 로그 기록
					UserHistoryVo userHistory = new UserHistoryVo();
					userHistory.setGui_code("403");
					userHistory.setJob_name("수정");
					userHistory.setGui_remark("Mac : " + noAuthVo.getU_updMac() + ", Device Name : " + noAuthVo.getUpdTermName() + ", Ship Code : " + noAuthVo.getUpdScode());
					userHistory.setUser_id((String) session.getAttribute("user_id"));
					userHistory.setSql_str("UPDATE TB_NO_AUTH_INFO SET\r\n"
							+ "			   TERM_NAME = '"+ noAuthVo.getUpdTermName() +"'\r\n"
							+ "			 , S_CODE = '"+ noAuthVo.getUpdScode() +"'\r\n"
							+ "			 , DESCR = '"+ noAuthVo.getUpdDescr() +"'\r\n"
							+ "			 , MODIFY_DATE = NOW()\r\n"
							+ "			 , GUI_FLAG = 'Y'\r\n"
							+ "		 WHERE MAC = '"+ noAuthVo.getU_updMac() +"'");
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
	
	/**
	 *  삭제
	 */
	@ResponseBody
	@RequestMapping(value="noAuthDelete.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> noAuthDelete(NoAuthVo noAuthVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			int delAuth = this.noAuthService.deleteAuth(noAuthVo);
			
			if(delAuth > 0) {
				resultMap.put("result", "1");
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("403");
				userHistory.setJob_name("삭제");
				userHistory.setGui_remark("Mac : " + noAuthVo.getMac());
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str("UPDATE TB_NO_AUTH_INFO SET\r\n"
						+ "			   DEL_FLAG = 'Y'\r\n"
						+ "			 , DEL_DATE = NOW()\r\n"
						+ "			 , GUI_FLAG = 'Y'\r\n"
						+ "		 WHERE MAC = '"+ noAuthVo.getMac() +"'\r\n"
						+ "		   AND S_CODE = '"+ noAuthVo.getS_code() +"'");
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
	@RequestMapping(value="noAuthExcelDownload.do")
	public void noAuthExcelDownload(HttpServletResponse response, final HttpSession session
			, NoAuthVo noAuthVo, Locale locale) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			noAuthVo.setScomp(scomp);
		}
		
		try {	
			
			noAuthVo.setStartrow(-1);
			List<NoAuthVo> excelList = this.noAuthService.selectAuthList(noAuthVo);

			String[] l_aColumns = {
					messageSource.getMessage("select.shipOwner", null, locale), 
					messageSource.getMessage("select.shipName", null, locale), 
					messageSource.getMessage("list.deviceName", null, locale), 
					"MAC", 
					messageSource.getMessage("list.description", null, locale)
			};
			
			String xlsName = messageSource.getMessage("noauth.noAuthRequiredMAC", null, locale);
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
			
			for(NoAuthVo vo : excelList){
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
				cell.setCellValue(vo.getTerm_name());
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
		
		String sql = "SELECT B.MAC, B.TERM_NAME, A.COMP_NAME, A.S_NAME, A.S_CODE, B.DESCR\r\n"
				+ "		  FROM (\r\n"
				+ "				   SELECT S.COMP_ID, C.COMP_NAME, S.S_NAME, S.S_CODE\r\n"
				+ "				   FROM TB_SHIP_INFO S, TB_SCOMP_INFO C\r\n"
				+ "				   WHERE S.COMP_ID = C.COMP_ID\r\n"
				+ "				 )A, TB_NO_AUTH_INFO B\r\n"
				+ "		 WHERE A.S_CODE = B.S_CODE\r\n"
				+ "		   AND B.DEL_FLAG = 'N'\r\n";

		logger.info("===================" + noAuthVo.getSearchTermName());
		
		if(noAuthVo.getSearchCompId() != null && !noAuthVo.getSearchCompId().isEmpty()) {
			sql += "AND COMP_ID = '"+ noAuthVo.getSearchCompId() +"'\r\n";
		}
		if(noAuthVo.getScomp() != "0" && noAuthVo.getScomp() != null) {
			sql += "AND COMP_ID = '"+ noAuthVo.getScomp() +"'\r\n";
		}
		if(noAuthVo.getSearchShipName() != null && !noAuthVo.getSearchShipName().isEmpty()) {
			sql += "AND S_NAME LIKE CONCAT ('%', '"+noAuthVo.getSearchShipName()+"', '%')\r\n";
		}
		if(noAuthVo.getSearchTermName() != null && !noAuthVo.getSearchTermName().isEmpty()) {
			sql += "AND TERM_NAME LIKE CONCAT ('%', '"+noAuthVo.getSearchTermName()+"', '%')\r\n";
		}

		sql += "ORDER BY A.COMP_NAME, A.S_NAME, B.TERM_NAME, B.MAC";
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("403");
		userHistory.setJob_name("엑셀");
		userHistory.setGui_remark("사유 : " + noAuthVo.getExcelMsg());
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str(sql);
		this.userHistoryService.insertUserHistory(userHistory);
	}
}
