package iwm_ko.wapStat.controller;

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
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import iwm_ko.wapStat.model.WapStatVo;
import iwm_ko.wapStat.service.WapStatService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/wapstat")
public class WapStatController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "iwm_ko.wapStat.WapStatService")
    private WapStatService wapStatService;
    
    @Autowired
    private UserHistoryService userHistoryService;
    
    @Autowired
    private MessageSource messageSource;

//    private String gui_code;
//
//    @PostConstruct
//    public void init() throws Exception {
//        gui_code = userHistoryService.searchGuiCode("wapstat");
//    }

    /**
     * 페이지 로딩
     */
    @RequestMapping("wapstat.do")
    public String wapStat(@ModelAttribute("wapStatVo") WapStatVo wapStatVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {

        wapStatVo.setStartrow((wapStatVo.getPageno() - 1) * wapStatVo.getFetchrow());
        String sessionCompId = (String)session.getAttribute("comp_id");
        wapStatVo.setSessionCompId(sessionCompId);
                if (!sessionCompId.equals("0")){
                    wapStatVo.setSearchCompId(sessionCompId);
                }
        model.addAttribute("sessionCompId",sessionCompId);

        List<WapStatVo> compList = this.wapStatService.selectCompList(wapStatVo);
        model.addAttribute("compList", compList);
        List<WapStatVo> shipList = this.wapStatService.shipNameList(wapStatVo.getSessionCompId());
        model.addAttribute("shipList",shipList);

        int totalCnt = -1;

        if(wapStatVo.getCs_ref_ship() == null && wapStatVo.getCs_ref_comp() == null && wapStatVo.getCs_ref_ap() == null){
            wapStatVo.setCs_ref_ship("true");
            wapStatVo.setCs_ref_comp("true");
            wapStatVo.setCs_ref_ap("true");
        }

        String searchTableName = this.wapStatService.selectTableName(wapStatVo);
        wapStatVo.setSearchTableName(searchTableName);


        if (searchTableName != null || !searchTableName.equals(" ")) {

            //페이지 첫 접속시 기본 검색 조건
            if (wapStatVo.getCs_unit() == null){
                wapStatVo.setCs_unit("hour");
            }

            // list검색
            List<WapStatVo> wapStatList = this.wapStatService.selectWapStatList(wapStatVo);
            model.addAttribute("wapStatList", wapStatList);

            // 페이징 처리를 위한 총 갯수
            totalCnt = this.wapStatService.selectWapStatCount(wapStatVo).size();
        }

        if (!(wapStatVo.getSearchCompId() == null || wapStatVo.getSearchCompId().equals("")
                && (wapStatVo.getCs_unit() == null || wapStatVo.getCs_unit().equals(""))
                && (wapStatVo.getSearchSCode() == null || wapStatVo.getSearchSCode().equals(""))
        )) {

            String selectedWap = (String) session.getAttribute("selectedWap");
            model.addAttribute("selectedWap", selectedWap);

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("303");
            userHistory.setJob_name("검색");
            userHistory.setGui_remark("Data Search");
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("");
            userHistoryService.insertUserHistory(userHistory);
        }

        //paging
        paging paging = new paging();
        String pagingHTML = paging.getPagingStr(wapStatVo.getPageno(), wapStatVo.getFetchrow(), totalCnt);
        model.addAttribute("pagingHTML", pagingHTML);

        //search data return
        model.addAttribute("wapStatVo", wapStatVo);
        model.addAttribute("totalCnt", totalCnt);

        if (wapStatVo.getSearchSCode() == null || wapStatVo.getSearchSCode().equals("select")){

            wapStatVo.setCs_code("all");
            List<WapStatVo> wapList = this.wapStatService.wapSelectList(wapStatVo);
            model.addAttribute("wapList",wapList);
        }
        return "wapstat/wapstat";
    }

    /**
     *  선박별 정책명
     */
    @ResponseBody
    @PostMapping("/wapSelectList.ajax")
    public HashMap<String, Object> wapSelectList(@RequestParam HashMap<String, Object> req,WapStatVo wapStatVo, final HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            wapStatVo.setSessionCompId((String)session.getAttribute("comp_id"));
            wapStatVo.setS_code(req.get("s_code").toString());
            wapStatVo.setCs_code("not all");
            List<WapStatVo> wapSelectList = wapStatService.wapSelectList(wapStatVo);    
            resultMap.put("wapSelectList", wapSelectList);
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
    @RequestMapping(value="wapStatExcelDownload.do")
    public void wapStatExcelDownload(HttpServletResponse response, final HttpSession session
    		, WapStatVo wapStatVo, Locale locale) throws Exception {

        try {

            wapStatVo.setSessionCompId((String)session.getAttribute("comp_id"));
            wapStatVo.setStartrow(-1);


            String searchTableName = this.wapStatService.selectTableName(wapStatVo);
            wapStatVo.setSearchTableName(searchTableName);

            List<WapStatVo> excelList = this.wapStatService.selectWapStatList(wapStatVo);


            String[] l_aColumns = { 
            		messageSource.getMessage("list.creationTime", null, locale), 
            		messageSource.getMessage("select.shipOwner", null, locale), 
            		messageSource.getMessage("select.shipName", null, locale),
            		messageSource.getMessage("list.wapName", null, locale), 
            		messageSource.getMessage("list.collectionNumber", null, locale),
            		messageSource.getMessage("list.usage", null, locale),
            		messageSource.getMessage("list.averageUsage", null, locale)
            		};

            String xlsName = "WAP별사용내역 ";
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

            for (WapStatVo vo : excelList) {
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
                cell.setCellValue(vo.getAp_name());
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
                sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 1200);
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
            userHistory.setGui_code("303");
            userHistory.setJob_name("엑셀");
            userHistory.setGui_remark("사유 : " + wapStatVo.getExcel_msg());
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str(" SELECT\n" +
                    " DATE_FORMAT(STR_TO_DATE(MON_TIME, '%Y%m%d%H%i%s'), '%Y-%m-%d %H:%i:%s') '생성 시간',\n" +
                    " COMP_NAME as '선주사',\n" +
                    " S_NAME as '선박명',\n" +
                    "AP_NAME as 'WAP 관리명'" +
                    " CONVERT(SUM(VAL1), INT) AS 'NN_수집건수', format(CONVERT(SUM(VAL2), INT),0) AS 'NN_사용량(MB)', format(CONVERT((CASE WHEN SUM(VAL1) > 0 THEN FLOOR(SUM(VAL2)/ SUM(VAL1)) ELSE 0 END), INT),0) 'NN_평균 사용량(MB)'\n" +
                    " FROM\n" +
                    " (\n" +
                    " SELECT A.MON_TIME, C.COMP_ID, C.COMP_NAME, S.S_CODE, S.S_NAME, A.VAL1, A.VAL2, A.VAL3\n" +
                    " FROM\n " +
                    wapStatVo.getSearchTableName() + " A " +
                    " TB_SHIP_INFO S,\n" +
                    " TB_SCOMP_INFO C\n" +
                    " WHERE A.KEY1 = S.S_CODE\n" +
                    " AND S.COMP_ID = C.COMP_ID\n" +
                    " AND A.MON_TIME BETWEEN " +wapStatVo.getSearchStartDate()+ " AND " +wapStatVo.getSearchEndDate()+" "+
                    " ) U GROUP BY DATE_FORMAT(STR_TO_DATE(MON_TIME, '%Y%m%d%H%i%s'), '%Y-%m-%d %H:%i:%s')\n" +
                    ", COMP_NAME , S_NAME ORDER BY MON_TIME desc, COMP_ID ASC, S_CODE ASC");

            userHistoryService.insertUserHistory(userHistory);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * 선주사별 선박명
     */
    @ResponseBody
    @PostMapping("/compList.ajax")
    public HashMap<String, Object> compListAjax(@RequestParam HashMap<String, Object> req,WapStatVo wapStatVo, final HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            String comp_id = req.get("comp_id").toString();
            wapStatVo.setSessionCompId(comp_id);
            wapStatVo.setCs_code("all");
            List<WapStatVo> shipNameList = wapStatService.shipNameList(comp_id);
            List<WapStatVo> wapSelectList = this.wapStatService.wapSelectList(wapStatVo);


            resultMap.put("shipNameList", shipNameList);
            resultMap.put("wapSelectList", wapSelectList);
            resultMap.put("result", "success");
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }

        return resultMap;
    }




}
