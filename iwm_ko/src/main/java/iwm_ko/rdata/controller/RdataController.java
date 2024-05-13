package iwm_ko.rdata.controller;

import java.text.DecimalFormat;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import iwm_ko.comm.paging;
import iwm_ko.rdata.model.RdataVo;
import iwm_ko.rdata.service.RdataService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/rdata")
public class RdataController{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "iwm_ko.rdata.RdataService")
    private RdataService rdataService;

    @Autowired
    private UserHistoryService userHistoryService;
    
    @Autowired
    private MessageSource messageSource;

//    private String gui_code;
//
//    @PostConstruct
//    public void init() throws Exception {
//        gui_code = userHistoryService.searchGuiCode("rdata");
//    }


    /**
     * 페이지 로딩
     */
    @RequestMapping("rdata.do")
    public String shipComp(@ModelAttribute("rdataVo") RdataVo rdataVo, HttpServletRequest request, final HttpSession session
    		, Model model, Locale locale)throws Exception{

        //프로토콜 목록 리스트
        rdataVo.setStartrow((rdataVo.getPageno() - 1) * rdataVo.getFetchrow());
        String sessionCompId = (String)session.getAttribute("comp_id");
        rdataVo.setSessionCompId(sessionCompId);

        model.addAttribute("sessionCompId",sessionCompId);
        
        List<RdataVo> rdataList = this.rdataService.selectRdataList(rdataVo, locale);
        model.addAttribute("rdataList", rdataList);

        List<RdataVo> compList = this.rdataService.selectShipCompList(rdataVo);
        model.addAttribute("compList",compList);

        // 페이징 처리를 위한 총 갯수
        int totalCnt = this.rdataService.selectRdataCount(rdataVo).size();


//        if(!(rdataVo.getSearchCompId() == null || rdataVo.getSearchCompId().equals("")
//                && rdataVo.getSearchRateName() == null || rdataVo.getSearchRateName().equals("")
//                && rdataVo.getSearchSName() == null || rdataVo.getSearchSName().equals("")
//               )){

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("105");
            userHistory.setJob_name("검색");
            userHistory.setGui_remark("Data Search");
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("");
            userHistoryService.insertUserHistory(userHistory);

  //      }


        //paging
        paging paging = new paging();
        String pagingHTML = paging.getPagingStr(rdataVo.getPageno(), rdataVo.getFetchrow() , totalCnt);
        model.addAttribute("pagingHTML", pagingHTML);

        //search data return
        model.addAttribute("rdataVo", rdataVo);


        return "rdata/rdata";
    }

    /**
     * 선주사별 선박명
     */
    @ResponseBody
    @PostMapping("/compList.ajax")
    public HashMap<String, Object> compListAjax(@RequestParam HashMap<String, Object> req, final HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            String compId = req.get("compId").toString();
            List<RdataVo> shipNameList = rdataService.shipNameList(compId);

            resultMap.put("shipNameList", shipNameList);
            resultMap.put("result", "success");
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }

        return resultMap;
    }

    /**
     *  선박별 정책명
     */
    @ResponseBody
    @PostMapping("/s_name.ajax")
    public HashMap<String, Object> s_nameAjax(@RequestParam HashMap<String, Object> req, final HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            String s_name = req.get("s_name").toString();
            List<RdataVo> rdataList = rdataService.rdataList(s_name);
            List<RdataVo> crewIdList = rdataService.crewIdList(s_name);
            resultMap.put("rdataList", rdataList);
            resultMap.put("crewIdList", crewIdList);
            resultMap.put("result", "success");
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }

        return resultMap;
    }

    /**
     * 등록
     */
    @ResponseBody
    @RequestMapping(value="rdataAdd.ajax", method= RequestMethod.POST)
    public HashMap<String, Object> rdataAdd(RdataVo rdataVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {

            //중복검사
            int chkRdata = this.rdataService.chkRdata(rdataVo).size();
            if(chkRdata > 0) {
                resultMap.put("result", "-2");
                return resultMap;
            }else {

                if(rdataVo.getAddDay().equals("")){
                    rdataVo.setAddDay("0");
                }
                if(rdataVo.getAddUse().equals("")){
                    rdataVo.setAddUse("0");
                }
                if(rdataVo.getAddMin().equals("")){
                    rdataVo.setAddMin("0");
                }
                int insResult = this.rdataService.insertRdata(rdataVo);

                 //로그 기록
                UserHistoryVo userHistory = new UserHistoryVo();
                userHistory.setGui_code("105");
                userHistory.setJob_name("추가");
                userHistory.setGui_remark("Data usage policy : "+rdataVo.getAddRateName() + ", Monthly supply(MB) : "+rdataVo.getAddMonth()+", Ship Code : "+rdataVo.getAddName());
                userHistory.setUser_id((String) session.getAttribute("user_id"));
                userHistory.setSql_str("insert into TB_CREW_RATE " +
                        "(S_CODE,RATE_NAME, AMT_TOTAL_MONTH, C_LIMIT_DAY_AMT, C_LIMIT_CONT_AMT, C_LIMIT_CONT_TIME, C_TIME_FROM, C_TIME_TO, C_APPLY_DAY, C_DAY_FROM, C_DAY_TO, C_TIME_FROM2, C_TIME_TO2, C_TIME_FROM3,C_TIME_TO3 )values" +
                        rdataVo.getAddName()+","+rdataVo.getAddRateName()+","+rdataVo.getAddMonth()+","+rdataVo.getAddDay()+","+rdataVo.getAddUse()+","+rdataVo.getAddMin()+","+rdataVo.getAddTimeFrom()+","+rdataVo.getAddTimeTo()+","+rdataVo.getAddApplyDays()+","+rdataVo.getAddDayFrom()
                        +","+rdataVo.getAddDayTo()+","+rdataVo.getAddTimeFrom2()+","+rdataVo.getAddTimeTo2()+","+rdataVo.getAddTimeFrom3()+","+rdataVo.getAddTimeTo3());

                userHistoryService.insertUserHistory(userHistory);

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
    @RequestMapping(value = "rdataSetUpdateData.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> rdataSetUpdateData(@RequestParam HashMap<String, Object> req, final HttpSession session) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            RdataVo rdataVo = this.rdataService.selectRdataInfo(req);
            rdataVo.setChKDel(this.rdataService.chkDel(rdataVo));

            resultMap.put("rdataVo", rdataVo);
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
    @RequestMapping(value="rdataUpdate.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> rdataUpdate(RdataVo rdataVo,HttpServletRequest request, final HttpSession session,Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();


        try {
            //update
            int updResult = this.rdataService.updateRdata(rdataVo);

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("105");
            userHistory.setJob_name("수정");
            userHistory.setGui_remark(this.rdataService.updateHistory(rdataVo));
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str(" update TB_CREW_RATE set GUI_FLAG='Y',S_CODE = " +
                   rdataVo.getUpdSCode()+ ",AMT_TOTAL_MONTH= "+ rdataVo.getUpdMonth() + ",C_LIMIT_DAY_AMT ="
                    +rdataVo.getUpdDay() + " ,C_LIMIT_CONT_AMT ="+rdataVo.getUpdUse() + " ,C_LIMIT_CONT_TIME ="
                    + rdataVo.getUpdMin() + " ,C_TIME_FROM= " + rdataVo.getUpdTimeFrom() + " ,C_TIME_TO = " +rdataVo.getUpdTimeTo()+
                    " ,C_APPLY_DAY =" + rdataVo.getUpdApplyDays() + " ,C_DAY_FROM = "+ rdataVo.getUpdDayFrom() + " ,C_DAY_TO =  "
                    + rdataVo.getUpdDayTo() + " ,C_TIME_FROM2 = " + rdataVo.getUpdTimeFrom2() +" ,C_TIME_TO2 = "+ rdataVo.getUpdTimeTo2() +
                    " ,C_TIME_FROM3 = " + rdataVo.getUpdTimeFrom3() + " ,C_TIME_TO3 = " + rdataVo.getUpdTimeTo3() + " where RATE_NAME="
                    + rdataVo.getU_updRateName() + " and S_CODE= " + rdataVo.getUpdSCode());

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
     *  삭제
     */
    @ResponseBody
    @RequestMapping(value="rdataDelete.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> rdataDelete(RdataVo rdataVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            //update

            int updResult = this.rdataService.deleteRdata(rdataVo);

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("105");
            userHistory.setJob_name("삭제");
            userHistory.setGui_remark("Data usage policy : " + rdataVo.getRate_name() );
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str(" delete from TB_CREW_RATE where RATE_NAME=" + rdataVo.getRate_name() + "and S_CODE= "+rdataVo.getS_code());

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
    @RequestMapping(value="rdataExcelDownload.do")
    public void rdataExcelDownload(HttpServletResponse response, final HttpSession session, RdataVo rdataVo
    		, Locale locale) throws Exception {

        try {
            rdataVo.setSessionCompId((String)session.getAttribute("comp_id") );
            rdataVo.setStartrow(-1);
            List<RdataVo> excelList = this.rdataService.selectRdataList(rdataVo, locale);

            String[] l_aColumns = { 
            		messageSource.getMessage("select.shipOwner", null, locale), 
            		messageSource.getMessage("select.shipName", null, locale), 
            		messageSource.getMessage("list.dataUsagePolicy", null, locale), 
            		messageSource.getMessage("list.monthlySupply", null, locale), 
            		messageSource.getMessage("list.dailyUseLimit", null, locale), 
            		messageSource.getMessage("list.continuousUseLimit", null, locale),
            		messageSource.getMessage("list.continuousUseTimeout", null, locale), 
            		messageSource.getMessage("list.usageTimeLimitRange", null, locale),
            		messageSource.getMessage("list.usageTimeLimitRange2", null, locale), 
            		messageSource.getMessage("list.usageTimeLimitRange3", null, locale),
            		messageSource.getMessage("list.dayOfUse", null, locale),
            		messageSource.getMessage("list.periodOfUse", null, locale)  
            		};

            String xlsName = messageSource.getMessage("rdata.dataUsagePolicy", null, locale) ;
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

            for (RdataVo vo : excelList) {
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
				cell.setCellValue(vo.getRate_name());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                double temp = Double.parseDouble(vo.getAmt_total_month());
                String fmtAmtTotalMonth = decimalFormat.format(temp);
				cell.setCellValue(fmtAmtTotalMonth);
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getLimit_day_amt());
				cell.setCellStyle(csBody);

				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getLimit_cont_amt());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getLimit_cont_time());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getTime_from() + "~" + vo.getTime_to());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getTime_from2() + "~" + vo.getTime_to2());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getTime_from3() + "~" + vo.getTime_to3());
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(convertNumbersToDays(vo.getApply_day()));
				cell.setCellStyle(csBody);
				
				cell = row.createCell(colIdx++);
				cell.setCellValue(vo.getDay_from() + "~" + vo.getDay_to());
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
            userHistory.setGui_code("105");
            userHistory.setJob_name("엑셀");
            userHistory.setGui_remark("사유 : " + rdataVo.getExcel_msg());
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            String sql = "   select B.RATE_NAME AS rate_name, A.COMP_NAME AS comp_name, A.COMP_ID AS comp_id, A.S_NAME AS s_name, A.S_CODE AS s_code, B.AMT_TOTAL_MONTH AS amt_total_month" +
                    ", B.C_LIMIT_DAY_AMT AS limit_day_amt, B.C_LIMIT_CONT_AMT AS limit_cont_amt, B.C_LIMIT_CONT_TIME AS limit_cont_time, B.C_TIME_FROM AS time_from, B.C_TIME_TO AS time_to" +
                    ",B.C_APPLY_DAY AS apply_day,C_DAY_FROM AS day_from, C_DAY_TO AS day_to, B.C_TIME_FROM2 AS time_from2, B.C_TIME_TO2 AS time_to2, B.C_TIME_FROM3 AS time_from3, B.C_TIME_TO3 AS time_to3" +
                    "from ( select S.COMP_ID, C.COMP_NAME, S.S_NAME, S.S_CODE from TB_SHIP_INFO S, TB_SCOMP_INFO C where S.COMP_ID=C.COMP_ID)A, TB_CREW_RATE B" +
                    "where A.S_CODE=B.S_CODE";

            if (rdataVo.getSearchCompId() != null && rdataVo.getSearchCompId() != ""){
                sql += " AND A.COMP_ID = "+ rdataVo.getSearchCompId();
            }
            if (rdataVo.getSearchSName() != null && rdataVo.getSearchSName() != ""){
                sql += " AND A.S_NAME LIKE CONCAT('%' ," + rdataVo.getSearchSName() + ", '%')";
            }
            if (rdataVo.getSearchRateName() != null && rdataVo.getSearchRateName() != ""){
                sql += " AND B.RATE_NAME LIKE CONCAT('%' , " + rdataVo.getSearchRateName() + ",'%')";
            }
            userHistory.setSql_str(sql);

            userHistoryService.insertUserHistory(userHistory);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String convertNumbersToDays(String numbers) {
        String[] days = {"일", "월", "화", "수", "목", "금", "토"};
        StringBuilder dayNames = new StringBuilder();

        String[] numberArray = numbers.split(",");

        for (String number : numberArray) {
            try {
                if (!number.trim().isEmpty()) {
                    int dayNumber = Integer.parseInt(number.trim()) - 1;
                    dayNames.append(days[dayNumber]).append(", ");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (dayNames.length() > 0) {
            dayNames.setLength(dayNames.length() - 2);
        }

        return dayNames.toString();
    }

}


