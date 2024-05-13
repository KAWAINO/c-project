package iwm_ko.conf.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import iwm_ko.comm.paging;
import iwm_ko.conf.model.ConfVo;
import iwm_ko.conf.service.ConfService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/conf")
public class ConfController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private MessageSource messageSource;

    @Resource(name = "iwm_ko.conf.ConfService")
    private ConfService confService;
    @Autowired
    private UserHistoryService userHistoryService;

//    private String gui_code;
//
//    @PostConstruct
//    public void init() throws Exception {
//        gui_code = userHistoryService.searchGuiCode("conf");
//    }

    /**
     * 페이지 로딩
     */
    @RequestMapping("conf.do")
    public String crewInfo(@ModelAttribute("confVo") ConfVo confVo, HttpServletRequest request, final HttpSession session, Model model)throws Exception{

        confVo.setFetchrow(50);
        confVo.setStartrow((confVo.getPageno() - 1) * confVo.getFetchrow());

        String sessionCompId = (String)session.getAttribute("comp_id");
        confVo.setComp_id(sessionCompId);
        model.addAttribute("sessionCompId",sessionCompId);


        if (sessionCompId.equals("0")) {

            List<ConfVo> confList = this.confService.selectConfList(confVo);
            model.addAttribute("confList", confList);

            // 페이징 처리를 위한 총 갯수
            int totalCnt = this.confService.selectConfCount(confVo).size();


            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("708");
            userHistory.setJob_name("검색");
            userHistory.setGui_remark("Data Search");
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("");
            userHistoryService.insertUserHistory(userHistory);

            //paging
            paging paging = new paging();
            String pagingHTML = paging.getPagingStr(confVo.getPageno(), confVo.getFetchrow(), totalCnt);
            model.addAttribute("pagingHTML", pagingHTML);
            model.addAttribute("totalCnt", totalCnt);

            //search data return
            model.addAttribute("confVo", confVo);
        }

        return "conf/conf";
    }


    /**
     * 등록
     */
    @ResponseBody
    @RequestMapping(value="confAdd.ajax", method= RequestMethod.POST)
    public HashMap<String, Object> crewInfoAdd(ConfVo confVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            int insResult = 0;
            //중복검사
            int chkConf = this.confService.chkConf(confVo).size();
            if(chkConf > 0) {
                resultMap.put("result", "-2");
            }else {
                insResult = this.confService.insertConf(confVo);

                //로그 기록
                UserHistoryVo userHistory = new UserHistoryVo();
                userHistory.setGui_code("708");
                userHistory.setJob_name("추가");
                userHistory.setGui_remark("Configuration Name : "+confVo.getAdd_conf()+", Value : "+confVo.getAdd_val()+", Description : "+confVo.getAdd_descr());
                userHistory.setUser_id((String) session.getAttribute("user_id"));
                userHistory.setSql_str("insert into TB_CONF_VAL (CONF_NAME,VAL,DESCR,MODIFY_DATE)" +
                 " values (" +confVo.getAdd_conf()+" , "+ confVo.getAdd_val()+" , "+confVo.getAdd_descr()+" ,NOW()) ");
                userHistoryService.insertUserHistory(userHistory);

                resultMap.put("result", "1");
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
    @RequestMapping(value = "confSetUpdateData.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> confSetUpdateData(@RequestParam HashMap<String, Object> req, final HttpSession session) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            ConfVo confVo = this.confService.selectConfInfo(req);

            resultMap.put("confVo", confVo);
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
    @RequestMapping(value="confUpdate.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> confUpdate(ConfVo confVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();


        try {
            //update
            int updResult = this.confService.updateConf(confVo);

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("708");
            userHistory.setJob_name("수정");
            userHistory.setGui_remark( "Configuration Name : "+confVo.getU_upd_conf()+", Value : "+confVo.getUpd_val()+", Description : "+confVo.getUpd_descr());
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("update TB_CONF_VAL set VAL=" +confVo.getUpd_val() +", DESCR= " + confVo.getUpd_descr()+" , MODIFY_DATE=NOW(), GUI_FLAG='Y' where CONF_NAME= " + confVo.getU_upd_conf());

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
    @RequestMapping(value="confDelete.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> confDelete(ConfVo confVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            //update

            int updResult = this.confService.deleteConf(confVo);

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("708");
            userHistory.setJob_name("삭제");
            userHistory.setGui_remark("Configuration Name : " + confVo.getU_upd_conf()    );
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("delete from TB_CONF_VAL where CONF_NAME= " + confVo.getU_upd_conf());

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
    @RequestMapping(value="confExcelDownload.do")
    public void confExcelDownload(HttpServletResponse response, final HttpSession session, ConfVo confVo, Locale locale) throws Exception {

        try {
            confVo.setFetchrow(50);
            confVo.setStartrow(-1);

            confVo.setComp_id((String) session.getAttribute("comp_id"));
            List<ConfVo> excelList = this.confService.selectConfList(confVo);


            String[] l_aColumns = {
            		messageSource.getMessage("list.configurationName", null, locale), 
            		messageSource.getMessage("list.value", null, locale), 
            		messageSource.getMessage("list.description", null, locale)
            		};

            String xlsName = messageSource.getMessage("conf.configurationManagement", null, locale);
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

            for (ConfVo vo : excelList) {
                row = sheet.createRow(rowIdx);

                colIdx = 0;

                Cell cell;

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getConf_name());
                cell.setCellStyle(dataStyle);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getVal());
                cell.setCellStyle(dataStyle);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getDescr());
                cell.setCellStyle(dataStyle);
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
            userHistory.setGui_code("708");
            userHistory.setJob_name("엑셀");
            userHistory.setGui_remark("사유 : " + confVo.getExcel_msg());
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            String sql = "select CONF_NAME AS conf_name, VAL AS val, DESCR AS descr from TB_CONF_VAL";
            if(confVo.getSearchConf() != null && confVo.getSearchConf() != ""){
                sql += "where CONF_NAME LIKE CONCAT('%'," + confVo.getSearchConf()+",'%')";
            }
            userHistory.setSql_str(sql);

            userHistoryService.insertUserHistory(userHistory);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
