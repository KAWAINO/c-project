package iwm_ko.apinfo.controller;

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

import iwm_ko.apinfo.model.ApInfoVo;
import iwm_ko.apinfo.service.ApInfoService;
import iwm_ko.comm.b15p10.paging_10;
import iwm_ko.shipInfo.service.ShipInfoService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/apinfo")
public class ApInfoController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	@Resource(name="iwm_ko.apinfo.apInfoService")
	private ApInfoService apInfoService;
	
	@Resource(name="iwm_ko.shipInfo.ShipInfoService")
	private ShipInfoService shipInfoService;
	
	@Resource(name="iwm_ko.userHistory.userHistoryService")
	private UserHistoryService userHistoryService;
	
	/*
	 * 페이지 로딩
	 */
	@RequestMapping("/apinfo.do")
	public String slog(@ModelAttribute("apInfoVo") ApInfoVo apInfoVo, Model model
			, HttpSession session, Locale locale) throws Exception {
		
		if(apInfoVo.getSearchCompId() == null) {
			apInfoVo.setSearchCompId("");
		}
//		logger.info("searchCompId ::::: " + apInfoVo.getSearchCompId());
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			apInfoVo.setScomp(scomp);
		}
		
		apInfoVo.setStartrow((apInfoVo.getPageno() -1) * apInfoVo.getFetchrow());

		// comp 리스트 조회
		List<String> compList = this.shipInfoService.selectCompList(scomp);
		model.addAttribute("compList", compList);
		
		// 대역폭 리스트 조회
		List<ApInfoVo> bandList = this.apInfoService.selectBandList(apInfoVo);
		model.addAttribute("bandList", bandList);

		List<ApInfoVo> apInfoList = this.apInfoService.selectApInfoList(apInfoVo, locale);
		model.addAttribute("apInfoList", apInfoList);
		
		// 페이징
		paging_10 paging = new paging_10();
		
		int totalCnt = this.apInfoService.totalApInfoList(apInfoVo);
		
		String pagingHTML = paging.getPagingStr(apInfoVo.getPageno(), apInfoVo.getFetchrow() , totalCnt);
		
		model.addAttribute("pagingHTML", pagingHTML);
		model.addAttribute("apInfoVo", apInfoVo);
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("401");
		userHistory.setJob_name("조회");
		userHistory.setGui_remark("Data Search");
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str("");
		this.userHistoryService.insertUserHistory(userHistory);
		
		return "apinfo/apinfo";
	}
	
	// 추가모달 선박명 조회
	@ResponseBody
	@RequestMapping(value="shipList.ajax", method=RequestMethod.POST) 
	public List<ApInfoVo> shipList(@ModelAttribute("apInfoVo") ApInfoVo apInfoVo,  Model model) throws Exception {
		
		// 선박명 조회
		List<ApInfoVo> shipList = this.apInfoService.selectShipList(apInfoVo);
		model.addAttribute("shipList", shipList);
		
		return shipList;
	}
	
	/*
	 * apinfo 등록
	 */
	@ResponseBody
	@RequestMapping(value="apinfoAdd.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> apinfoAdd(ApInfoVo apInfoVo, HttpSession session, Model model) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		int result = 0;
		
		if(apInfoVo.getAddUnLimit() != null) {
			apInfoVo.setAddUnLimit("Y");
		} else {
			apInfoVo.setAddUnLimit("N");
		}
		// conn_time 설정
		int contime = 50000;
		if(apInfoVo.getAddUnLimit().equals("N")) {
			contime=(Integer.parseInt(apInfoVo.getAddHour())*60) + Integer.parseInt(apInfoVo.getAddMin());
		}
		apInfoVo.setAddConnTime(String.valueOf(contime));
		
		try {
			// MAC 중복검사
			int chkMac = this.apInfoService.chkMac(apInfoVo).size();
			
			if(chkMac > 0) {
				resultMap.put("result", "-2");
			} else {
				// ap_name 중복검사
				int chkName = this.apInfoService.chkName(apInfoVo).size();
				
				if(chkName > 0) {
					resultMap.put("result", "-3");
				} else {
					// flag 체크
					int chkFlag = this.apInfoService.chkFlag(apInfoVo).size();
					
					if(chkFlag > 0) {
						// 기존 ap데이터 수정
						int changeAp = this.apInfoService.changeAp(apInfoVo);
						
						// 로그 기록
						UserHistoryVo userHistory = new UserHistoryVo();
						userHistory.setGui_code("401");
						userHistory.setJob_name("추가");
						userHistory.setGui_remark("MAC : " + apInfoVo.getAddMac() + ", WAP Name : " + apInfoVo.getAddApName() + ", Ship Code : " + apInfoVo.getAddShipName());
						userHistory.setUser_id((String) session.getAttribute("user_id"));
						userHistory.setSql_str("UPDATE TB_AP_INFO SET\r\n"
								+ "			   S_CODE = '"+ apInfoVo.getAddShipName() +"'\r\n"
								+ "			 , AP_NAME = '"+ apInfoVo.getAddApName() +"'\r\n"
								+ "			 , SERIAL_NO = '"+ apInfoVo.getAddSerial() +"'\r\n"
								+ "			 , IP_NO = '"+ apInfoVo.getAddIpNumber() +"'\r\n"
								+ "			 , BAND_WIDTH = '"+ apInfoVo.getAddBand() +"'\r\n"
								+ "			 , CON_USER = '"+ apInfoVo.getAddConUser() +"'\r\n"
								+ "			 , DESCR = '"+ apInfoVo.getAddDescr() +"'\r\n"
								+ "			 , GUI_FLAG = 'Y'\r\n"
								+ "			 , DEL_FLAG = 'N'\r\n"
								+ "			 , MODIFY_DATE = NOW()\r\n"
								+ "		 WHERE MAC = '"+ apInfoVo.getAddMac() +"'\r\n"
								+ "		   AND S_CODE = '"+ apInfoVo.getAddShipName() +"'");
						this.userHistoryService.insertUserHistory(userHistory);
//						logger.info("changeAp ::::: " + changeAp);
						if(changeAp > 0) {
							result = 1;
						} else {
							resultMap.put("result", "0");
						}
					} else {
						// apinfo 신규등록
						int insertApInfo = this.apInfoService.insertApInfo(apInfoVo);
//						logger.info("================================= insertApInfo : " + insertApInfo);
						
						// 로그 기록
						UserHistoryVo userHistory = new UserHistoryVo();
						userHistory.setGui_code("401");
						userHistory.setJob_name("추가");
						userHistory.setGui_remark("MAC : " + apInfoVo.getAddMac() + ", WAP Name : " + apInfoVo.getAddApName() + ", Ship Code : " + apInfoVo.getAddShipName());
						userHistory.setUser_id((String) session.getAttribute("user_id"));
						userHistory.setSql_str("		INSERT INTO TB_AP_INFO\r\n"
								+ "		(\r\n"
								+ "			MAC\r\n"
								+ "		  , S_CODE\r\n"
								+ "		  , AP_NAME\r\n"
								+ "		  , SERIAL_NO\r\n"
								+ "		  , BAND_WIDTH\r\n"
								+ "		  , CON_USER\r\n"
								+ "		  , DESCR\r\n"
								+ "		  , IP_NO\r\n"
								+ "		  , IP_ADDR\r\n"
								+ "		  , MODIFY_DATE\r\n"
								+ "		  , DEL_DATE\r\n"
								+ "		)\r\n"
								+ "		VALUES\r\n"
								+ "		(\r\n"
								+ "			'"+ apInfoVo.getAddMac() +"'\r\n"
								+ "		  , '"+ apInfoVo.getAddShipName() +"'\r\n"
								+ "		  , '"+ apInfoVo.getAddApName() +"'\r\n"
								+ "		  , '"+ apInfoVo.getAddSerial() +"'\r\n"
								+ "		  , '"+ apInfoVo.getAddBand() +"'\r\n"
								+ "		  , '"+ apInfoVo.getAddConUser() +"'\r\n"
								+ "		  , '"+ apInfoVo.getAddDescr() +"'\r\n"
								+ "		  , '"+ apInfoVo.getAddIpNumber() +"'\r\n"
								+ "		  , ''\r\n"
								+ "		  , NOW()\r\n"
								+ "		  , NOW()\r\n"
								+ "		");
						this.userHistoryService.insertUserHistory(userHistory);
						
						if(insertApInfo > 0) {
							result = 1;
						} else {
							resultMap.put("result", "0");
						}
					}
						
					if(result == 1) {
						// subdata 중복검사
						int chkSubData = this.apInfoService.chkSubData(apInfoVo).size();
//							logger.info("====================================== chkSubData : " + chkSubData);
						if(chkSubData > 0) {
							int changeApSub = this.apInfoService.changeApSub(apInfoVo);
							
							if(changeApSub > 0) {
								resultMap.put("result", "1");
							} else {
								resultMap.put("result", "0");
							}
						} else {
							int insertApSub = this.apInfoService.insertApSub(apInfoVo);	
							
							if(insertApSub > 0) {
								resultMap.put("result", "1");
							} else {
								resultMap.put("result", "0");
							}
						}						
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
	
	/*
	 * 수정모달 이동
	 */
	@ResponseBody
	@RequestMapping(value="apinfoUpdateData.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> apinfoUpdateData(@RequestParam HashMap<String, Object> req, Model model) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {

			ApInfoVo apInfoVo = this.apInfoService.selectApInfo(req);
			
			// conn_time 포멧
			apInfoService.connTimeFormat(apInfoVo);

			model.addAttribute("updHour2", apInfoVo.getUpdHour());
			model.addAttribute("updMin2", apInfoVo.getUpdMin());

			resultMap.put("apInfoVo", apInfoVo);
			resultMap.put("result", "success");
			
		} catch(Exception e) {
 			e.printStackTrace();
 			resultMap.put("result", "error");
 		}	
		return resultMap;
	}
	
	/*
	 * apinfo 운영자 수정
	 */
	@ResponseBody
	@RequestMapping(value="apinfoUpdate.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> apinfoUpdate(ApInfoVo apInfoVo, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		if(apInfoVo.getUpdUnLimit() != null) {
			apInfoVo.setUpdUnLimit("Y");
		} else {
			apInfoVo.setUpdUnLimit("N");
		}
		
		if(apInfoVo.getUpdConnTimeChange() != null) {
			apInfoVo.setUpdConnTimeChange("Y");
		} else {
			apInfoVo.setUpdConnTimeChange("N");
		}
		
		// conn_time 설정
		int contime = 50000;
		if(apInfoVo.getUpdUnLimit().equals("N")) {
			contime=(Integer.parseInt(apInfoVo.getUpdHour())*60) + Integer.parseInt(apInfoVo.getUpdMin());
		}
		apInfoVo.setUpdConnTime(String.valueOf(contime));
		
		try {
			
			
			int chkName = this.apInfoService.updChkName(apInfoVo).size();
			
			if(chkName > 0) {
				resultMap.put("result", "-3");
			} else {
			
				int updResult = this.apInfoService.updateApInfo(apInfoVo);
				
				if(updResult > 0) {	
					
					// 로그 기록
					UserHistoryVo userHistory = new UserHistoryVo();
					userHistory.setGui_code("401");
					userHistory.setJob_name("수정");
					userHistory.setGui_remark("MAC : " + apInfoVo.getUpdMac() + ", WAP Name : " + apInfoVo.getUpdApName() + ", Ship Code : " + apInfoVo.getUpdScode());
					userHistory.setUser_id((String) session.getAttribute("user_id"));
					userHistory.setSql_str("UPDATE TB_AP_INFO SET\r\n"
							+ "			   S_CODE = '"+ apInfoVo.getUpdScode() +"'\r\n"
							+ "			 , AP_NAME = '"+ apInfoVo.getUpdApName() +"'\r\n"
							+ "			 , SERIAL_NO = '"+ apInfoVo.getUpdSerial() +"'\r\n"
							+ "			 , BAND_WIDTH = '"+ apInfoVo.getUpdBand() +"'\r\n"
							+ "			 , CON_USER = '"+ apInfoVo.getUpdConUser() +"'\r\n"
							+ "			 , DESCR = '"+ apInfoVo.getUpdDescr() +"'\r\n"
							+ "			 , IP_NO = '"+ apInfoVo.getUpdIpNumber() +"'\r\n"
							+ "			 , GUI_FLAG = 'Y'\r\n"
							+ "			 , MODIFY_DATE = NOW()\r\n"
							+ "		 WHERE MAC = '"+ apInfoVo.getUpdMac() + "\r\n"
							+ "		   AND S_CODE = '"+ apInfoVo.getUpdScode() +"'");
					this.userHistoryService.insertUserHistory(userHistory);
					
					// subdata 중복검사
					int chkSubData = this.apInfoService.chkSubData2(apInfoVo).size();
					
					if(apInfoVo.getUpdConnTimeChange().equals("Y")) {
						if(chkSubData > 0) {
							int updateApSubY = this.apInfoService.updateApSubY(apInfoVo);
							
							if(updateApSubY > 0) {
								resultMap.put("result", "1");
							} else {
								resultMap.put("result", "0");
							}
						} else {
							int insertApSubY = this.apInfoService.insertApSubY(apInfoVo);
							
							if(insertApSubY > 0) {
								resultMap.put("result", "1");
							} else {
								resultMap.put("result", "0");
							}
						}
					} else {
						if(chkSubData > 0) {
							int updateApSubN = this.apInfoService.updateApSubN(apInfoVo);
							
							if(updateApSubN > 0) {
								resultMap.put("result", "1");
							} else {
								resultMap.put("result", "0");
							}
						} else {
							int insertApSubN = this.apInfoService.insertApSubN(apInfoVo);
							
							if(insertApSubN > 0) {
								resultMap.put("result", "1");
							} else {
								resultMap.put("result", "0");
							}
						}
					}
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
	 * MAC 삭제
	 */
	@ResponseBody
	@RequestMapping(value="apinfoDelete.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> apinfoDelete(ApInfoVo apInfoVo,HttpServletRequest request, HttpSession session) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			int delResult = this.apInfoService.deleteApInfo(apInfoVo);
			
			if(delResult > 0) {
				
				// 로그 기록
				UserHistoryVo userHistory = new UserHistoryVo();
				userHistory.setGui_code("401");
				userHistory.setJob_name("삭제");
				userHistory.setGui_remark("MAC : " + apInfoVo.getMac());
				userHistory.setUser_id((String) session.getAttribute("user_id"));
				userHistory.setSql_str("UPDATE TB_AP_INFO SET\r\n"
						+ "			   DEL_FLAG = 'Y'\r\n"
						+ "			 , DEL_DATE = NOW()\r\n"
						+ "			 , GUI_FLAG = 'Y'\r\n"
						+ "		 WHERE MAC = '"+ apInfoVo.getMac() +"'\r\n"
						+ "		   AND S_CODE = '"+ apInfoVo.getS_code() +"'");
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
	
	/**
	 * 리부팅
	 */
	@ResponseBody
	@RequestMapping(value="reboot.ajax", method=RequestMethod.POST)
	public HashMap<String, Object> reboot(ApInfoVo apInfoVo) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		Integer result = 0;
		
		try {
			
			this.apInfoService.reboot(apInfoVo);
			
//			logger.info("Controller vResult :::: " + apInfoVo.getVResult());
			
			result = apInfoVo.getVResult();
			
			if(result > 0) {
				resultMap.put("result", "1");
			}
		} catch (Exception e) {
//			logger.error("Reboot process failed :::: ", e);
			e.printStackTrace();
			resultMap.put("result", "0");
		}
		return resultMap;
	}
	
	/**
	 * Excel Down
	 */
	@RequestMapping(value="apinfoExcelDownload.do")
	public void suserExcelDownload(HttpServletResponse response, final HttpSession session
			, ApInfoVo apInfoVo, Locale locale) throws Exception {
		
		String scomp = (String)session.getAttribute("comp_id");
		
		if(!scomp.equals("0")) {
			apInfoVo.setScomp(scomp);
		}
		
		try {	
			
			apInfoVo.setStartrow(-1);
			List<ApInfoVo> excelList = this.apInfoService.selectApInfoList(apInfoVo, locale);

			String[] l_aColumns = {
					messageSource.getMessage("select.shipOwner", null, locale),
					messageSource.getMessage("select.shipName", null, locale),
					messageSource.getMessage("list.wapName", null, locale),
					"MAC",
					messageSource.getMessage("list.serialNumber", null, locale),
					messageSource.getMessage("list.bandWidthKBPS", null, locale),
					messageSource.getMessage("list.concurrentUsers", null, locale),
					messageSource.getMessage("list.status", null, locale),
					messageSource.getMessage("list.externalIP", null, locale),
					messageSource.getMessage("list.internalIPband", null, locale),
					messageSource.getMessage("list.communicationServer", null, locale),
					messageSource.getMessage("list.wifiRetentionTime", null, locale),
					messageSource.getMessage("list.description", null, locale),
			};
			
			String xlsName = messageSource.getMessage("apinfo.wapManagement", null, locale);
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
			
			for(ApInfoVo vo : excelList){
				row = sheet.createRow(rowIdx);
				
				StringBuilder sb = new StringBuilder();
                int band = vo.getBand_width();
                if(band == 0) {
                	sb.append(messageSource.getMessage("list.noLimit", null, locale));
                } else {
                	sb.append(band);
                }
                
                String status = vo.getStatus();
                if(status.equals("1")) {
                	vo.setStatus(messageSource.getMessage("select.connected", null, locale));
                } else {
                	vo.setStatus(messageSource.getMessage("select.notConnected", null, locale));
                }
                
                StringBuilder sb2 = new StringBuilder();
                int connFlag = vo.getWifi_conn_flag();
                if(connFlag == 0) {
                	sb2.append(messageSource.getMessage("list.use", null, locale));
                } else {
                	sb2.append(messageSource.getMessage("list.notUsed", null, locale));
                }

//                String time = "";
//                String connTime = vo.getConn_time();
//				
//				if(connTime != null) {	
//					if(connTime.equals("50000") || connTime.equals("0") ) {
//						time = "제한 없음";
//					} else if(Integer.parseInt(connTime) % 60 == 0) {
//						int hour = Integer.parseInt(connTime) / 60;
//						time = String.valueOf(hour) + "시간";
//					} else if(Integer.parseInt(connTime) >= 60) {
//						int hour = Integer.parseInt(connTime) / 60;
//						int min = Integer.parseInt(connTime) % 60;
//						time = String.valueOf(hour) + "시간" + " " + String.valueOf(min) + "분";
//					} else {
//						time = connTime + "분";
//					}
//				} else {
//					time = "제한 없음";
//				}
//				vo.setConn_time(time);
				
				colIdx = 0;
				
				Cell cell;
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getComp_name());	
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getS_name());	
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getAp_name());	
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getMac());	
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getSerial_no());	
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(sb.toString());	
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getCon_user());	
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getStatus());	
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getFw_version());	
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue("192.168."+vo.getIp_no()+".01");	
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(sb2.toString());	
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getConn_time());	
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
		
		String addSql = "";
		
		if(apInfoVo.getSearchCompId().equals("")) {
			addSql = "TB_AP_STATUS B";
		}		
		if(!apInfoVo.getSearchCompId().isEmpty() && apInfoVo.getSearchCompId() != null) {
			addSql = "( SELECT AA.* FROM TB_AP_STATUS AA, TB_SCOMP_INFO BB, TB_SHIP_INFO CC\r\n"
					+ "	 WHERE AA.S_CODE = CC.S_CODE\r\n"
					+ "	   AND BB.COMP_ID = CC.COMP_ID\r\n"
					+ "	   AND BB.COMP_ID = '"+ apInfoVo.getSearchCompId() +"' ) B";
		}
		
		String sql = "SELECT MAC, AP_NAME, SERIAL_NO, IP_NO, COMP_ID\r\n"
				+ "    		 , COMP_NAME, S_NAME, S_CODE, BAND_WIDTH, CON_USER\r\n"
				+ "    		 , STATUS, FW_VERSION, DESCR, WIFI_CONN_FLAG, CONN_TIME\r\n"
				+ "		  FROM (\r\n"
				+ "				SELECT A.MAC MAC, A.AP_NAME AP_NAME, A.SERIAL_NO SERIAL_NO, A.IP_NO IP_NO, A.COMP_ID COMP_ID\r\n"
				+ "				     , A.COMP_NAME COMP_NAME, A.S_NAME S_NAME, A.S_CODE S_CODE, A.BAND_WIDTH BAND_WIDTH, A.CON_USER CON_USER\r\n"
				+ "				     , (CASE WHEN B.STATUS IS NULL THEN '2' ELSE B.STATUS END) STATUS\r\n"
				+ "				     , B.FW_VERSION FW_VERSION, A.DESCR DESCR, A.DEL_FLAG DEL_FLAG, WIFI_CONN_FLAG, CONN_TIME\r\n"
				+ "				  FROM (\r\n"
				+ "				        SELECT B.MAC, B.AP_NAME, B.SERIAL_NO, B.IP_NO, A.COMP_ID\r\n"
				+ "				             , A.COMP_NAME, A.S_NAME, B.S_CODE, B.DESCR, B.DEL_FLAG\r\n"
				+ "				             , B.BAND_WIDTH, B.CON_USER, D.WIFI_CONN_FLAG,D.CONN_TIME\r\n"
				+ "				          FROM (\r\n"
				+ "							    SELECT S.S_CODE, S.S_NAME, S.COMP_ID, C.COMP_NAME\r\n"
				+ "							      FROM TB_SHIP_INFO S, TB_SCOMP_INFO C\r\n"
				+ "							     WHERE S.COMP_ID = C.COMP_ID\r\n"
				+ "				               ) A, TB_AP_INFO B\r\n"
				+ "				          LEFT JOIN TB_AP_INFO_SUB D\r\n"
				+ "				            ON B.MAC = D.MAC\r\n"
				+ "				           AND B.S_CODE = D.S_CODE\r\n"
				+ "				         WHERE A.S_CODE = B.S_CODE\r\n"
				+ "		               ) A \r\n"
				+ "		          LEFT JOIN " + addSql  
				+ "		            ON A.MAC = B.MAC\r\n"
				+ "		      ) C\r\n"
				+ "		WHERE DEL_FLAG = 'N'\r\n";

		if(!apInfoVo.getSearchCompId().isEmpty() && apInfoVo.getSearchCompId() != null) {
			sql += "AND COMP_ID = '"+ apInfoVo.getSearchCompId() +"' \r\n";
		}
		if(apInfoVo.getScomp() != "0" && apInfoVo.getScomp() != null && apInfoVo.getSearchCompId().isEmpty()) {
			sql += "AND COMP_ID = '"+ apInfoVo.getScomp() +"'\r\n";
		}
		if(!apInfoVo.getSearchStatus().isEmpty() && apInfoVo.getSearchStatus() != null) {
			sql += "AND STATUS = '"+ apInfoVo.getSearchStatus() +"' \r\n";
		}
		if(apInfoVo.getSearchShipName() != null && !apInfoVo.getSearchShipName().isEmpty()) {
			sql += "AND S_NAME LIKE CONCAT ('%', '"+apInfoVo.getSearchShipName()+"', '%')\r\n";
		}
		if(apInfoVo.getSearchApName() != null && !apInfoVo.getSearchApName().isEmpty()) {
			sql += "AND AP_NAME LIKE CONCAT ('%', '"+apInfoVo.getSearchApName()+"', '%')\r\n";
		}
		
		sql += "ORDER BY S_CODE, AP_NAME";
		
		// 로그 기록
		UserHistoryVo userHistory = new UserHistoryVo();
		userHistory.setGui_code("401");
		userHistory.setJob_name("엑셀");
		userHistory.setGui_remark("사유 : " + apInfoVo.getExcelMsg());
		userHistory.setUser_id((String) session.getAttribute("user_id"));
		userHistory.setSql_str(sql);
		this.userHistoryService.insertUserHistory(userHistory);
	}
}
