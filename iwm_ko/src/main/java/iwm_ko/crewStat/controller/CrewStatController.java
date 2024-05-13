package iwm_ko.crewStat.controller;

import java.text.SimpleDateFormat;
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
import iwm_ko.crewStat.model.CrewStatVo;
import iwm_ko.crewStat.service.CrewStatService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/crewstat")
public class CrewStatController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "iwm_ko.crewStat.CrewStatService")
    private CrewStatService crewStatService;
    
    @Autowired
    private UserHistoryService userHistoryService;
    
    @Autowired
    private MessageSource messageSource;

//    private String gui_code;
//
//    @PostConstruct
//    public void init() throws Exception {
//        gui_code = userHistoryService.searchGuiCode("crewstat");
//    }

    /**
     * 페이지 로딩
     */
    @RequestMapping("crewstat.do")
    public String crewStat(@ModelAttribute("crewStatVo") CrewStatVo crewStatVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {


        crewStatVo.setStartrow((crewStatVo.getPageno() - 1) * crewStatVo.getFetchrow());
        String sessionCompId = (String)session.getAttribute("comp_id");
        crewStatVo.setSessionCompId(sessionCompId);

        model.addAttribute("sessionCompId",sessionCompId);

        List<CrewStatVo> compList = this.crewStatService.selectCompList(crewStatVo);
        model.addAttribute("compList", compList);

        List<CrewStatVo> shipList = this.crewStatService.shipNameList(crewStatVo.getSessionCompId());
        model.addAttribute("shipList",shipList);

        int totalCnt = -1;

        //페이지 첫 접속시 기본 검색 조건
        if(crewStatVo.getCs_ref_ship() == null && crewStatVo.getCs_ref_comp() == null && crewStatVo.getCs_ref_crew() == null){

            crewStatVo.setCs_ref_ship("true");
            crewStatVo.setCs_ref_comp("true");
            crewStatVo.setCs_ref_crew("true");
            crewStatVo.setCs_ref_crewName("true");
        }

        String searchTableName = this.crewStatService.selectTableName(crewStatVo);
        crewStatVo.setSearchTableName(searchTableName);


        if (searchTableName != null || !searchTableName.equals(" ")) {

            //페이지 첫 접속시 기본 검색 조건
            if (crewStatVo.getCs_unit() == null){
                crewStatVo.setCs_unit("hour");
            }
            // list검색
            List<CrewStatVo> crewStatList = this.crewStatService.selectCrewStatList(crewStatVo);
            model.addAttribute("crewStatList", crewStatList);
            // 페이징 처리를 위한 총 갯수
            totalCnt = this.crewStatService.selectCrewStatCount(crewStatVo).size();
        }

        if (!(crewStatVo.getSearchCompId() == null || crewStatVo.getSearchCompId().equals("")
                && (crewStatVo.getCs_unit() == null || crewStatVo.getCs_unit().equals(""))
                && (crewStatVo.getSearchSCode() == null || crewStatVo.getSearchSCode().equals(""))
        )) {
            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("204");
            userHistory.setJob_name("검색");
            userHistory.setGui_remark("Data Search");
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("");
            userHistoryService.insertUserHistory(userHistory);
        }

        //paging
        paging paging = new paging();
        String pagingHTML = paging.getPagingStr(crewStatVo.getPageno(), crewStatVo.getFetchrow(), totalCnt);
        model.addAttribute("pagingHTML", pagingHTML);

        //search data return
        model.addAttribute("crewStatVo", crewStatVo);
        model.addAttribute("totalCnt", totalCnt);

        // 초기 접속시 crewList 불러오기
        // 로딩 시간이 길어져서 주석처리
//        if (crewStatVo.getSearchSCode() == null || crewStatVo.getSearchSCode().equals("select") || crewStatVo.getSearchSCode() == "") {
//            crewStatVo.setCs_code("all");
//            List<CrewStatVo> crewNameList = this.crewStatService.crewSelectList(crewStatVo);
//            model.addAttribute("crewNameList", crewNameList);
//        }

        return "crewstat/crewstat";
    }


    /**
     *  선박별 크루 정보
     */
    @ResponseBody
    @PostMapping("/crewSelectList.ajax")
    public HashMap<String, Object> crewSelectList(@RequestParam HashMap<String, Object> req, CrewStatVo crewStatVo, final HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();


        try {
            crewStatVo.setS_code(req.get("s_code").toString());
            crewStatVo.setSessionCompId((String)session.getAttribute("comp_id") );
            crewStatVo.setCs_code("not all");
            List<CrewStatVo> crewSelectList = crewStatService.crewSelectList(crewStatVo);
            resultMap.put("crewSelectList", crewSelectList);
            resultMap.put("result", "success");
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }

        return resultMap;
    }

    /**
     * 선주사별 선박명
     */
    @ResponseBody
    @PostMapping("/compList.ajax")
    public HashMap<String, Object> compListAjax(@RequestParam HashMap<String, Object> req,CrewStatVo crewStatVo, final HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            String comp_id = req.get("comp_id").toString();

            List<CrewStatVo> shipNameList = crewStatService.shipNameList(comp_id);
            crewStatVo.setSessionCompId(comp_id);
            crewStatVo.setCs_code("all");
            List<CrewStatVo> crewNameList = this.crewStatService.crewSelectList(crewStatVo);
           resultMap.put("crewNameList",crewNameList);


            resultMap.put("shipNameList", shipNameList);
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
    @RequestMapping(value="crewStatExcelDownload.do")
    public void crewStatExcelDownload(HttpServletResponse response, final HttpSession session
    		, CrewStatVo crewStatVo, Locale locale) throws Exception {

        try {


            crewStatVo.setSessionCompId((String)session.getAttribute("comp_id"));
            crewStatVo.setStartrow(-1);


            String searchTableName = this.crewStatService.selectTableName(crewStatVo);
            crewStatVo.setSearchTableName(searchTableName);

            List<CrewStatVo> excelList = this.crewStatService.selectCrewStatList(crewStatVo);


            String[] l_aColumns = { 
            		messageSource.getMessage("list.creationTime", null, locale), 
            		messageSource.getMessage("select.shipOwner", null, locale), 
            		messageSource.getMessage("select.shipName", null, locale),
            		messageSource.getMessage("list.crewID", null, locale),
            		messageSource.getMessage("list.crewNickName", null, locale), 
            		messageSource.getMessage("list.collectionNumber", null, locale),
            		messageSource.getMessage("list.usage", null, locale),
            		messageSource.getMessage("list.averageUsage", null, locale)
            		};

            String xlsName = messageSource.getMessage("crewstat.crewUsageHistory", null, locale);
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

            for (CrewStatVo vo : excelList) {
                row = sheet.createRow(rowIdx);

                colIdx = 0;

                Cell cell;

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getMon_time());
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
                cell.setCellValue(vo.getVal1());
                cell.setCellStyle(numberStyle);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getVal2());
                cell.setCellStyle(numberStyle);


                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getSumval());
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
            userHistory.setGui_code("204");
            userHistory.setJob_name("엑셀");
            userHistory.setGui_remark("사유 : " + crewStatVo.getExcel_msg());
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            String sql = " SELECT";

            if(crewStatVo.getCs_unit().equals("hour")){
                sql +="DATE_FORMAT(STR_TO_DATE(MON_TIME, '%Y%m%d%H%i%s'), '%Y-%m-%d %H:%i:%s') '생성 시간' ,";
            } else if (crewStatVo.getCs_unit().equals("day") || crewStatVo.getCs_unit().equals("week")) {
                sql += "DATE_FORMAT(STR_TO_DATE(MON_TIME, '%Y%m%d%H%i%s'), '%Y-%m-%d') '생성 시간',";
            }else if(crewStatVo.getCs_unit().equals("month")){
                sql +="DATE_FORMAT(STR_TO_DATE(MON_TIME, '%Y%m%d%H%i%s'), '%Y-%m') '생성 시간',";
            }

            if (crewStatVo.getCs_ref_comp() != null && crewStatVo.getCs_ref_comp().equals("true")){
                if (crewStatVo.getSearchCompId() != null && !crewStatVo.getSearchCompId().equals("select") && !crewStatVo.getSearchCompId().equals(" "));
                sql += "COMP_NAME as '선주사'";
            }
            if (crewStatVo.getCs_ref_ship() != null && crewStatVo.getCs_ref_ship().equals("true")){
                sql += " S_NAME as '선박명',";
            }
            if (crewStatVo.getCs_ref_crew() != null && crewStatVo.getCs_ref_crew().equals("true")){
                sql += "CREW_ID AS '선원ID' CREW_NAME AS '선원 별칭'";
            }
            sql += " CONVERT(SUM(VAL1), INT) AS 'NN_수집건수', format(CONVERT(SUM(VAL2), INT),0) AS 'NN_사용량(MB)', format(CONVERT((CASE WHEN SUM(VAL1) > 0 THEN FLOOR(SUM(VAL2)/ SUM(VAL1)) ELSE 0 END), INT),0) 'NN_평균 사용량(MB)'\n" +
                    " FROM\n" +
                    " (\n" +
                    " SELECT A.MON_TIME, C.COMP_ID, C.COMP_NAME, S.S_CODE, S.S_NAME, A.VAL1, A.VAL2, A.VAL3\n" +
                    " FROM\n " +
                    crewStatVo.getSearchTableName() + " A " +
                    " TB_CREW_INFO S, TB_SHIP_INFO SI, TB_SCOMP_INFO C" +
                    "WHERE A.KEY2 = S.CREW_ID" +
                    " AND S.S_CODE=SI.S_CODE" +
                    " AND SI.COMP_ID=C.COMP_ID" +
                    " AND A.MON_TIME BETWEEN " +crewStatVo.getSearchStartDate()+ " AND " +crewStatVo.getSearchEndDate()+" ";
            if(crewStatVo.getCs_ref_comp().equals("true")){
                if (!crewStatVo.getCs_ref_comp().equals("select")){
                    sql += "AND C.COMP_ID=" + crewStatVo.getSearchCompId();
                }
            }

            if (crewStatVo.getCs_ref_ship().equals("true")){
                if (!crewStatVo.getCs_ref_ship().equals("select")){
                    sql += "AND S.S_CODE = "+ crewStatVo.getSearchSCode();
                }
            }
            if (crewStatVo.getCs_ref_crew().equals("true")){
                if (!crewStatVo.getCs_ref_crew().equals("select")){
                    sql += "AND A.KEY2 = " + crewStatVo.getSearchCrew();
                }
            }

            sql += " ) U GROUP BY";

            if(crewStatVo.getCs_unit().equals("hour")){
                sql +="DATE_FORMAT(STR_TO_DATE(MON_TIME, '%Y%m%d%H%i%s'), '%Y-%m-%d %H:%i:%s') ,";
            } else if (crewStatVo.getCs_unit().equals("day") || crewStatVo.getCs_unit().equals("week")) {
                sql += "DATE_FORMAT(STR_TO_DATE(MON_TIME, '%Y%m%d%H%i%s'), '%Y-%m-%d'),";
            }else if(crewStatVo.getCs_unit().equals("month")){
                sql +="DATE_FORMAT(STR_TO_DATE(MON_TIME, '%Y%m%d%H%i%s'), '%Y-%m'),";
            }

            if (crewStatVo.getCs_ref_comp().equals("true")){
                sql += ", COMP_NAME";
            }
            if (crewStatVo.getCs_ref_ship().equals("true")){
                sql+=", S_NAME";
            }
            if (crewStatVo.getCs_ref_crew().equals("true")){
                sql += ", CREW_ID";
            }

            sql += " ORDER BY MON_TIME desc, COMP_NAME, S_NAME, CREW_NAME ASC";

            userHistory.setSql_str(sql);

            userHistoryService.insertUserHistory(userHistory);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

