package iwm_ko.crewcdr.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import iwm_ko.comm.paging;
import iwm_ko.crewcdr.model.CrewcdrVo;
import iwm_ko.crewcdr.service.CrewcdrService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/crewcdr")
public class CrewcdrController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "iwm_ko.crewcdr.CrewcdrService")
    private CrewcdrService crewcdrService;
    
    @Autowired
    private UserHistoryService userHistoryService;
    
    @Autowired
    private MessageSource messageSource;

//    private String gui_code;
//
//    @PostConstruct
//    public void init() throws Exception {
//        gui_code = userHistoryService.searchGuiCode("crewcdr");
//    }

    /**
     * 페이지 로딩
     */
    @RequestMapping("crewcdr.do")
    public String crewcdr(@ModelAttribute("crewcdrVo") CrewcdrVo crewcdrVo, HttpServletRequest request, final HttpSession session, Model model)throws Exception{


        crewcdrVo.setFetchrow(20);

        crewcdrVo.setStartrow((crewcdrVo.getPageno() - 1) * crewcdrVo.getFetchrow());
        String sessionCompId = (String)session.getAttribute("comp_id");
        crewcdrVo.setComp_id(sessionCompId);

        model.addAttribute("sessionCompId",sessionCompId);
        //selectbox 회사 목록
        List<CrewcdrVo> compList = this.crewcdrService.selectShipCompList(crewcdrVo);
        model.addAttribute("compList",compList);

        int totalCnt = -1;

        // 검색조건 있을경우
        if(!(crewcdrVo.getSearchCompId() == null || crewcdrVo.getSearchCompId().equals("")
                && crewcdrVo.getSearchCrewId() == null || crewcdrVo.getSearchCrewId().equals("")
                && crewcdrVo.getSearchSCode() == null || crewcdrVo.getSearchSCode().equals("")
        )){

            SimpleDateFormat hyphenFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat nonFormat = new SimpleDateFormat("yyyyMMdd");


            Date startDate = hyphenFormat.parse(crewcdrVo.getSearchStartDate());
            crewcdrVo.setSearchStartDate(nonFormat.format(startDate));
            Date endDate = hyphenFormat.parse(crewcdrVo.getSearchEndDate());
            crewcdrVo.setSearchEndDate(nonFormat.format(endDate));
            String finalSearchStartDate = crewcdrVo.getSearchStartDate() + crewcdrVo.getSearchStartHour()+"0000";
            String finalSearchEndDate = crewcdrVo.getSearchEndDate() + crewcdrVo.getSearchEndHour()+"0000";
            crewcdrVo.setFinalSearchStartDate(finalSearchStartDate);
            crewcdrVo.setFinalSearchEndDate(finalSearchEndDate);

            Date chkSearchStartDate = hyphenFormat.parse(crewcdrVo.getChkSearchStartDate());
            crewcdrVo.setChkSearchStartDate(nonFormat.format(chkSearchStartDate));
            Date chkSearchEndDate = hyphenFormat.parse(crewcdrVo.getChkSearchEndDate());
            crewcdrVo.setChkSearchEndDate(nonFormat.format(chkSearchEndDate));



            if (crewcdrVo.getChkStartDate()!= null && crewcdrVo.getChkStartDate().equals("true")){
                String finalChkSearchStartDate = crewcdrVo.getChkSearchStartDate() + crewcdrVo.getChkSearchStartHour()+"0000";
                String finalChkSearchEndDate = crewcdrVo.getChkSearchEndDate() + crewcdrVo.getChkSearchEndHour()+"0000";

                crewcdrVo.setFinalChkSearchStartDate(finalChkSearchStartDate);
                crewcdrVo.setFinalChkSearchEndDate(finalChkSearchEndDate);
            }

            String searchTableName = this.crewcdrService.selectTableName(crewcdrVo);
            crewcdrVo.setSearchTableName(searchTableName);
            totalCnt = 0;
            // 테이블 조회 성공시 list 조회
            if (searchTableName != null && !searchTableName.equals("(  )")){
                List<CrewcdrVo> crewcdrList = this.crewcdrService.selectCrewcdrList(crewcdrVo);
                model.addAttribute("crewcdrList", crewcdrList);

                // 페이징 처리를 위한 총 갯수
                totalCnt = this.crewcdrService.selectCrewcdrCount(crewcdrVo).size();

            }

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("201");
            userHistory.setJob_name("검색");
            userHistory.setGui_remark("Data Search");
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("");
            userHistoryService.insertUserHistory(userHistory);

        }

        //paging
        paging paging = new paging();
        String pagingHTML = paging.getPagingStr(crewcdrVo.getPageno(), crewcdrVo.getFetchrow() , totalCnt);
        model.addAttribute("pagingHTML", pagingHTML);

        //search data return
        model.addAttribute("crewcdrVo", crewcdrVo);
        model.addAttribute("totalCnt", totalCnt);

        return "crewcdr/crewcdr";
    }


    /**
     * 선주사별 선박명
     */
    @ResponseBody
    @PostMapping("/compList.ajax")
    public HashMap<String, Object> compListAjax(@RequestParam HashMap<String, Object> req, final HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            String comp_id = req.get("comp_id").toString();

            List<CrewcdrVo> shipNameList = crewcdrService.shipNameList(comp_id);

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
    @PostMapping("/crewIdList.ajax")
    public HashMap<String, Object> crewIdList(@RequestParam HashMap<String, Object> req, final HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            String s_code = req.get("s_code").toString();
            List<CrewcdrVo> crewIdList = crewcdrService.crewIdList(s_code);
            resultMap.put("crewIdList", crewIdList);
            resultMap.put("result", "success");
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }

        return resultMap;
    }


    /**
     *  Excel
     */
    @RequestMapping(value="crewcdrExcelDownload.do")
    public void crewcdrExcelDownload(HttpServletResponse response, final HttpSession session
    		, CrewcdrVo crewcdrVo, Locale locale) throws Exception {

        String sessionCompId = (String)session.getAttribute("comp_id");
        crewcdrVo.setComp_id(sessionCompId);

        try {

            SimpleDateFormat hyphenFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat nonFormat = new SimpleDateFormat("yyyyMMdd");

            Date startDate = hyphenFormat.parse(crewcdrVo.getSearchStartDate());
            crewcdrVo.setSearchStartDate(nonFormat.format(startDate));
            Date endDate = hyphenFormat.parse(crewcdrVo.getSearchEndDate());
            crewcdrVo.setSearchEndDate(nonFormat.format(endDate));

            Date chkSearchStartDate = hyphenFormat.parse(crewcdrVo.getChkSearchStartDate());
            crewcdrVo.setChkSearchStartDate(nonFormat.format(chkSearchStartDate));
            Date chkSearchEndDate = hyphenFormat.parse(crewcdrVo.getChkSearchEndDate());
            crewcdrVo.setChkSearchEndDate(nonFormat.format(chkSearchEndDate));

            crewcdrVo.setStartrow(-1);
            List<CrewcdrVo> excelList = this.crewcdrService.selectCrewcdrList(crewcdrVo);


            String[] l_aColumns = { 
            		"MAC", 
            		messageSource.getMessage("list.wapName", null, locale), 
            		messageSource.getMessage("select.shipOwner", null, locale), 
            		messageSource.getMessage("select.shipName", null, locale),
            		messageSource.getMessage("list.crewID", null, locale),
            		messageSource.getMessage("list.crewNickName", null, locale), 
            		messageSource.getMessage("list.useStartTime", null, locale), 
            		messageSource.getMessage("list.collectionTime", null, locale),
            		messageSource.getMessage("list.usageTime", null, locale), 
            		messageSource.getMessage("list.usage", null, locale),
            		messageSource.getMessage("list.cumulativeUsageToday", null, locale),
            		messageSource.getMessage("list.cumulativeMonthlyUsage", null, locale),
            		messageSource.getMessage("list.monthlyRemainingData", null, locale)
            		};

            String xlsName = messageSource.getMessage("crewcdr.crewUsageDetailedHistory", null, locale);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            String today = formatter.format(new java.util.Date());
            String fileName = xlsName + "_" + today + ".xlsx";

            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet(xlsName);
            CellStyle headerStyle = wb.createCellStyle();

            // 폰트 설정
            Font headerFont = wb.createFont();
            headerFont.setFontName("Verdana");
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 9); // 글씨 크기 12
            headerStyle.setFont(headerFont);

            // 정렬 설정
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // 배경색 설정
            XSSFColor headerBgColor = new XSSFColor(new java.awt.Color(223, 236, 240), new DefaultIndexedColorMap());
            ((XSSFCellStyle) headerStyle).setFillForegroundColor(headerBgColor);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 테두리 설정
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle dataStyle = wb.createCellStyle();


            Font dataFont = wb.createFont();
            dataFont.setFontName("Verdana");
            dataFont.setFontHeightInPoints((short) 10);
            dataStyle.setFont(dataFont);

            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            XSSFColor dataBgColor = new XSSFColor(new java.awt.Color(250, 250, 250), new DefaultIndexedColorMap());
            ((XSSFCellStyle) dataStyle).setFillForegroundColor(dataBgColor);
            dataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);


            //넘버링 스타일
            CellStyle numberStyle = wb.createCellStyle();
            numberStyle.cloneStyleFrom(dataStyle);
            DataFormat format = wb.createDataFormat();
            numberStyle.setDataFormat(format.getFormat("#,###"));


            int rowIdx = 0;
            int colIdx = 0;
            Row row = sheet.createRow(rowIdx);
            row.setHeightInPoints(20);

            for (int i = 0; i < l_aColumns.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(headerStyle);
                cell.setCellValue(l_aColumns[i]);
            }

            rowIdx++;
            logger.info(String.valueOf(excelList.size()));
            for (CrewcdrVo vo : excelList) {
                row = sheet.createRow(rowIdx);

                colIdx = 0;

                Cell cell;

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getMac());
                cell.setCellStyle(dataStyle);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getAp_name());
                cell.setCellStyle(dataStyle);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getComp_name());
                cell.setCellStyle(dataStyle);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getS_name());
                cell.setCellStyle(dataStyle);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getCrew_id());
                cell.setCellStyle(dataStyle);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getCrew_name());
                cell.setCellStyle(dataStyle);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getStart_time());
                cell.setCellStyle(dataStyle);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getEnd_time());
                cell.setCellStyle(dataStyle);

                //end time이 맞나?
                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getUse_time());
                cell.setCellStyle(dataStyle);

                //사용량맞는지 확인
                cell = row.createCell(colIdx++);
                double amtUse = Double.parseDouble(vo.getAmt_use());
                double roundedValue = Math.round(amtUse / 1000.0 * 100.0) / 100.0;
                String formattedValue = String.format("%.2f", roundedValue);
                cell.setCellValue(formattedValue);
                cell.setCellStyle(numberStyle);

                cell = row.createCell(colIdx++);
                double amtAccDay = Double.parseDouble(vo.getAmt_acc_day());
                int flooredValue = (int)Math.floor(amtAccDay / 1000.0);
                cell.setCellValue(flooredValue);
                cell.setCellStyle(numberStyle);

                cell = row.createCell(colIdx++);
                double amtAccMon = Double.parseDouble(vo.getAmt_acc_mon());
                flooredValue = (int)Math.floor(amtAccMon / 1000.0);
                cell.setCellValue(flooredValue);
                cell.setCellStyle(numberStyle);

                cell = row.createCell(colIdx++);
                double amtAccBal = Double.parseDouble(vo.getAmt_bal());
                flooredValue = (int)Math.floor(amtAccBal / 1000.0);
                cell.setCellValue(flooredValue);
                cell.setCellStyle(numberStyle);
                rowIdx++;

            }

            for (int i = 0; i < l_aColumns.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 1024);
            }

            String outputFileName = new String(fileName.getBytes("UTF-8"), "8859_1");
            response.setContentType("ms-vnd/excel; utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + outputFileName);
            ServletOutputStream servletOutputStream = response.getOutputStream();

            wb.write(servletOutputStream);
            wb.close();
            servletOutputStream.flush();
            servletOutputStream.close();


            //  로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("201");
            userHistory.setJob_name("엑셀");
            userHistory.setGui_remark("사유 : " + crewcdrVo.getExcel_msg());
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            String sql_str = " select A.MAC as \"MAC\", A.AP_NAME as \"WAP 관리명\", B.COMP_NAME as '선주사', B.S_NAME as '선박명', A.CREW_ID as \"선원ID\", C.CREW_NAME as '선원 별칭', DATE_FORMAT(A.START_TIME, '%Y-%m-%d %H:%i:%s') as \"사용 시작 시간\", DATE_FORMAT(A.END_TIME, '%Y-%m-%d %H:%i:%s') \"수집시간\",(CASE WHEN A.USE_TIME <60 THEN TIME_FORMAT(SEC_TO_TIME(A.USE_TIME), '%s sec') WHEN A.USE_TIME <3600 THEN TIME_FORMAT(SEC_TO_TIME(A.USE_TIME), '%i min %s sec') ELSE TIME_FORMAT(SEC_TO_TIME(A.USE_TIME), '%H hour %i min %s sec') END) \"사용시간\",format(round(A.AMT_USE/1000, 2),2) as \\\"NN_사용량(MB)\\\",  format(floor(A.AMT_ACC_DAY/1000),0) as \\\"NN_금일 누적 사용량(MB)\\\", format(floor(A.AMT_ACC_MON/1000),0) as \\\"NN_금월 누적 사용량(MB)\\\", format(floor(A.AMT_BAL/1000),0) as \\\"NN_금월 잔여 데이터량(MB)\\\"from \" +crewcdrVo.getSearchTableName() + \" A, ( select S.S_CODE, S.S_NAME, C.COMP_NAME, C.COMP_ID from TB_SHIP_INFO S, TB_SCOMP_INFO C where S.COMP_ID=C.COMP_ID ) B, TB_CREW_INFO C where A.S_CODE=B.S_CODE and A.CREW_ID=C.CREW_ID and (" + crewcdrVo.getFinalSearchStartDate()+"<=DATE_FORMAT(A.END_TIME,'%Y%m%d%H%i%s') and DATE_FORMAT(A.END_TIME,'%Y%m%d%H%i%s')<='"+ crewcdrVo.getFinalSearchEndDate()+"')and ";
            if (crewcdrVo.getChkStartDate() != null && crewcdrVo.getChkStartDate().equals("true")){
                sql_str += "('"+ crewcdrVo.getFinalChkSearchStartDate()+"'<=DATE_FORMAT(A.START_TIME,'%Y%m%d%H%i%s') and DATE_FORMAT(A.START_TIME,'%Y%m%d%H%i%s')<='"+crewcdrVo.getFinalChkSearchEndDate()+"')and " ;
            }
            sql_str += " B.COMP_ID='"+crewcdrVo.getSearchCompId();
            if (crewcdrVo.getSearchSCode() != null && !crewcdrVo.getSearchSCode().equals("select")){
                sql_str += "and B.S_CODE="+crewcdrVo.getSearchSCode();
            }
            sql_str += " order by A.END_TIME desc";
            userHistory.setSql_str(sql_str);

            userHistoryService.insertUserHistory(userHistory);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
