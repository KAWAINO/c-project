package iwm_ko.apport.controller;

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

import iwm_ko.apport.model.ApportVo;
import iwm_ko.apport.service.ApportService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/apport")
public class ApportController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	@Resource(name="iwm_ko.apport.apportService")
	private ApportService apportService;

	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;
	
	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/apport.do")
	public String apport(@ModelAttribute("apportVo") ApportVo apportVo, Model model, HttpSession session, Locale locale
			, HttpServletRequest request, @RequestParam(value = "searchShipList", required = false) String searchShipList) throws Exception {

		apportVo.setComp_id((String)session.getAttribute("comp_id"));

		List<ApportVo> shipList = this.apportService.selectShipList(apportVo);
		model.addAttribute("shipList", shipList);
		
//		logger.info("searchShipList ::::: " + searchShipList);
		
		String s_code = "";
		if(searchShipList == null) {
			s_code = shipList.get(0).getS_code();
			apportVo.setSearchShipCode(s_code);
		} else {
			apportVo.setSearchShipCode(searchShipList);
		}
		
		// 선박 조회
		List<ApportVo> portList = this.apportService.selectPortList(apportVo, locale);
		model.addAttribute("portList", portList);
				
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("402");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);

		return "apport/apport";
	}	

	// 선박 조회
