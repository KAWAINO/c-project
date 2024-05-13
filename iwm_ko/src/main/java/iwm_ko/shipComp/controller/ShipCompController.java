package iwm_ko.shipComp.controller;

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
import iwm_ko.shipComp.model.ShipCompVo;
import iwm_ko.shipComp.service.ShipCompService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/shipcomp")
public class  ShipCompController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private MessageSource messageSource;

    @Resource(name = "iwm_ko.shipComp.ShipCompService")
    private ShipCompService shipCompService;
    
    @Autowired
    private UserHistoryService userHistoryService;

//    private String gui_code;
//
//    @PostConstruct
//    public void init() throws Exception {
//        gui_code = userHistoryService.searchGuiCode("shipcomp");
//    }
    /**
     * 페이지 로딩
     */
    @RequestMapping("shipcomp.do")
    public String shipComp(@ModelAttribute("shipCompVo") ShipCompVo shipCompVo, HttpServletRequest request, final HttpSession session, Model model)throws Exception{

        shipCompVo.setFetchrow(20);
        //프로토콜 목록 리스트
        shipCompVo.setStartrow((shipCompVo.getPageno() - 1) * shipCompVo.getFetchrow());

        String comp_id = ((String) session.getAttribute("comp_id"));
        String sessionCompId = (String)session.getAttribute("comp_id");
        shipCompVo.setSessionCompId(sessionCompId);

        model.addAttribute("sessionCompId",sessionCompId);
        model.addAttribute("comp_id",comp_id);

        List<ShipCompVo> shipCompList = this.shipCompService.selectShipCompList(shipCompVo);
        model.addAttribute("shipCompList", shipCompList);

        // 페이징 처리를 위한 총 갯수
        int totalCnt = this.shipCompService.selectShipCompCount(shipCompVo).size();

        // 로그 기록
        UserHistoryVo userHistory = new UserHistoryVo();
        userHistory.setGui_code("101");
        userHistory.setJob_name("검색");
        userHistory.setGui_remark("Data Search");
        userHistory.setUser_id((String) session.getAttribute("user_id"));
        userHistory.setSql_str("");

        userHistoryService.insertUserHistory(userHistory);

        //paging
        paging paging = new paging();
        String pagingHTML = paging.getPagingStr(shipCompVo.getPageno(), shipCompVo.getFetchrow() , totalCnt);
        model.addAttribute("pagingHTML", pagingHTML);

        //search data return
        model.addAttribute("shipCompVo", shipCompVo);

        return "shipcomp/shipcomp";
    }

    /**
     * 등록
     */
    @ResponseBody
    @RequestMapping(value="shipCompAdd.ajax", method= RequestMethod.POST)
    public HashMap<String, Object> shipCompAdd(ShipCompVo shipCompVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        shipCompVo.setSessionCompId((String) session.getAttribute("comp_id"));
        shipCompVo.setAddCompId("-1");
        int insResult = 0;
        try {

            //중복검사
            int chkProtocol = this.shipCompService.chkInsertShipComp(shipCompVo).size();
            if(chkProtocol > 0) {
                resultMap.put("result", "-2");
                return resultMap;
            }else {

                String chkFlag = this.shipCompService.chkFlag(shipCompVo);
                if (chkFlag != null && chkFlag.equals("Y")){
                    insResult = this.shipCompService.updateInsertShipComp(shipCompVo);

                    // 로그 기록
                    UserHistoryVo userHistory = new UserHistoryVo();
                    userHistory.setGui_code("101");
                    userHistory.setJob_name("추가");
                    userHistory.setGui_remark("Ship owner name : "+shipCompVo.getAddCompName()+", Description : "+shipCompVo.getAddDescr());
                    userHistory.setUser_id((String) session.getAttribute("user_id"));
                    userHistory.setSql_str("update TB_SCOMP_INFO set SHIP_HOLD= " + shipCompVo.getAddCompCnt() + " , DESCR=" + shipCompVo.getAddDescr()+ ", DEL_FLAG='N', MODIFY_DATE=NOW(), GUI_FLAG='Y' where COMP_NAME= " + shipCompVo.getAddCompName());

                    userHistoryService.insertUserHistory(userHistory);


                }else {
                    int compIdResult = this.shipCompService.compIdSeq();
                    shipCompVo.setAddCompId(String.valueOf(compIdResult));
                    //insert
                     insResult = this.shipCompService.insertShipComp(shipCompVo);

                    // 로그 기록
                    UserHistoryVo userHistory = new UserHistoryVo();
                    userHistory.setGui_code("101");
                    userHistory.setJob_name("추가");
                    userHistory.setGui_remark("Ship owner name : "+shipCompVo.getAddCompName()+", Description : "+shipCompVo.getAddDescr());
                    userHistory.setUser_id((String) session.getAttribute("user_id"));
                    userHistory.setSql_str("insert into TB_SCOMP_INFO (COMP_ID, COMP_NAME, SHIP_HOLD, DESCR, MODIFY_DATE, DEL_DATE) values (" +
                            shipCompVo.getAddCompId() + " ," +shipCompVo.getAddCompName()+","+shipCompVo.getAddCompCnt() + " ," +shipCompVo.getAddDescr() + ", NOW(), NOW())");

                    userHistoryService.insertUserHistory(userHistory);

                }
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
    @RequestMapping(value = "shipCompSetUpdateData.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> shipCompSetUpdateData(@RequestParam HashMap<String, Object> req, final HttpSession session) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            ShipCompVo shipCompVo = this.shipCompService.selectCompInfo(req);

            shipCompVo.setSessionCompId((String) session.getAttribute("comp_id"));
            shipCompVo.setChkDel(this.shipCompService.chkDel(shipCompVo));


            resultMap.put("shipCompVo", shipCompVo);
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
    @RequestMapping(value="shipCompUpdate.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> shipCompUpdate(ShipCompVo shipCompVo,HttpServletRequest request, final HttpSession session,Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();


        try {
            shipCompVo.setAddCompId(shipCompVo.getUpdCompId());
            shipCompVo.setAddCompName(shipCompVo.getUpdCompName());

            //중복검사
            int chkProtocol = this.shipCompService.chkInsertShipComp(shipCompVo).size();
            if(chkProtocol > 0) {
                resultMap.put("result", "-2");
                return resultMap;
            }else {

                //update
                int updResult = this.shipCompService.updateShipComp(shipCompVo);

                // 로그 기록
                UserHistoryVo userHistory = new UserHistoryVo();
                userHistory.setGui_code("101");
                userHistory.setJob_name("수정");
                userHistory.setGui_remark("Ship owner name : "+shipCompVo.getUpdCompName()+", Description : "+shipCompVo.getUpdDescr());
                userHistory.setUser_id((String) session.getAttribute("user_id"));
                userHistory.setSql_str("update TB_SCOMP_INFO set COMP_NAME=" + shipCompVo.getUpdCompName() + ", SHIP_HOLD=" +shipCompVo.getUpdCompHold()+", DESCR=" + shipCompVo.getUpdDescr() +", MODIFY_DATE=NOW(), GUI_FLAG='Y' where COMP_ID=" + shipCompVo.getUpdCompId());

                userHistoryService.insertUserHistory(userHistory);

                if(updResult > 0) {
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
     *  삭제
     */
    @ResponseBody
    @RequestMapping(value="shipCompDelete.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> shipCompDelete(ShipCompVo shipCompVo,HttpServletRequest request, final HttpSession session,Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            //update
            int updResult = this.shipCompService.deleteShipComp(shipCompVo);

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("101");
            userHistory.setJob_name("삭제");
            userHistory.setGui_remark("Ship owner name : " + shipCompVo.getComp_name() );
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str(" update TB_SCOMP_INFO set DEL_FLAG='Y',DEL_DATE=NOW(),GUI_FLAG='Y' where COMP_ID=" + shipCompVo.getUpdCompId());

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
     * 운영자 Excel
     */
    @RequestMapping(value="shipCompExcelDownload.do")
    public void shipCompExcelDownload(HttpServletResponse response, final HttpSession session, ShipCompVo shipCompVo, Locale locale) throws Exception {

        try {

            shipCompVo.setStartrow(-1);
            List<ShipCompVo> excelList = this.shipCompService.selectShipCompList(shipCompVo);

            String[] l_aColumns = {
            		messageSource.getMessage("list.shipOwnerName", null, locale), 
            		messageSource.getMessage("list.numberOfShips", null, locale), 
            		messageSource.getMessage("list.owmStatus", null, locale), 
            		messageSource.getMessage("list.wapStatus", null, locale), 
            		messageSource.getMessage("list.description", null, locale)
            		};

            String xlsName = messageSource.getMessage("shipComp.shipOwnerStatus", null, locale);
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

            for (ShipCompVo vo : excelList) {
                row = sheet.createRow(rowIdx);

                colIdx = 0;
                
				Cell cell;
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getComp_name());
				cell.setCellStyle(csBody);

				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getShip_hold());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getS_cnt());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getA_cnt());
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
            userHistory.setGui_code("101");
            userHistory.setJob_name("엑셀");
            userHistory.setGui_remark("사유 : " + shipCompVo.getExcel_msg());
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("select COMP_NAME as '선주사명', SHIP_HOLD as 'NN_보유 선박 개수',\n" +
                    " (select count(*) from TB_SHIP_INFO S\n" +
                    " where C.COMP_ID = S.COMP_ID\n" +
                    " and S.DEL_FLAG='N') 'NN_OWM 현황',\n" +
                    " (select count(A.MAC) from TB_AP_INFO A, TB_SHIP_INFO SS\n" +
                    " where C.COMP_ID = SS.COMP_ID\n" +
                    " and SS.S_CODE = A.S_CODE\n" +
                    " and A.DEL_FLAG='N') 'NN_WAP 현황',\n" +
                    " DESCR as '설명' from TB_SCOMP_INFO C where DEL_FLAG='N'\n" +
                    " order by COMP_ID");

            userHistoryService.insertUserHistory(userHistory);

        } catch (Exception e) {
                    e.printStackTrace();
                }

            }



    }
