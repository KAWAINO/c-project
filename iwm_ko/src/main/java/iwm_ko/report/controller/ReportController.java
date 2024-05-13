package iwm_ko.report.controller;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import iwm_ko.report.model.ReportVo;
import iwm_ko.report.service.ReportService;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Base64;

@Controller
@RequestMapping("/report")
public class ReportController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DataSource dataSource;

    @Autowired
    private ServletContext servletContext;

    public ReportController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Resource(name = "iwm_ko.report.ReportService")
    private ReportService reportService;
    
	@Resource(name="iwm_ko.userHistory.userHistoryService")
    private UserHistoryService userHistoryService;

    /**
     * 페이지 로딩
     */
    @RequestMapping("report.do")
    public String report(@ModelAttribute("reportVo") ReportVo reportVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {


        String sessionCompId = (String)session.getAttribute("comp_id");
        reportVo.setSessionCompId(sessionCompId);

        model.addAttribute("sessionCompId",sessionCompId);

        List<ReportVo> compList = this.reportService.selectCompList(reportVo);
        model.addAttribute("compList", compList);
        
        List<ReportVo> shipList = this.reportService.selectShipList(reportVo);
        model.addAttribute("shipList",shipList);

        List<Integer> years = new ArrayList<>();
        for (int i = LocalDate.now().getYear(); i >= 2017; i--) {
            years.add(i);
        }
        model.addAttribute("years", years);

        //search data return
        model.addAttribute("reportVo", reportVo);

        return "report/report";
    }

    /**
     * 선주사별 선박명
     */
    @ResponseBody
    @PostMapping("/SNameList.ajax")
    public HashMap<String, Object> SNameList(@RequestParam HashMap<String, Object> req, final HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            String comp_id = req.get("comp_id").toString();
            if (comp_id.equals("")){
                return  resultMap;
            }

            List<ReportVo> shipNameList = reportService.shipNameList(comp_id);

            resultMap.put("shipNameList", shipNameList);
            resultMap.put("result", "success");
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }

        return resultMap;
    }

    /**
     *
     */
    @ResponseBody
    @RequestMapping(value = "searchReport.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> searchReport(ReportVo reportVo, @RequestParam HashMap<String, Object> req
    		, final HttpSession session, HttpServletRequest request) throws Exception{
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            reportVo.setUser_id((String)session.getAttribute("user_id"));
            reportVo.setComp_id((String)session.getAttribute("comp_id"));
            reportVo.setStime(reportVo.getCs_year() + reportVo.getCs_month() + "01");
            reportVo.setEtime(reportVo.getCs_year() + reportVo.getCs_month() + "31");

            String chkTable = this.reportService.chkTable(reportVo);
            if (chkTable != null && chkTable.length() <= 10) {
                resultMap.put("result","-2");
                return resultMap;
            } else {
                reportVo.setTableName(chkTable);
                reportVo.setRedirectUrl("/report/saveImage/makeChart");
                reportVo.setPath(request.getSession().getServletContext().getRealPath("/"));
                reportVo.setViewer("web");
                reportVo.setResult("1");
                reportVo.setStartDate(reportVo.getCs_year() + reportVo.getCs_month() + "01");
                reportVo.setEndDate(reportVo.getCs_year() + reportVo.getCs_month() + "31");
                List<ReportVo> compList = this.reportService.getDataList(reportVo);

                resultMap.put("reportVo", reportVo);
                resultMap.put("compList", compList);

                resultMap.put("result", "1");
            }
            return resultMap;
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }
        return resultMap;
    }

    /**
     * 페이지 로딩
     */
    @RequestMapping("save_image.do")
    public String save_image(@ModelAttribute("reportVo") ReportVo reportVo, HttpServletRequest request
    		, final HttpSession session, Model model) throws Exception {

        reportVo.setComp_id((String)session.getAttribute("comp_id") );
        reportVo.setImgData(reportVo.getImgData().replaceAll("data:image/png;base64,", ""));
        reportVo.setImg_path(request.getSession().getServletContext().getRealPath("web/report/saveImage/chartImage"));

        // reportVo 객체를 세션에 저장
        session.setAttribute("reportData", reportVo);

        if ("pdf".equals(reportVo.getViewer())) {
            return "redirect:/report/report_pdf";
        } else if ("web".equals(reportVo.getViewer())) {
            return "redirect:/report/report_web";
        }
        return "report/report";
    }

    @RequestMapping(value = "/report_web", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> report_web(HttpSession session, HttpServletResponse response) throws Exception {
        Map<String, Object> responseUrl = new HashMap<>();
        ReportVo reportVo = (ReportVo) session.getAttribute("reportData");
        reportVo.setUser_id((String) session.getAttribute("user_id"));

        // 파일 이름 설정
        String fileSaved = String.format("선원별_월간_사용내역_보고서_%s.pdf", 
                new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()));

        // 캐시 설정 해제
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Pragma", "no-cache");
        response.addDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");

        // 템플릿 파일 경로 설정
        String temPath = reportVo.getPath() + "web/report/template/report_crew_month.jrxml";
        
        // PDF 파일 경로 설정
        String pdfPath = reportVo.getPath() + "web/report/viewer/pdf_view/report_crew_month_" + reportVo.getUser_id() + ".pdf";
        String saveDir = servletContext.getRealPath("/web/report/saveImage/chartImage");
        String userIdPng = "report_"+reportVo.getUser_id()+".png";

        File file =  binaryToFile(reportVo.getImgData(), saveDir,userIdPng);

        // JasperReports를 사용하여 PDF 리포트 생성
        try {

            reportVo.setCs_year(reportVo.getS_year());
            reportVo.setCs_month(reportVo.getS_month());
            reportVo.setStime(reportVo.getCs_year() + reportVo.getCs_month() + "01");
            reportVo.setEtime(reportVo.getCs_year() + reportVo.getCs_month() + "31");
            reportVo.setSearchCompId(reportVo.getCs_comp());
            reportVo.setSearchSCode(reportVo.getCs_ship());

            String chkTable = this.reportService.chkTable(reportVo);

            // pre_month 2자릿수 고정 & cs_month 01월일 경우 전년도 12월로 변경
            reportVo.setPre_month(String.format("%02d",Integer.parseInt(reportVo.getCs_month())-1));

            String preYear = "";
            String preStime = "";
            
            if (reportVo.getPre_month().equals("00")){
                preYear = String.valueOf(Integer.parseInt(reportVo.getCs_year())-1);
                reportVo.setPre_month("12");
                preStime = preYear+reportVo.getPre_month()+"01";

            }else {
                preYear= reportVo.getCs_year();
                preStime = reportVo.getCs_year()+reportVo.getPre_month()+"01";
            }
            reportVo.setPreSTime(preStime);
            reportVo.setPreETime(reportVo.getCs_year() + reportVo.getCs_month() + "31");
            reportVo.setS_name(this.reportService.getShipName(reportVo));
            String preTable = this.reportService.preTable(reportVo);

            // 파라미터 설정
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("YEAR", reportVo.getCs_year());
            paramMap.put("PRE_YEAR", preYear);
            paramMap.put("MONTH", reportVo.getCs_month());
            paramMap.put("PRE_MONTH", reportVo.getPre_month());
            paramMap.put("SHIPCODE", reportVo.getSearchSCode());
            paramMap.put("SHIPNAME", reportVo.getS_name());
            paramMap.put("TABLE", chkTable);
            paramMap.put("PRE_TABLE", preTable);
            paramMap.put("USERID", reportVo.getUser_id());
            paramMap.put("IMGPATH", reportVo.getImg_path());
            paramMap.put("STIME", reportVo.getStime());
            paramMap.put("PRE_STIME", reportVo.getPreSTime());
            paramMap.put("ETIME", reportVo.getEtime());
            paramMap.put("PRE_ETIME", reportVo.getPreETime());

            try {
                JasperReport jasperReport = JasperCompileManager.compileReport(temPath);

                try (Connection conn = dataSource.getConnection()) {
                    JasperPrint print = JasperFillManager.fillReport(jasperReport, paramMap, conn);
                    JasperExportManager.exportReportToPdfFile(print, pdfPath);

                    if (!response.isCommitted()) {
                        String pdfUrl = "/web/report/viewer/web/viewer.html?file=../pdf_view/report_crew_month_" 
                        		+ reportVo.getUser_id() + ".pdf";
                        responseUrl.put("pdfUrl", pdfUrl);
                        responseUrl.put("result", "1");
                    }
                }
            } catch (Exception e) {
                logger.error("Error occurred while filling the report", e);
                responseUrl.put("error", e.getMessage());
            }
        } catch (JRException e) {
            // JasperReports 예외 처리
            e.printStackTrace();
        }
        return ResponseEntity.ok(responseUrl);
    }
 
    private File binaryToFile(String base64Data, String saveDir, String userIdPng) {

        String binaryFile = base64Data.replaceAll("data:image/png;base64,", "");
        // 입력값 검증


        if ((binaryFile == null || binaryFile.isEmpty()) || (saveDir == null || saveDir.isEmpty())
                || (userIdPng == null || userIdPng.isEmpty())) {
            return null;
        }

        // 해당 디렉토리가 없다면 생성
        File fileDir = new File(saveDir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        // 저장될 파일 정의
        File destFile = new File(saveDir + File.separator + userIdPng);

        try {
            // Base64 문자열 바이트로
            byte[] decodedBytes = Base64.getDecoder().decode(binaryFile);

            // 디코드된 바이트를 파일에 쓰기
            try (FileOutputStream fos = new FileOutputStream(destFile)) {
                fos.write(decodedBytes);
            }
        } catch (IllegalArgumentException | IOException e) {
            System.out.println("파일 디코딩이나 쓰기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        return destFile;
    }
    
//    @ResponseBody
//    @RequestMapping(value = "searchReport.ajax", method = RequestMethod.POST)
//    public HashMap<String, Object> getGeport(ReportVo reportVo, @RequestParam HashMap<String, Object> req
//    		, final HttpSession session, HttpServletRequest request) throws Exception {
//        HashMap<String, Object> resultMap = new HashMap<>();
//
//        return resultMap;
//    }

}
