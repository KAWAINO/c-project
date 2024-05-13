package iwm_ko.crewInfo.controller;


import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import iwm_ko.comm.paging;
import iwm_ko.crewInfo.model.CrewInfoVo;
import iwm_ko.crewInfo.service.CrewInfoService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/crewinfo")
public class CrewInfoController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "iwm_ko.crewInfo.CrewInfoService")
    private CrewInfoService crewInfoService;
    
    @Autowired
    private UserHistoryService userHistoryService;
    
    @Autowired
    private MessageSource messageSource;

//    private String gui_code;
//
//    @PostConstruct
//    public void init() throws Exception {
//        gui_code = userHistoryService.searchGuiCode("crewinfo");
//    }

    /**
     * 페이지 로딩
     */
    @RequestMapping("crewinfo.do")
    public String crewInfo(@ModelAttribute("crewInfoVo") CrewInfoVo crewInfoVo, HttpServletRequest request
    		, final HttpSession session, Model model, Locale locale) throws Exception {

        crewInfoVo.setStartrow((crewInfoVo.getPageno() - 1) * crewInfoVo.getFetchrow());
        String sessionCompId = (String)session.getAttribute("comp_id");
        crewInfoVo.setSessionCompId(sessionCompId);

        model.addAttribute("sessionCompId",sessionCompId);
        List<CrewInfoVo> compList = this.crewInfoService.selectShipCompList(crewInfoVo);
        model.addAttribute("compList", compList);

        int totalCnt = -1;

        //검색조건 존재시 or 검색 버튼 클릭시
        if ("true".equals(crewInfoVo.getSearchBtn()) ||
            (!(crewInfoVo.getSearchCompId() == null || crewInfoVo.getSearchCompId().equals("")) ||
            !(crewInfoVo.getSearchIdName() == null || crewInfoVo.getSearchIdName().equals("")) ||
            !(crewInfoVo.getSearchSName() == null || crewInfoVo.getSearchSName().equals("")))) {

            List<CrewInfoVo> crewInfoList = this.crewInfoService.selectCrewInfoList(crewInfoVo, locale);

            for (CrewInfoVo crewInfo : crewInfoList) {
                // 여기에서 amt_rest 계산
                int amtTotalMonth = crewInfo.getAmt_total_month() != null ? Integer.parseInt(crewInfo.getAmt_total_month()) : 0;
                int accUseMonth = crewInfo.getAcc_use_day() != null ? Integer.parseInt(crewInfo.getAcc_use_day()) : 0;
                int amt_rest = amtTotalMonth - (accUseMonth / 1000);
                if (amt_rest < 0) {
                    amt_rest = 0;
                }
                crewInfo.setAmt_rest(String.valueOf(amt_rest));
            }
            model.addAttribute("crewInfoList", crewInfoList);

            // 페이징 처리를 위한 총 갯수
            totalCnt = this.crewInfoService.selectCrewInfoCount(crewInfoVo).size();

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("106");
            userHistory.setJob_name("검색");
            userHistory.setGui_remark("Data Search");
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("");
            userHistoryService.insertUserHistory(userHistory);
        }

        //paging
        paging paging = new paging();
        String pagingHTML = paging.getPagingStr(crewInfoVo.getPageno(), crewInfoVo.getFetchrow(), totalCnt);
        model.addAttribute("pagingHTML", pagingHTML);
        model.addAttribute("totalCnt", totalCnt);

        //search data return
        model.addAttribute("crewInfoVo", crewInfoVo);


        return "crewinfo/crewinfo";
    }


    /**
     * 선박별 정책명
     */
    @ResponseBody
    @PostMapping("/selectRateNameList.ajax")
    public HashMap<String, Object> selectRateNameListAjax(@RequestParam HashMap<String, Object> req, final HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            String s_name = req.get("s_name").toString();
            List<CrewInfoVo> rateNameList = crewInfoService.rateNameList(s_name);
            List<CrewInfoVo> crewIdList = crewInfoService.crewIdList(s_name);
            resultMap.put("rateNameList", rateNameList);
            resultMap.put("crewIdList", crewIdList);
            resultMap.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }

        return resultMap;
    }

    /**
     * 등록
     */
    @ResponseBody
    @RequestMapping(value = "crewInfoAdd.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> crewInfoAdd(CrewInfoVo crewInfoVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            int insResult = 0;
            //중복검사
            int chkRdata = this.crewInfoService.chkCrewInfo(crewInfoVo).size();
            if (chkRdata > 0) {
                resultMap.put("result", "-2");
            } else {
                if (crewInfoVo.getAddDay().equals("")) {
                    crewInfoVo.setAddDay("0");
                }
                if (crewInfoVo.getAddUse().equals("")) {
                    crewInfoVo.setAddUse("0");
                }
                if (crewInfoVo.getAddMin().equals("")) {
                    crewInfoVo.setAddMin("0");
                }
                Object chkFlagResult = this.crewInfoService.chkFlag(crewInfoVo);
                String chkFlag = chkFlagResult != null ? String.valueOf(chkFlagResult) : "N";

                if (chkFlag.equals("Y")) {
                    //chkFlag Y일경우 update로 등록
                    insResult = this.crewInfoService.updateInsertCrewInfo(crewInfoVo);
                } else {
                    insResult = this.crewInfoService.insertCrewInfo(crewInfoVo);

                }

                //로그 기록
                UserHistoryVo userHistory = new UserHistoryVo();
                userHistory.setGui_code("106");
                userHistory.setJob_name("추가");
                userHistory.setGui_remark("Crew ID : " + crewInfoVo.getAddCrewId() + ", Monthly supply(MB) : " + crewInfoVo.getAddMonth() + ", Ship Code : " + crewInfoVo.getAddSName());
                userHistory.setUser_id((String) session.getAttribute("user_id"));
                userHistory.setSql_str(" insert into TB_CREW_INFO  S_CODE, CREW_ID, PASSWD, CREW_NAME,AMT_TOTAL_MONTH, C_LIMIT_DAY_AMT, C_LIMIT_CONT_AMT, C_LIMIT_CONT_TIME,C_TIME_FROM, C_TIME_TO,C_APPLY_DAY, C_DAY_FROM, C_DAY_TO, C_TIME_FROM2,C_TIME_TO2, C_TIME_FROM3,C_TIME_TO3, RATE_NAME, MODIFY_DATE, DEL_DATE values ()");

                userHistoryService.insertUserHistory(userHistory);


                if (insResult > 0) {
                    int chkSubCrewInfo = this.crewInfoService.chkSubCrewInfo(crewInfoVo).size();
                    if (chkSubCrewInfo == 0) {
                        int insSubResult = this.crewInfoService.insertSubCrewInfo(crewInfoVo);
                        if (insSubResult > 0) {
                            resultMap.put("result", "1");
                        } else {
                            resultMap.put("result", "0");
                        }
                    } else {
                        resultMap.put("result", "1");
                    }
                } else {
                    resultMap.put("result", "0");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "0");
        }

        return resultMap;
    }


    /**
     * 수정 Modal 이동
     */
    @ResponseBody
    @RequestMapping(value = "crewInfoSetUpdateData.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> crewInfoSetUpdateData(@RequestParam HashMap<String, Object> req, final HttpSession session) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            CrewInfoVo crewInfoVo = this.crewInfoService.selectCrewInfo(req);
            //rateNameList
            List<CrewInfoVo> rdataList = this.crewInfoService.selectRdataList(crewInfoVo.getS_code());

            resultMap.put("crewInfoVo", crewInfoVo);
            resultMap.put("rdataList", rdataList);
            resultMap.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }
        return resultMap;
    }


    /**
     * 수정
     */
    @ResponseBody
    @RequestMapping(value = "crewInfoUpdate.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> crewInfoUpdate(CrewInfoVo crewInfoVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();


        try {
            //update
            int updResult = this.crewInfoService.updateCrewInfo(crewInfoVo);

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("106");
            userHistory.setJob_name("수정");
            userHistory.setGui_remark(this.crewInfoService.updateHistory(crewInfoVo));
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            String sql = this.crewInfoService.updateHistory(crewInfoVo);
            userHistory.setSql_str(sql);
            userHistoryService.insertUserHistory(userHistory);

            if (updResult > 0) {
                try {
                    crewInfoVo.setAddSName(crewInfoVo.getU_updSCode());
                    crewInfoVo.setAddCrewId(crewInfoVo.getU_updCrewId());
                    int chkSubCrewInfo = this.crewInfoService.chkSubCrewInfo(crewInfoVo).size();
                    if (chkSubCrewInfo == 0) {
                        int insSubResult = this.crewInfoService.insertSubCrewInfo(crewInfoVo);
                        if (insSubResult > 0) {
                            resultMap.put("result", "1");
                        } else {
                            resultMap.put("result", "0");
                        }
                    } else {
                        resultMap.put("result", "1");
                    }
                    resultMap.put("result", "1");

                } catch (Exception e) {
                    resultMap.put("result", "0");
                }

            } else {
                resultMap.put("result", "0");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "0");
        }

        return resultMap;
    }


    //엑셀 등록
    @ResponseBody
    @RequestMapping(value = "excelImport.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> excelImport(@RequestParam("add_file") MultipartFile file,
                                               @RequestParam(value = "add_field", defaultValue = "false") String cs_field,
                                               @RequestParam(value = "add_predel", defaultValue = "false") String cs_predel, CrewInfoVo crewInfoVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {

        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String saveDir = request.getSession().getServletContext().getRealPath("web/import/temp");
        String result = "";
        try {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                File destFile = new File(saveDir + File.separator + fileName);
                file.transferTo(destFile);


                if ("on,true".equals(cs_predel)) {
                    this.crewInfoService.delPreData();
                }
                result = this.crewInfoService.setExcel(file, cs_field, saveDir, destFile);

                resultMap.put("result", "1");
                resultMap.put("message", result);

            } else {
                resultMap.put("message", "파일이 없습니다.");

            }
        } catch (Exception e) {
            resultMap.put("result", "0");
            resultMap.put("message", "An error occurred during processing.");
        }

        // 로그 기록
        UserHistoryVo userHistory = new UserHistoryVo();
        userHistory.setGui_code("106");
        userHistory.setJob_name("Import");
        userHistory.setGui_remark("Excel Import");
        userHistory.setUser_id((String) session.getAttribute("user_id"));
        userHistory.setSql_str("");
        userHistoryService.insertUserHistory(userHistory);

        return resultMap;
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
            List<CrewInfoVo> shipNameList = crewInfoService.shipNameList(comp_id);

            resultMap.put("shipNameList", shipNameList);
            resultMap.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }

        return resultMap;
    }

    /**
     * 정책명 선택시 해당 정보 가져오기
     */
    @ResponseBody
    @PostMapping("/valList.ajax")
    public HashMap<String, Object> valList(@RequestParam HashMap<String, Object> req, CrewInfoVo crewInfoVo, final HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            crewInfoVo.setS_code(req.get("s_code").toString());
            crewInfoVo.setRate_name(req.get("rate_name").toString());
            List<CrewInfoVo> valList = crewInfoService.valList(crewInfoVo);

            resultMap.put("valList", valList);
            resultMap.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }

        return resultMap;
    }


    /**
     * possom 일괄적용
     */
    @ResponseBody
    @PostMapping("/addall.ajax")
    public HashMap<String, Object> addall(@RequestParam HashMap<String, Object> req, CrewInfoVo crewInfoVo, final HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            crewInfoVo.setSessionCompId((String) session.getAttribute("comp_id"));
            crewInfoVo.setSessionUserId((String) session.getAttribute("user_id"));
            String cs_code = "S_" + req.get("cs_code").toString();
            crewInfoVo.setCs_code(cs_code);

            ArrayList<String> arrRate = new ArrayList<String>();
            ArrayList<String> arrCrew = new ArrayList<String>();

            String arrCrewName[] = {"선장", "1등항해사", "2등항해사", "3등항해사", "실습항해사", "기관장", "1등기관사", "2등기관사", "3등기관사", "실습기관사", "갑판장", "갑판수", "갑판수", "갑판원", "갑판원", "조기장", "기관수", "기관수", "조리장", "조리원", "SPARE", "SPARE", "SPARE", "SPARE", "SPARE", "SPARE", "SPARE", "SPARE"};

            String arrCrewPwd[] = {"03097", "04096", "05095", "06094", "07093", "08092", "09091", "10090", "11089", "12088", "13087", "14086", "15085", "16084", "17083", "18082", "19081", "20080", "21079", "22078", "23077", "24076", "25075", "26074", "27073", "28072", "29071", "30070"};

            arrRate.add(0, "'" + cs_code + "', '5G', 5000, 0, 0, 0, '00', '00', ',1,2,3,4,5,6,7,', '20200101', '20300202', '00', '00', '00', '00'");
            arrRate.add(1, "'" + cs_code + "', '1G', 3500, 0, 0, 0, '00', '00', ',1,2,3,4,5,6,7,', '20200101', '20300202', '00', '00', '00', '00'");
            arrRate.add(2, "'" + cs_code + "', '1G_1', 3500, 0, 0, 0, '04', '07', ',1,2,3,4,5,6,7,', '20200101', '20300202', '16', '19', '00', '00'");
            arrRate.add(3, "'" + cs_code + "', '1G_2', 3500, 0, 0, 0, '00', '03', ',1,2,3,4,5,6,7,', '20200101', '20300202', '12', '15', '00', '00'");
            arrRate.add(4, "'" + cs_code + "', '1G_3', 3500, 0, 0, 0, '08', '11', ',1,2,3,4,5,6,7,', '20200101', '20300202', '20', '23', '00', '00'");
            arrRate.add(5, "'" + cs_code + "', '1G_4', 3500, 0, 0, 0, '08', '16', ',1,2,3,4,5,6,7,', '20200101', '20300202', '00', '00', '00', '00'");

            int chkScode = this.crewInfoService.chkScode(crewInfoVo).size();
            int rateResult = 0;
            int result = 0;
            if (chkScode > 0) {
                //chkScode 존재시
                for (int i = 0; i < arrRate.size(); i++) {

                    String rateData = arrRate.get(i);

                    String[] arrayRate = rateData.split(", ");
                    crewInfoVo.setS_code(arrayRate[0].replaceAll("'", ""));
                    crewInfoVo.setRate_name(arrayRate[1].replaceAll("'", ""));

                    int chkRate = this.crewInfoService.CheckRateName(crewInfoVo).size();
                    if (chkRate > 0) {
                        //chkRate 존재시 update
                        rateResult = this.crewInfoService.updateCrewRate(arrayRate);
                    } else {
                        //chkRate 미존재시 insert
                        rateResult = this.crewInfoService.insertCrewRate(rateData);
                    }


                    if (rateResult > 0) {

                        //update , insert 성공시

                        arrCrew.add(0, "'" + cs_code + "', 'Deck', '01099', 'Deck Part', 5000, 0, 0, 0, '00', '00', ',1,2,3,4,5,6,7,', '20200101', '20300202', " + "'00', '00', '00', '00', '5G'");
                        arrCrew.add(1, "'" + cs_code + "', 'Eng', '02098', 'Eng. Part', 5000, 0, 0, 0, '00', '00', ',1,2,3,4,5,6,7,', '20200101', '20300202', " + "'00', '00', '00', '00', '5G'");

                        for (int j = 0; j < arrCrewName.length; j++) {
                            String addSql = "'" + cs_code + "', 'CREW_" + ((j + 1) >= 10 ? (j + 1) : "0" + (j + 1)) + "', '" + arrCrewPwd[j] + "', '" + arrCrewName[j] + "', ";

                            switch (j + 3) {
                                case 4:
                                case 14:
                                case 15:
                                case 16:
                                    addSql += "3500, 0, 0, 0, '04', '07', ',1,2,3,4,5,6,7,', '20200101', '20300202', '16', '19', '00', '00', '1G_1'";
                                    break;
                                case 5:
                                    addSql += "3500, 0, 0, 0, '00', '03', ',1,2,3,4,5,6,7,', '20200101', '20300202', '12', '15', '00', '00', '1G_2'";
                                    break;
                                case 6:
                                    addSql += "3500, 0, 0, 0, '08', '11', ',1,2,3,4,5,6,7,', '20200101', '20300202', '20', '23', '00', '00', '1G_3'";
                                    break;
                                case 3:
                                case 8:
                                    addSql += "3500, 0, 0, 0, '00', '00', ',1,2,3,4,5,6,7,', '20200101', '20300202', '00', '00', '00', '00', '1G'";
                                    break;
                                default:
                                    addSql += "3500, 0, 0, 0, '08', '16', ',1,2,3,4,5,6,7,', '20200101', '20300202', '00', '00', '00', '00', '1G_4'";
                                    break;
                            }

                            arrCrew.add(j + 2, addSql);
                        }


                        for (int k = 0; k < arrCrew.size(); k++) {
                            String crewData = arrCrew.get(k);
                            String[] arrayCrew = crewData.split(", ");

//                            Map<String, Object> paramMap = new HashMap<>();
//
//
//                            paramMap.put("crewId", arrayCrew[0].replaceAll("'", ""));
//                            paramMap.put("s_code", arrayCrew[1].replaceAll("'", ""));
//                            paramMap.put("password", arrayCrew[2].replaceAll("'", ""));
//                            paramMap.put("crew_name", arrayCrew[3].replaceAll("'", ""));
//                            paramMap.put("amt_total_month", arrayCrew[4].replaceAll("'", ""));
//                            paramMap.put("limit_day_amt", arrayCrew[5].replaceAll("'", ""));
//                            paramMap.put("limit_cont_amt", arrayCrew[6].replaceAll("'", ""));
//                            paramMap.put("limit_cont_time", arrayCrew[7].replaceAll("'", ""));
//                            paramMap.put("time_from", arrayCrew[8].replaceAll("'", ""));
//                            paramMap.put("time_to", arrayCrew[9].replaceAll("'", ""));
//                            paramMap.put("apply_day", arrayCrew[10].replaceAll("'", ""));
//                            paramMap.put("day_from", arrayCrew[11].replaceAll("'", ""));
//                            paramMap.put("day_to", arrayCrew[12].replaceAll("'", ""));
//                            paramMap.put("time_from2", arrayCrew[13].replaceAll("'", ""));
//                            paramMap.put("time_to2", arrayCrew[14].replaceAll("'", ""));
//                            paramMap.put("time_from3", arrayCrew[15].replaceAll("'", ""));
//                            paramMap.put("time_to3", arrayCrew[16].replaceAll("'", ""));
//                            paramMap.put("rate_name", arrayCrew[17].replaceAll("'", ""));

                            crewInfoVo.setCrewNameSCode(arrayCrew[0].replaceAll("'", ""));
                            crewInfoVo.setCrewNameCrewId(arrayCrew[1].replaceAll("'", ""));


                            //crewName 중복확인
                            int chkCrew = this.crewInfoService.CheckCrewName(crewInfoVo).size();
                            if (chkCrew == 0) {
                                //S_code가 일치한 crewName 없을경우
                                crewInfoVo.setAddCrewId(crewInfoVo.getCrewNameCrewId());
                                crewInfoVo.setAddSName(crewInfoVo.getCrewNameSCode());
                                logger.info("crewInfoVo.SNAME : " + crewInfoVo.getAddSName());
                                logger.info("crewInfoVo.CREWID : " + crewInfoVo.getAddCrewId());

                                //delFlag 확인
                                String chkCrewFlag = this.crewInfoService.chkFlag(crewInfoVo);
                                if (chkCrewFlag != null && chkCrewFlag.equals("Y")) {
                                    //delFlag Y일경우 crew정보 업데이트
                                    result = this.crewInfoService.updateCrewName(arrayCrew);
                                } else if (chkCrewFlag != null && chkCrewFlag.equals("N")){
                                    //delFlag N일경우 crew정보 insert
                                    result = this.crewInfoService.updateCrewName(arrayCrew);
                                }else {
                                    result = this.crewInfoService.insertCrewName(arrayCrew);
                                }

                            } else {

                                //s_code가 일치한 crewName이 있을경우 해당 선원 정보 update
                                result = this.crewInfoService.updateCrewName(arrayCrew);

                            }

                            logger.info("result :: " + result);

                            if (result == 1) {
                                //선원 정보 등록,수정 성공시 subdata
                                crewInfoVo.setAllSCode(arrayCrew[0].replaceAll("'", ""));
                                crewInfoVo.setUpdCrewId(arrayCrew[1].replaceAll("'", ""));

                                //subData 존재 확인
                                int chkSubData = this.crewInfoService.chkSubData(crewInfoVo).size();
                                if (chkSubData == 0) {
                                    //subData 미존재시 insert
                                    crewInfoVo.setAddSName(arrayCrew[0].replaceAll("'", ""));
                                    crewInfoVo.setAddCrewId(arrayCrew[1].replaceAll("'", ""));
                                    crewInfoVo.setAddVal2("N");

                                    result = this.crewInfoService.insertSubCrewInfo(crewInfoVo);
                                    if (result == 1) {
                                        //subData 등록 후 결과 return
                                        resultMap.put("result", "1");
                                    }else {
                                        //실패시 -1 return
                                        resultMap.put("result", "-1");

                                        return resultMap;
                                    }
                                } else {
                                    //subData 존재시 pass
                                    resultMap.put("result", "1");

                                }

                            } else {
                                //선원 정보 등록,수정 실패시 -1 return
                                resultMap.put("result", "-1");
                                return resultMap;
                            }

                        }

                    }
                }
            } else {
                //chkScode 미존재시 선박번호 재입력 return
                resultMap.put("result", "3");

                return resultMap;
            }
            resultMap.put("result", "1");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }

        return resultMap;
    }

    /**
     * 삭제
     */
    @ResponseBody
    @RequestMapping(value = "crewInfoDelete.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> crewInfoDelete(CrewInfoVo crewInfoVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            //update

            int updResult = this.crewInfoService.deleteCrewInfo(crewInfoVo);

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("106");
            userHistory.setJob_name("삭제");
            userHistory.setGui_remark("Crew ID : " + crewInfoVo.getUpdCrewId() + ", Ship Code : " + crewInfoVo.getS_code());
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str(" update TB_CREW_INFO set DEL_FLAG='Y', DEL_DATE=NOW(), GUI_FLAG='Y' where CREW_ID="+ crewInfoVo.getUpdCrewId() + " and S_CODE="+crewInfoVo.getS_code());

            userHistoryService.insertUserHistory(userHistory);

            if (updResult > 0) {
                resultMap.put("result", "1");
            } else {
                resultMap.put("result", "0");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "0");
        }
        return resultMap;
    }


    /**
     * 일괄 수정
     */
    @ResponseBody
    @RequestMapping(value = "updateCrewInfoAll.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> updateCrewInfoAll(CrewInfoVo crewInfoVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();


        try {
            String historyReMark = "";
            String sql = "";
            int updResult = 0;

            switch (crewInfoVo.getCs_select()) {
                //데이터 사용 정책
                case "rate":
                    // 기본 정책으로 저장 미체크시 'N' 설정
                    if (crewInfoVo.getSave_flag() == null) {
                        crewInfoVo.setSave_flag("N");
                    }
                    //기본 정책으로 저장 체크시

                        updResult = this.crewInfoService.updateAllRate(crewInfoVo);

                        historyReMark = "Crew Rate all update : Rate Name - " + crewInfoVo.getRate_name();

                        CrewInfoVo crewInfoVoSql = new CrewInfoVo();
                        crewInfoVoSql.setRate_name(crewInfoVo.getDefaultRateName());
                        crewInfoVoSql.setS_code(crewInfoVo.getAllSCode());

                       crewInfoVoSql =  this.crewInfoService.getRateInfo(crewInfoVo);


                        sql = "update TB_CREW_INFO set " +
                                "AMT_TOTAL_MONTH=" + crewInfoVoSql.getAmt_total_month() +
                                " C_LIMIT_DAY_AMT = " + crewInfoVoSql.getLimit_day_amt() +
                                ", C_LIMIT_CONT_AMT =" + crewInfoVoSql.getLimit_cont_amt() +
                                " , C_LIMIT_CONT_TIME +" + crewInfoVoSql.getLimit_cont_time() +
                                ", C_TIME_FROM " + crewInfoVoSql.getTime_from() + ", C_TIME_TO =" + crewInfoVoSql.getTime_to() +
                                " ,C_APPLY_DAT" + crewInfoVoSql.getApply_day() + ", C_DAY+FROM= " + crewInfoVoSql.getDay_from() +
                                " , C_DAY_TO = " + crewInfoVoSql.getDay_to() + ", C_TIME_FROM2 = " + crewInfoVoSql.getTime_from2() +
                                ", C_TIME_TO2 = " + crewInfoVoSql.getTime_to2() + ", C_TIME_FROM3 = " + crewInfoVoSql.getTime_from3() +
                                " ,C_TIME_TO3 =" + crewInfoVoSql.getTime_to3() + " RATE_NAME = " + crewInfoVo.getDefaultRateName() +
                                "GUI_FLAG= 'Y' where CREW_ID in (" + crewInfoVo.getSelectedCrewIds() + ") and S_CODE = " + crewInfoVo.getAllSCode();

                    break;
                //WIFI 차단 / 해제
                case "wifi":

                    for (String crew_id : crewInfoVo.getSelectedCrewIds()) {
                        crewInfoVo.setUpdCrewId(crew_id);
                        // TB_CREW_INFO_SUB 확인
                        int result = crewInfoService.chkSubData(crewInfoVo).size();
                        if (result == 0) {
                            //없으면 subCrewInsert
                            crewInfoVo.setAddSName(crewInfoVo.getAllSCode());
                            crewInfoVo.setAddCrewId(crew_id);
                            crewInfoVo.setAddVal2(crewInfoVo.getUpdVal2());
                            crewInfoService.insertSubCrewInfo(crewInfoVo);
                        }
                    }
                    // WIFI 차단 / 해제 업데이트 (cs_data)
                    updResult = this.crewInfoService.updateAllWifi(crewInfoVo);
                    // Gui_remark 설정
                    historyReMark = "Crew WiFi ";
                    if (crewInfoVo.getCs_data().equals("0")) {
                        historyReMark += " block";
                    } else {
                        historyReMark += " unblock";
                    }
                    //history sql 설정
                    sql = "update TB_CREW_INFO_SUB set WIFI_CONN= " + crewInfoVo.getCs_data() + " GUI_FLAG='Y' where CREW_ID in " + crewInfoVo.getSelectedCrewIds() +
                            " and S_CODE=" + crewInfoVo.getAllSCode();
                    break;
                case "default":
                    //정책 기본값 불러오기
                    updResult = this.crewInfoService.updateAllDefault(crewInfoVo);
                    break;
                case "carry_over":
                    //데이터 이월 기능
                    updResult = this.crewInfoService.updateAllVal2(crewInfoVo);
                    historyReMark = "Carry Over ";
                    if (crewInfoVo.getAllVal2().equals("Y")) {
                        historyReMark += "Use ";
                    } else if (crewInfoVo.getAllVal2().equals("N")) {
                        historyReMark += "Unuse";
                    }

                    sql = "UPDATE TB_CREW_INFO_SUB set VAL2= " + crewInfoVo.getAllVal2() + ",GUI_FLAG='Y' where CREW_ID in (" + crewInfoVo.getSelectedCrewIds() + ") and S_CODE=" + crewInfoVo.getAllSCode();
                    break;
                default:
                    updResult = 0;
                    break;
            }
                //정책 기본값 불러오기일시 log 미작성
                    if (historyReMark.length() >= 5){

                    // 로그 기록
                    UserHistoryVo userHistory = new UserHistoryVo();
                    userHistory.setGui_code("106");
                    userHistory.setJob_name("수정");
                    userHistory.setGui_remark(historyReMark);
                    userHistory.setUser_id((String) session.getAttribute("user_id"));
                    userHistory.setSql_str(sql);

                    userHistoryService.insertUserHistory(userHistory);
                    }

            if (updResult > 0) {
                resultMap.put("result", "1");
            } else {
                resultMap.put("result", "0");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "0");
        }

        return resultMap;
    }


    /**
     * Excel
     */
    @RequestMapping(value = "crewInfoExcelDownload.do")
    public void crewInfoExcelDownload(HttpServletResponse response, final HttpSession session
    		, CrewInfoVo crewInfoVo, Locale locale) throws Exception {
    	
    	String language = (String)session.getAttribute("language");

        try {

            crewInfoVo.setStartrow(-1);
            List<CrewInfoVo> excelList = this.crewInfoService.selectCrewInfoList(crewInfoVo, locale);


            String[] l_aColumns = {
            		messageSource.getMessage("list.crewID", null, locale), 
            		messageSource.getMessage("list.password", null, locale), 
            		messageSource.getMessage("list.crewNickName", null, locale), 
            		messageSource.getMessage("select.shipOwner", null, locale), 
            		messageSource.getMessage("select.shipName", null, locale), 
            		messageSource.getMessage("list.monthlySupply", null, locale), 
            		messageSource.getMessage("list.dailyUseLimit", null, locale), 
            		messageSource.getMessage("list.continuousUseLimit", null, locale), 
            		messageSource.getMessage("list.continuousUseTimeout", null, locale), 
            		messageSource.getMessage("list.usageTimeLimitStart", null, locale), 
            		messageSource.getMessage("list.usageTimeLimitEnd", null, locale), 
            		messageSource.getMessage("list.usageTimeLimitStart2", null, locale), 
            		messageSource.getMessage("list.usageTimeLimitEnd2", null, locale), 
            		messageSource.getMessage("list.usageTimeLimitStart3", null, locale), 
            		messageSource.getMessage("list.usageTimeLimitEnd3", null, locale), 
            		messageSource.getMessage("list.dayOfUse", null, locale), 
            		messageSource.getMessage("list.periodOfUseStart", null, locale), 
            		messageSource.getMessage("list.periodOfUseEnd", null, locale), 
            		messageSource.getMessage("crewInfo.excelList.dataUsagePolicy", null, locale), 
            		messageSource.getMessage("crewInfo.excelList.blockUnblock", null, locale), 
            		messageSource.getMessage("crewInfo.excelList.carryOverBlockUnBlock", null, locale)
            		};

            String xlsName = messageSource.getMessage("crewInfo.crewInformation", null, locale);
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
			
	        //넘버링 스타일
            CellStyle numberStyle = wb.createCellStyle();
            numberStyle.cloneStyleFrom(csBody);
            DataFormat format = wb.createDataFormat();
            numberStyle.setDataFormat(format.getFormat("#,###"));
			
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

            for (CrewInfoVo vo : excelList) {
                row = sheet.createRow(rowIdx);

                colIdx = 0;
                
                Cell cell;

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getCrew_id());
                cell.setCellStyle(csBody);

                cell = row.createCell(colIdx++);
                cell.setCellValue("*");
                cell.setCellStyle(csBody);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getCrew_name());
                cell.setCellStyle(csBody);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getComp_name());
                cell.setCellStyle(csBody);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getS_name());
                cell.setCellStyle(csBody);

                cell = row.createCell(colIdx++);
                if (vo.getAmt_total_month() == null  || vo.getAmt_total_month().equals("0")){
                    vo.setAmt_total_month("0");
                    cell.setCellValue(vo.getAmt_total_month());
                    cell.setCellStyle(csBody);
                }else {
                    cell.setCellValue(parseNumberString(vo.getAmt_total_month()));
                    cell.setCellStyle(numberStyle);
                }

                cell = row.createCell(colIdx++);
                if (vo.getLimit_day_amt() == null || vo.getLimit_day_amt().equals("0")){
                    vo.setLimit_day_amt("0");
                    cell.setCellValue(vo.getLimit_day_amt());
                    cell.setCellStyle(csBody);
                }else {
                    cell.setCellValue(parseNumberString(vo.getLimit_day_amt()));
                    cell.setCellStyle(numberStyle);
                }

                cell = row.createCell(colIdx++);
                if (vo.getLimit_cont_amt() == null || vo.getLimit_cont_amt().equals("0")){
                    vo.setLimit_cont_amt("0");
                    cell.setCellValue(vo.getLimit_cont_amt());
                    cell.setCellStyle(csBody);
                }else {
                    cell.setCellValue(parseNumberString(vo.getLimit_cont_amt()));
                    cell.setCellStyle(numberStyle);
                }


                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getLimit_cont_time());
                cell.setCellStyle(csBody);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getTime_from());
                cell.setCellStyle(csBody);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getTime_to());
                cell.setCellStyle(csBody);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getTime_from2());
                cell.setCellStyle(csBody);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getTime_to2());
                cell.setCellStyle(csBody);


                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getTime_from3());
                cell.setCellStyle(csBody);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getTime_to3());
                cell.setCellStyle(csBody);

                // 셀 값 설정 부분
                cell = row.createCell(colIdx++);
                cell.setCellValue(convertNumbersToDays(vo.getApply_day()));
                cell.setCellStyle(csBody);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getDay_from());
                cell.setCellStyle(csBody);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getDay_to());
                cell.setCellStyle(csBody);

                cell = row.createCell(colIdx++);
                cell.setCellValue(vo.getRate_name());
                cell.setCellStyle(csBody);

                cell = row.createCell(colIdx++);
                String wifiValue = "1".equals(vo.getWifi_conn()) ? 
                		messageSource.getMessage("crewInfo.block", null, locale) : messageSource.getMessage("crewInfo.unBlock", null, locale);
                cell.setCellValue(wifiValue);
                cell.setCellStyle(csBody);


                cell = row.createCell(colIdx++);
                String val2Value = "Y".equals(vo.getVal2()) ? 
                		messageSource.getMessage("crewInfo.use", null, locale) : messageSource.getMessage("crewInfo.unUse", null, locale);
                cell.setCellValue(val2Value);
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


            //  로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("106");
            userHistory.setJob_name("엑셀");
            userHistory.setGui_remark("사유 : " + crewInfoVo.getExcel_msg());
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("select B.CREW_ID as \"선원 ID\", '*' as \"비밀번호\", B.CREW_NAME as \"선원 별칭\", A.COMP_NAME as \"선주사\", A.S_NAME \"선박명\", B.AMT_TOTAL_MONTH as \"NN_월 제공량\", B.C_LIMIT_DAY_AMT as \"NN_하루사용 제한량\", B.C_LIMIT_CONT_AMT as \"NN_연속사용 제한량\", B.C_LIMIT_CONT_TIME as \"연속사용 제한시간\", B.C_TIME_FROM as \"사용 제한 시간 시작\", B.C_TIME_TO as \"사용 제한 시간 끝\", B.C_TIME_FROM2 as \"사용 제한 시간 시작2\", B.C_TIME_TO2 as \"사용 제한 시간 끝2\", B.C_TIME_FROM3 as \"사용 제한 시간 시작3\", B.C_TIME_TO3 as \"사용 제한 시간 끝3\", FNC_GET_WEEK_DAY_ARRAY(B.C_APPLY_DAY) as \"사용 요일\",B.C_DAY_FROM as \"사용기간 시작\", B.C_DAY_TO as \"사용기간 끝\", B.RATE_NAME as \"데이터 정책\", (case when IFNULL(D.WIFI_CONN,1)=0 then '차단' else '허용' end) as 'WiFi 차단/해제', (CASE WHEN TRIM(IFNULL(D.VAL2, 'N')) = 'Y' THEN '사용' ELSE '미사용' END ) as '데이터 이월 사용/미사용'from (select S.COMP_ID, C.COMP_NAME, S.S_NAME, S.S_CODE from TB_SHIP_INFO S, TB_SCOMP_INFO C where S.COMP_ID=C.COMP_ID)A, TB_CREW_INFO B left join TB_CREW_INFO_SUB D on B.CREW_ID=D.CREW_ID and B.S_CODE=D.S_CODE where A.S_CODE=B.S_CODE and B.DEL_FLAG='N' order by A.COMP_NAME, A.S_NAME, B.CREW_NAME, B.CREW_ID");

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

    private double parseNumberString(String numberStr) {
        try {
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number = format.parse(numberStr);
            return number.doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // 오류 발생 시 기본값으로 0을 반환
        }
    }

}