//	@ResponseBody
//	@RequestMapping(value="portList.ajax", method=RequestMethod.POST) 
//	public List<ApportVo> portList(@ModelAttribute("apportVo") ApportVo apportVo, Model model) throws Exception {
//				
//		// 선박 조회
//		List<ApportVo> portList = this.apportService.selectPortList(apportVo);
//		model.addAttribute("portList", portList);
//
//		return portList;
//	}
	
	/*
	 * apinfo 등록
	 */
	@ResponseBody
	@RequestMapping(value="apportAdd.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> apportAdd(ApportVo apportVo,HttpSession session, Model model) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			int chkName = this.apportService.chkName(apportVo).size();

			if(chkName == 0) {
				
				// portFrom, portTo 범위 비교
				List<ApportVo> chkRange = this.apportService.chkRange(apportVo);
				
				if(!chkRange.isEmpty()) {
	
					int addFrom = Integer.parseInt(apportVo.getAddPortFrom());
					int addTo = Integer.parseInt(apportVo.getAddPortTo());
					int result = 0;

					for(int i = 0; i < chkRange.size(); i++) {		
						
						int from = chkRange.get(i).getPort_from();
						int to = chkRange.get(i).getPort_to();
			
						 if((addFrom >= from && addTo <= from) || (addFrom >= to && addTo <= to) ||
								 (addFrom <= from && addTo >= from) || (addFrom <= to && addTo >= to) ||
								 (addFrom >= from && addTo <= to)) {
							 
						 logger.info("addFrom ::::: " + addFrom);
						 logger.info("  addTo ::::: " + addTo);
						 logger.info("   from ::::: " + from);
						 logger.info("     to ::::: " + to);

						 result = 1; 
						 resultMap.put("result", "-3"); 
						 break;
						 }
					}
					
					if(result != 1) {
						int insertPort = this.apportService.insertPort(apportVo);
							
						// 로그 기록
						UserHistoryVo userHistory = new UserHistoryVo();
						userHistory.setGui_code("402");
						userHistory.setJob_name("추가");
						userHistory.setGui_remark("Name : " + apportVo.getAddRuleName() 
													+ ", Allowed port range : " + apportVo.getAddPortFrom() + " ~ " + apportVo.getAddPortTo() 
													+ ", Ship Code : " + apportVo.getAddScode());
						userHistory.setUser_id((String) session.getAttribute("user_id"));
						userHistory.setSql_str("INSERT INTO TB_AP_PORT_LIST (S_CODE, RULE_NAME, PORT_FROM, PORT_TO, USE_FLAG, DESCR, MODIFY_DATE)\r\n"
												+ "    	VALUES ('"+ apportVo.getAddScode() +"', '"+ apportVo.getAddRuleName() +"', " 
												+ "'"+ apportVo.getAddPortFrom() +"', '"+ apportVo.getAddPortTo() +"', '"+ apportVo.getAddUseFlag() 
												+ "', '"+ apportVo.getAddDescr() +"', NOW())");
						this.userHistoryService.insertUserHistory(userHistory);
						
						if(insertPort > 0) {
							resultMap.put("result", "1");
						} else {
							resultMap.put("result", "0");
						}
					}
				} else {
					int insertPort = this.apportService.insertPort(apportVo);
					
					// 로그 기록
					UserHistoryVo userHistory = new UserHistoryVo();
					userHistory.setGui_code("402");
					userHistory.setJob_name("추가");
					userHistory.setGui_remark("Name : " + apportVo.getAddRuleName() 
												+ ", Allowed port range : " + apportVo.getAddPortFrom() + " ~ " + apportVo.getAddPortTo() 
												+ ", Ship Code : " + apportVo.getAddScode());
					userHistory.setUser_id((String) session.getAttribute("user_id"));
					userHistory.setSql_str("INSERT INTO TB_AP_PORT_LIST (S_CODE, RULE_NAME, PORT_FROM, PORT_TO, USE_FLAG, DESCR, MODIFY_DATE)\r\n"
											+ "    	VALUES ('"+ apportVo.getAddScode() +"' ,'"+ apportVo.getAddRuleName() +"', '" 
											+ apportVo.getAddPortFrom() +"', '"+ apportVo.getAddPortTo() +"', '"+ apportVo.getAddUseFlag() +"', '" 
										    + apportVo.getAddDescr() +"', NOW())");
					this.userHistoryService.insertUserHistory(userHistory);
					
					if(insertPort > 0) {
						resultMap.put("result", "1");
					} else {
						resultMap.put("result", "0");
					}
				}
			} else {
				resultMap.put("result", "-2");
			}
		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "0");
		}
		
		return resultMap;
	}
	
	/*
	 * 수정모달 이동
	 */
	@ResponseBody
	@RequestMapping(value="apportUpdateData.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> apportUpdateData(@RequestParam HashMap<String, Object> req, Model model) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			ApportVo apportVo = this.apportService.selectApport(req);

			resultMap.put("apportVo", apportVo);
			resultMap.put("result", "success");

		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "error");
		}
		return resultMap;
	}
	
	/*
	 * apport 수정
	 */
	@ResponseBody
	@RequestMapping(value="apportUpdate.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> apportUpdate(ApportVo apportVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			// portFrom, portTo 범위 비교
			List<ApportVo> updateChkRange = this.apportService.updateChkRange(apportVo);
			
			if(!updateChkRange.isEmpty()) {

				int updFrom = Integer.parseInt(apportVo.getUpdPortFrom());
				int updTo = Integer.parseInt(apportVo.getUpdPortTo());
				int result = 0;

				for(int i = 0; i < updateChkRange.size(); i++) {		
					
					int from = updateChkRange.get(i).getPort_from();
					int to = updateChkRange.get(i).getPort_to();
		
					 if((updFrom >= from && updTo <= from) || (updFrom >= to && updTo <= to) ||
							 (updFrom <= from && updTo >= from) || (updFrom <= to && updTo >= to) ||
							 (updFrom >= from && updTo <= to)) {

					 result = 1; 
					 resultMap.put("result", "-3"); 
					 break;
					 }
				}
				
				if(result > 0) {				
					resultMap.put("result", "-3");
				} else {
					int chgPort = this.apportService.chgPort(apportVo);
					
					// 로그 기록
					UserHistoryVo userHistory = new UserHistoryVo();
					userHistory.setGui_code("402");
					userHistory.setJob_name("수정");
					userHistory.setGui_remark("Name : " + apportVo.getUpdRuleName() 
												+ ", Allowed port range : " + apportVo.getUpdPortFrom() + " ~ " + apportVo.getUpdPortTo() 
												+ ", Ship Code : " + apportVo.getUpdScode());
					userHistory.setUser_id((String) session.getAttribute("user_id"));
					userHistory.setSql_str("UPDATE TB_AP_PORT_LIST SET\r\n"
							+ "    		   PORT_FROM = '"+ apportVo.getUpdPortFrom()+"'\r\n"
							+ "    		 , PORT_TO = '"+ apportVo.getUpdPortTo() +"'\r\n"
							+ "    		 , USE_FLAG = '"+ apportVo.getUpdUseFlag() +"'\r\n"
							+ "    		 , DESCR = '" +apportVo.getUpdDescr() +"'\r\n"
							+ "    		 , MODIFY_DATE = NOW()\r\n"
							+ "    	 WHERE S_CODE = '" +apportVo.getUpdScode() +"'\r\n"
							+ "    	   AND RULE_NAME = '"+ apportVo.getUpdRuleName() +"'");
					this.userHistoryService.insertUserHistory(userHistory);
					
					if(chgPort > 0) {
						resultMap.put("result", "1");
					} else {
						resultMap.put("result", "0");
					} 
				}
			}
		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "0");
 		}	
		
		return resultMap;
	}
	
	/**
	 * apport 삭제
	 */
	@ResponseBody
	@RequestMapping(value="apportDelete.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> apportDelete(ApportVo apportVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			int delResult = this.apportService.deleteApport(apportVo);
			
			if(delResult > 0) {
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("402");
				userHistory.setJob_name("삭제");
				userHistory.setGui_remark("Name : " + apportVo.getRule_name());
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str("DELETE FROM TB_AP_PORT_LIST\r\n"
						+ "		 WHERE S_CODE = '"+ apportVo.getS_code() +"'\r\n"
						+ "		   AND RULE_NAME = '"+ apportVo.getRule_name() +"'");
				this.userHistoryService.insertUserHistory(userHistory);
				
				resultMap.put("result", "1");
			} else {
				resultMap.put("result", "0");
			}
		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "0");
 		}
		
		return resultMap;
	}
	
	/*
	 * 모든포트 허용
	 */
	@ResponseBody
	@RequestMapping(value="allowAllPort.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> allowAllPort(ApportVo apportVo, Model model, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
					
			String allFlag = apportVo.getAllowAllPort();

			if(allFlag.equals("true")) {
				apportVo.setAllowAllPort("1");
			} else {
				apportVo.setAllowAllPort("0");
			} 

			List<ApportVo> chkPort = this.apportService.chkPort(apportVo);
			
			if(chkPort.size() > 0) {
				int chgAllPort = this.apportService.chgAllPort(apportVo);
				
				if(chgAllPort > 0) {
					resultMap.put("result", "success");
				} else {
					resultMap.put("result", "error");
				}
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("402");
				userHistory.setJob_name("추가");
				userHistory.setGui_remark((apportVo.getAllowAllPort().equals("1") ? "Allow" : "Disable") + " all ports");
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str("UPDATE TB_AP_ALL_PORT SET\r\n"
						+ "    		   ALL_FLAG = '"+ apportVo.getAllowAllPort() +"'\r\n"
						+ "    		 , MODIFY_DATE = NOW()\r\n"
						+ "    		 , GUI_FLAG = 'Y'\r\n"
						+ "    	 WHERE S_CODE = '"+ apportVo.getS_code() +"'");
				this.userHistoryService.insertUserHistory(userHistory);
				
			} else {
				int insertAllPort = this.apportService.insertAllPort(apportVo);
				
				if(insertAllPort > 0) {
					resultMap.put("result", "success");
				} else {
					resultMap.put("result", "error");
				}
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("402");
				userHistory.setJob_name("추가");
				userHistory.setGui_remark((apportVo.getAllowAllPort().equals("1") ? "Allow" : "Disable") + " all ports");
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str("UPDATE TB_AP_ALL_PORT SET\r\n"
						+ "    		   ALL_FLAG = '"+ apportVo.getAllowAllPort() +"'\r\n"
						+ "    		 , MODIFY_DATE = NOW()\r\n"
						+ "    		 , GUI_FLAG = 'Y'\r\n"
						+ "    	 WHERE S_CODE = '"+ apportVo.getS_code() +"'");
				this.userHistoryService.insertUserHistory(userHistory);
			}

		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "error");
		}
		return resultMap;
	}
	
	/*
	 * 모든선박적용
	 */
	@ResponseBody
	@RequestMapping(value="setAllShip.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> setAllShip(ApportVo apportVo, Model model, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			String allFlag = apportVo.getAllowAllPort();

			if(allFlag.equals("true")) {
				apportVo.setAllowAllPort("1");
			} else {
				apportVo.setAllowAllPort("0");
			} 
			
			List<ApportVo> shipList = this.apportService.shipCode(apportVo);
				
			for(int i = 0; i < shipList.size(); i++) {
				String sCode = shipList.get(i).getS_code();
				apportVo.setShipCode(sCode);

				int delScode = this.apportService.delShipcode(apportVo);
	
				int insScode = this.apportService.insShipCode(apportVo); 

				List<ApportVo> chkShipCode = this.apportService.chkShipCode(apportVo);
				
				if(chkShipCode.size() > 0) {
					int chgApAllPort = this.apportService.chgApAllPort(apportVo);
					
					if(chgApAllPort > 0) {
						resultMap.put("result", "1");
					} else {
						resultMap.put("result", "0");
					}

				} else {
					int insertApAllPort = this.apportService.insertApAllPort(apportVo);
					
					if(insertApAllPort > 0) {
						resultMap.put("result", "1");
					} else {
						resultMap.put("result", "0");
					}
				}
			}
			
			// 로그 기록
			UserHistoryVo userHistory = new UserHistoryVo();
			userHistory.setGui_code("402");
			userHistory.setJob_name("수정");
			userHistory.setGui_remark("Apply to all ship");
			userHistory.setUser_id((String) session.getAttribute("user_id"));
			userHistory.setSql_str("");
			this.userHistoryService.insertUserHistory(userHistory);
			
			resultMap.put("result", "1");
			
		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "-0");
		}
		return resultMap;
	}
	
	/**
	 * Excel Down
	 */
	@RequestMapping(value="apportExcelDownload.do", method=RequestMethod.POST)
	public void apportExcelDownload(@RequestParam(value = "searchShipList", required = false) String searchShipList
								  , @RequestParam(value = "excelMsg", required = false) String excelMsg, HttpServletResponse response
			, final HttpSession session, ApportVo apportVo, Locale locale) throws Exception {
		
		try {	
			
			apportVo.setComp_id((String)session.getAttribute("comp_id"));

			List<ApportVo> shipList = this.apportService.selectShipList(apportVo);
			
			String s_code = "";
			if(searchShipList == null) {
				s_code = shipList.get(0).getS_code();
				apportVo.setSearchShipCode(s_code);
			} else {
				apportVo.setSearchShipCode(searchShipList);
			}

//			logger.info("========================== s_code : " + s_code);
//			logger.info("========================== msg : " + excelMsg);

			apportVo.setStartrow(-1);
			List<ApportVo> excelList = this.apportService.selectPortList(apportVo, locale);
		
			String[] l_aColumns = {
					messageSource.getMessage("list.name", null, locale), 
					messageSource.getMessage("select.shipOwner", null, locale), 
					messageSource.getMessage("select.shipName", null, locale), 
					messageSource.getMessage("list.allowedPortRange", null, locale), 
					messageSource.getMessage("list.allowed", null, locale), 
					messageSource.getMessage("list.description", null, locale)
			};
			
			String xlsName = messageSource.getMessage("apport.wapPortUsage", null, locale);
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
			
			for(ApportVo vo : excelList){
				row = sheet.createRow(rowIdx);
							
				colIdx = 0;
				
				Cell cell;
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getRule_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getComp_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getS_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getPort_from() + " ~ " + vo.getPort_to());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getUse_flag());
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
			logger.error("Excel download error", e);
		}
		
		String sql = "SELECT S.S_CODE\r\n"
				+ "		     , S.S_NAME\r\n"
				+ "		     , C.COMP_NAME\r\n"
				+ "		  FROM TB_SHIP_INFO S, TB_SCOMP_INFO C\r\n"
				+ "		 WHERE S.COMP_ID = C.COMP_ID\r\n"
				+ "		   AND S.DEL_FLAG = 'N'\r\n";
		
		if(Integer.parseInt(apportVo.getComp_id()) != 0 && apportVo.getComp_id() != null) {
			sql += "AND COMP_ID = '"+ apportVo.getComp_id() +"'\r\n";
		}

		sql += "ORDER BY C.COMP_NAME, S.S_NAME";
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("402");
		userHistory.setJob_name("엑셀");
		userHistory.setGui_remark("사유 : " + apportVo.getExcelMsg());
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str(sql);
		this.userHistoryService.insertUserHistory(userHistory);
	}
}
