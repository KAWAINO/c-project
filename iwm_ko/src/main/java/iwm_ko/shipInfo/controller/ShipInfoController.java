package iwm_ko.shipInfo.controller;

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

import iwm_ko.comm.paging;
import iwm_ko.shipInfo.model.ShipInfoVo;
import iwm_ko.shipInfo.service.ShipInfoService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/shipinfo")
public class ShipInfoController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "iwm_ko.shipInfo.ShipInfoService")
    private ShipInfoService shipInfoService;
    
    @Autowired
    private UserHistoryService userHistoryService;
    
    @Autowired
    MessageSource messageSource;

//    private String gui_code;
//
//    @PostConstruct
//    public void init() throws Exception {
//        gui_code = userHistoryService.searchGuiCode("rdata");
//    }


    /**
     * 페이지 로딩
     */
    @RequestMapping("shipinfo.do")
    public String shipInfo(@ModelAttribute("shipInfoVo") ShipInfoVo shipInfoVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {


        //프로토콜 목록 리스트
        shipInfoVo.setStartrow((shipInfoVo.getPageno() - 1) * shipInfoVo.getFetchrow());
        String sessionCompId = (String)session.getAttribute("comp_id");
        shipInfoVo.setSessionCompId(sessionCompId);
        
        String scomp = (String)session.getAttribute("comp_id");
        shipInfoVo.setScomp(scomp);
        List<String> compList = this.shipInfoService.selectCompList(scomp);

        model.addAttribute("sessionCompId",sessionCompId);
        List<ShipInfoVo> shipInfoList = this.shipInfoService.selectShipInfoList(shipInfoVo);
//        List<ShipInfoVo> compList = this.shipInfoService.selectShipCompList(shipInfoVo);

        model.addAttribute("shipInfoList", shipInfoList);
        model.addAttribute("compList", compList);


        // 페이징 처리를 위한 총 갯수
        int totalCnt = this.shipInfoService.selectShipInfoCount(shipInfoVo).size();

        //paging
        paging paging = new paging();

        String pagingHTML = paging.getPagingStr(shipInfoVo.getPageno(), shipInfoVo.getFetchrow(), totalCnt);
        model.addAttribute("pagingHTML", pagingHTML);

        //search data return
        model.addAttribute("shipInfoVo", shipInfoVo);

        //로그 기록
//        if(!(shipInfoVo.getSearchApmsStatus() == null || shipInfoVo.getSearchApmsStatus().equals("")
//                && shipInfoVo.getSearchCompName() == null || shipInfoVo.getSearchCompName().equals("")
//                && shipInfoVo.getSearchSCode() == null || shipInfoVo.getSearchSCode().equals("")
//                && shipInfoVo.getSearchSName() == null || shipInfoVo.getSearchSName().equals(""))){


        // 로그 기록
        UserHistoryVo userHistory = new UserHistoryVo();
        userHistory.setGui_code("102");
        userHistory.setJob_name("검색");
        userHistory.setGui_remark("Data Search");
        userHistory.setUser_id((String) session.getAttribute("user_id"));
        userHistory.setSql_str("");
        userHistoryService.insertUserHistory(userHistory);
//
//        }
        return "shipinfo/shipinfo";
    }


    /**
     * 등록
     */
    @ResponseBody
    @RequestMapping(value = "shipInfoAdd.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> shipInfoAdd(ShipInfoVo shipInfoVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            String chkName = this.shipInfoService.chkShipName(shipInfoVo);

            if (chkName == null) {
                int chkSCode = this.shipInfoService.chkSCode(shipInfoVo).size();

                if (chkSCode > 0 ){
                    resultMap.put("result", "4");
                    return resultMap;
                }else if (chkSCode == 0) {
                    String chkDelFlag = this.shipInfoService.chkDelFlag(shipInfoVo);
                    int insResult = 0;
                    if (chkDelFlag != null && chkDelFlag.equals("Y")) {
                        //기존 s_code존재시 Y -> N 변경
                        insResult = this.shipInfoService.updateShipInsert(shipInfoVo);

                        UserHistoryVo userHistory = new UserHistoryVo();
                        userHistory.setGui_code("102");
                        userHistory.setJob_name("추가");
                        userHistory.setGui_remark("Ship Code : " + shipInfoVo.getAddCode() + ", Ship Name : " + shipInfoVo.getAddName() + ", Ship owner : " + shipInfoVo.getAddCompId());
                        userHistory.setUser_id((String) session.getAttribute("user_id"));
                        userHistory.setSql_str("update TB_SHIP_INFO set COMP_ID= " + shipInfoVo.getAddCompId() + ", S_NAME = " + shipInfoVo.getAddName() + ", DESCR = " + shipInfoVo.getAddDescr() + ", GUI_FLAG='Y', DEL_FLAG='N', MODIFY_DATE=NOW() where S_CODE= " + shipInfoVo.getAddCode());
                        userHistoryService.insertUserHistory(userHistory);
                    } else {
                        // 기존 s_code 미존재시 insert
                        insResult = this.shipInfoService.insertShipInfo(shipInfoVo);

                        UserHistoryVo userHistory = new UserHistoryVo();
                        userHistory.setGui_code("102");
                        userHistory.setJob_name("추가");
                        userHistory.setGui_remark("Ship Code : " + shipInfoVo.getAddCode() + ", Ship Name : " + shipInfoVo.getAddName() + ", Ship owner : " + shipInfoVo.getAddCompId());
                        userHistory.setUser_id((String) session.getAttribute("user_id"));
                        userHistory.setSql_str("insert into TB_SHIP_INFO(S_CODE,COMP_ID, S_NAME,DESCR,MODIFY_DATE,DEL_DATE)values(" + shipInfoVo.getAddCode() + "," + shipInfoVo.getAddCompId() + "," + shipInfoVo.getAddName() + "," + shipInfoVo.getAddDescr() + ",NOW(),NOW())");
                        userHistoryService.insertUserHistory(userHistory);

                    }

                    if (insResult > 0) {
                        resultMap.put("result", "1");
                        return resultMap;
                    } else {
                        resultMap.put("result", "0");
                        return resultMap;
                    }
                }

            }

            resultMap.put("result", "3");
            return resultMap;

        } catch (Exception e) {

            e.printStackTrace();
            resultMap.put("result", "error");

        }
        return resultMap;
    }







    /**
     * 수정 Modal 이동
     */
    @ResponseBody
    @RequestMapping(value = "shipInfoSetUpdateData.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> shipInfoSetUpdateData(@RequestParam HashMap<String, Object> req, final HttpSession session) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {

            ShipInfoVo shipInfoVo = this.shipInfoService.selectCompInfo(req);
            shipInfoVo.setChkDel(this.shipInfoService.chkDel(shipInfoVo));

            shipInfoVo.setSelectedCompId(shipInfoVo.getComp_id());

            resultMap.put("shipInfoVo", shipInfoVo);
            resultMap.put("result", "success");
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }
        return resultMap;
    }


    /**
     *  수정
     */
    @ResponseBody
    @RequestMapping(value="shipInfoUpdate.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> shipInfoUpdate(ShipInfoVo shipInfoVo,HttpServletRequest request, final HttpSession session,Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();


        try {

            shipInfoVo.setAddName(shipInfoVo.getUpdName());
            shipInfoVo.setAddCompId(shipInfoVo.getUpdCompId());
            shipInfoVo.setAddCode(shipInfoVo.getUpdCode());


            if(shipInfoVo.getU_updName().equals(shipInfoVo.getUpdName())){

                    int updResult = this.shipInfoService.updateShipInfo(shipInfoVo);

                    // 로그 기록
                    UserHistoryVo userHistory = new UserHistoryVo();
                    userHistory.setGui_code("102");
                    userHistory.setJob_name("수정");
                    userHistory.setGui_remark("Ship owner name : "+shipInfoVo.getUpdName()+", Description : "+shipInfoVo.getUpdDescr());
                    userHistory.setUser_id((String) session.getAttribute("user_id"));
                    userHistory.setSql_str(" update TB_SHIP_INFO  set COMP_ID= " + shipInfoVo.getUpdCompId() + "S_NAME=" + shipInfoVo.getUpdName() + "DESCR=" + shipInfoVo.getUpdDescr() +
                            "GUI_FLAG='Y', MODIFY_DATE=NOW() where S_CODE= "+shipInfoVo.getU_updCode() + ")");
                    userHistoryService.insertUserHistory(userHistory);

                    if(updResult > 0) {
                        resultMap.put("result", "1");
                        return resultMap;
                    }else {
                        resultMap.put("result", "0");
                        return resultMap;
                    }
            }else {
                String chkName = this.shipInfoService.chkShipName(shipInfoVo);

                if (chkName == null) {

                    //update
                    int updResult = this.shipInfoService.updateShipInfo(shipInfoVo);

                    // 로그 기록
                    UserHistoryVo userHistory = new UserHistoryVo();
                    userHistory.setGui_code("102");
                    userHistory.setJob_name("수정");
                    userHistory.setGui_remark("Ship owner name : "+shipInfoVo.getUpdName()+", Description : "+shipInfoVo.getUpdDescr());
                    userHistory.setUser_id((String) session.getAttribute("user_id"));
                    userHistory.setSql_str(" update TB_SHIP_INFO  set COMP_ID= " + shipInfoVo.getUpdCompId() + "S_NAME=" + shipInfoVo.getUpdName() + "DESCR=" + shipInfoVo.getUpdDescr() +
                            "GUI_FLAG='Y', MODIFY_DATE=NOW() where S_CODE= "+shipInfoVo.getU_updCode() + ")");
                    userHistoryService.insertUserHistory(userHistory);
                    if(updResult > 0) {
                        resultMap.put("result", "1");
                    }else {
                        resultMap.put("result", "0");
                    }
                }else {
                    resultMap.put("result", "3");
                    return resultMap;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "0");
        }

        return resultMap;
    }



    /**
     *  삭제
     */
    @ResponseBody
    @RequestMapping(value="shipInfoDelete.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> shipInfoDelete(ShipInfoVo shipInfoVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            //update

            int updResult = this.shipInfoService.deleteShipInfo(shipInfoVo);
            shipInfoVo.setResult(0);

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("102");
            userHistory.setJob_name("삭제");
            userHistory.setGui_remark("Ship owner name : " + shipInfoVo.getS_code() );
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("{call RPC_CLEAN_SCODE (" + shipInfoVo.getS_code() + ",Types.INTEGER)}");

            userHistoryService.insertUserHistory(userHistory);

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
     *  Excel
     */
    @RequestMapping(value="shipInfoExcelDownload.do")
    public void shipInfoExcelDownload(HttpServletResponse response, final HttpSession session, ShipInfoVo shipInfoVo
    		, Locale locale) throws Exception {

        try {

            shipInfoVo.setStartrow(-1);
            List<ShipInfoVo> excelList = this.shipInfoService.selectShipInfoList(shipInfoVo);

            String[] l_aColumns = {
            		messageSource.getMessage("list.shipCode", null, locale), 
            		messageSource.getMessage("select.shipOwner", null, locale), 
            		messageSource.getMessage("select.shipName", null, locale), 
            		messageSource.getMessage("list.usage.month", null, locale), 
            		messageSource.getMessage("list.usage.today", null, locale), 
            		messageSource.getMessage("list.OWMstatus", null, locale) , 
            		messageSource.getMessage("list.description", null, locale)
            		};

            String xlsName = messageSource.getMessage("shipinfo.shipinfo", null, locale);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            String today = formatter.format(new java.util.Date());
            String fileName = xlsName + "_" + today + ".xlsx";

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
 			
 			for(ShipInfoVo vo : excelList){
 				row = sheet.createRow(rowIdx);

 				colIdx = 0;
 				
				Cell cell;
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getS_code());
				cell.setCellStyle(csBody);

				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getComp_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getS_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				if(vo.getAmt_use_month() == null) {
					vo.setAmt_use_month("0");
				}
				cell.setCellValue(vo.getAmt_use_month());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				if(vo.getAmt_use_day() == null) {
					vo.setAmt_use_day("0");
				}
				cell.setCellValue(vo.getAmt_use_day());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				if (vo.getApms_status().equals("1")) {
					vo.setApms_status(messageSource.getMessage("select.connected", null, locale));
					cell.setCellValue(vo.getApms_status());
				} else if (vo.getApms_status().equals("2")) {
					vo.setApms_status(messageSource.getMessage("select.notConnected", null, locale));
					cell.setCellValue(vo.getApms_status());
				}
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getDescr());
				cell.setCellStyle(csBody);               
                
//                for(int idx = 0; idx < colIdx; idx++) {
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

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("102");
            userHistory.setJob_name("엑셀");
            userHistory.setGui_remark("사유 : " + shipInfoVo.getExcel_msg());
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str(" select SI.S_CODE, SI.S_NAME, CI.COMP_NAME, CI.COMP_ID, SI.DESCR, SI.DEL_FLAG\n" +
                    " from TB_SHIP_INFO SI, TB_SCOMP_INFO CI\n" +
                    " where SI.COMP_ID=CI.COMP_ID\n" +
                    " ) A left join TB_SHIP_STATUS B \n" +
                    " on A.s_code = B.s_code\n" +
                    " ) C \n" +
                    " where DEL_FLAG='N'\n" +
                    " order by S_CODE");

            userHistoryService.insertUserHistory(userHistory);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
