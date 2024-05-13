package iwm_ko.crewInfo.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import iwm_ko.crewInfo.dao.CrewInfoDao;
import iwm_ko.crewInfo.model.CrewInfoVo;
import iwm_ko.crewInfo.service.CrewInfoService;
import jakarta.annotation.Resource;

@Service("iwm_ko.crewInfo.CrewInfoService")
//@MapperScan("iwm_ko.crewInfo.dao")
public class CrewInfoServiceImpl implements CrewInfoService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    CrewInfoVo crewInfoVo;
 
    @Autowired
    private MessageSource messageSource;

    @Resource(name = "iwm_ko.crewInfo.crewInfoDao")
    private CrewInfoDao crewInfoDao;

    @Override
    public List<CrewInfoVo> selectCrewInfoList(CrewInfoVo crewInfoVo, Locale locale) throws Exception {
    	
        List<CrewInfoVo> resList = this.crewInfoDao.selectCrewInfoList(crewInfoVo, locale);
        
        for(int i = 0; i < resList.size(); i++) {
        	if(resList.get(i).getCrew_status() == null || resList.get(i).getCrew_status().isEmpty()) {
        		resList.get(i).setCrew_status("");
        	} else if(resList.get(i).getCrew_status().equals("1")) {
        		resList.get(i).setCrew_status(messageSource.getMessage("crewInfo.normal", null, locale));
        	} else if(resList.get(i).getCrew_status().equals("2")) {
        		resList.get(i).setCrew_status(messageSource.getMessage("crewInfo.monthlydeliveryExceeded", null, locale));
        	} else if(resList.get(i).getCrew_status().equals("3")) {
        		resList.get(i).setCrew_status(messageSource.getMessage("crewInfo.dailyUseLimitExceeded", null, locale));
        	} else if(resList.get(i).getCrew_status().equals("4")) {
        		resList.get(i).setCrew_status(messageSource.getMessage("crewInfo.notInUseTime", null, locale));
        	} else if(resList.get(i).getCrew_status().equals("5")) {
        		resList.get(i).setCrew_status(messageSource.getMessage("crewInfo.noDayOfUse", null, locale));
        	} else if(resList.get(i).getCrew_status().equals("6")) {
        		resList.get(i).setCrew_status(messageSource.getMessage("crewInfo.notInUsePeriod", null, locale));
        	} else if(resList.get(i).getCrew_status().equals("7")) {
        		resList.get(i).setCrew_status(messageSource.getMessage("crewInfo.continuousUsageExceeded", null, locale));
        	} else if(resList.get(i).getCrew_status().equals("8")) {
        		resList.get(i).setCrew_status(messageSource.getMessage("crewInfo.continuousUseTimeOut", null, locale));
        	} else if(resList.get(i).getCrew_status().equals("9")) {
        		resList.get(i).setCrew_status(messageSource.getMessage("crewInfo.disconnectionByUser", null, locale));
        	} else if(resList.get(i).getCrew_status().equals("10")) {
        		resList.get(i).setCrew_status(messageSource.getMessage("crewInfo.disconnectionByOperator", null, locale));
        	} else if(resList.get(i).getCrew_status().equals("11")) {
        		resList.get(i).setCrew_status(messageSource.getMessage("crewInfo.deauthrization", null, locale));
        	} else if(resList.get(i).getCrew_status().equals("12")) {
        		resList.get(i).setCrew_status(messageSource.getMessage("crewInfo.concurrentUsersExceeded", null, locale));
        	} else if(resList.get(i).getCrew_status().equals("13")) {
        		resList.get(i).setCrew_status(messageSource.getMessage("crewInfo.blockWIFIuse", null, locale));
        	}
        	
        }
        
        return resList;
    }

    @Override
    public List<CrewInfoVo> selectShipCompList(CrewInfoVo crewInfoVo) throws Exception {
        List<CrewInfoVo> resList = this.crewInfoDao.selectShipCompList(crewInfoVo);
        return resList;
    }

    @Override
    public List<CrewInfoVo> selectCrewInfoCount(CrewInfoVo crewInfoVo) throws Exception {
        List<CrewInfoVo> resList = this.crewInfoDao.selectCrewInfoCount(crewInfoVo);
        return resList;

    }

    @Override
    public List<CrewInfoVo> chkCrewInfo(CrewInfoVo crewInfoVo) throws Exception {
        List<CrewInfoVo> resData = this.crewInfoDao.chkCrewInfo(crewInfoVo);
        return resData;
    }

    @Override
    public int insertCrewInfo(CrewInfoVo crewInfoVo) throws Exception {
        return this.crewInfoDao.insertCrewInfo(crewInfoVo);
    }

    @Override
    public CrewInfoVo selectCrewInfo(HashMap<String, Object> map) throws Exception {
        return this.crewInfoDao.selectCrewInfo(map);
    }

    @Override
    public List<CrewInfoVo> selectRdataList(String s_name) throws Exception {
        List<CrewInfoVo> resList = this.crewInfoDao.selectRdataList(s_name);
        return resList;
    }

    @Override
    public int insertSubCrewInfo(CrewInfoVo crewInfoVo) throws Exception {
        return this.crewInfoDao.insertSubCrewInfo(crewInfoVo);
    }

    @Override
    public List<CrewInfoVo> chkSubCrewInfo(CrewInfoVo crewInfoVo) throws Exception {
        List<CrewInfoVo> resData = this.crewInfoDao.chkSubCrewInfo(crewInfoVo);
        return resData;
    }

    @Override
    public int updateInsertCrewInfo(CrewInfoVo crewInfoVo) throws Exception {
        return this.crewInfoDao.updateInsertCrewInfo(crewInfoVo);
    }

    @Override
    public int updateCrewInfo(CrewInfoVo crewInfoVo) throws Exception {
        return this.crewInfoDao.updateCrewInfo(crewInfoVo);
    }

    @Override
    public String chkFlag(CrewInfoVo crewInfoVo) throws Exception {
        return this.crewInfoDao.chkFlag(crewInfoVo);
    }

    @Override
    @Transactional
    public int updateAllDefault(CrewInfoVo crewInfoVo) throws Exception {
        int result = 0;

        for (String crew_id : crewInfoVo.getSelectedCrewIds()) {


            crewInfoVo.setUpdCrewId(crew_id);
            // val1 조회 후 rate_name으로 set
            crewInfoVo.setRate_name(this.crewInfoDao.getVal1(crewInfoVo));

            if (crewInfoVo.getRate_name() != null && crewInfoVo.getRate_name() != "") {

                CrewInfoVo sub = new CrewInfoVo();
                sub = this.crewInfoDao.getRateInfo(crewInfoVo);
                crewInfoVo.setAmt_total_month(sub.getAmt_total_month());
                crewInfoVo.setLimit_day_amt(sub.getLimit_day_amt());
                crewInfoVo.setLimit_cont_amt(sub.getLimit_cont_amt());
                crewInfoVo.setLimit_cont_time(sub.getLimit_cont_time());
                crewInfoVo.setTime_from(sub.getTime_from());
                crewInfoVo.setTime_from2(sub.getTime_from2());
                crewInfoVo.setTime_from3(sub.getTime_from3());
                crewInfoVo.setTime_to(sub.getTime_to());
                crewInfoVo.setTime_to2(sub.getTime_to2());
                crewInfoVo.setTime_to3(sub.getTime_to3());
                crewInfoVo.setApply_day(sub.getApply_day());
                crewInfoVo.setDay_from(sub.getDay_from());
                crewInfoVo.setDay_to(sub.getDay_to());
                // TB_CREW_INFO 에 update
                result = this.crewInfoDao.getAllDefault(crewInfoVo);

            }
        }
        return result;
    }

    @Override
    public int updateAllRate(CrewInfoVo crewInfoVo) throws Exception {
        int result = 0;
        if (crewInfoVo.getSave_flag().equals("Y")) {
            for (String crew_id : crewInfoVo.getSelectedCrewIds()) {
                crewInfoVo.setUpdCrewId(crew_id);
                //현재 rate_name 확인 후 defaultRateName으로 set
                crewInfoVo.setDefaultRateName(this.crewInfoDao.getDefaultRate(crewInfoVo));
                // subData 존재시 update 미존재시 insert
                result = this.crewInfoDao.chkSubData(crewInfoVo).size();
                if (result == 1) {
                    this.crewInfoDao.updateDefaultVal1(crewInfoVo);
                } else if (result == 0) {
                    crewInfoVo.setAddSName(crewInfoVo.getAllSCode());
                    crewInfoVo.setCrew_id(crew_id);
                    crewInfoVo.setVal2("N");
                    this.crewInfoDao.insertSubCrewInfo(crewInfoVo);
                }
            }
            crewInfoVo.setRate_name(crewInfoVo.getDefaultRateName());

        } else if ((crewInfoVo.getSave_flag().equals("N"))) {
            crewInfoVo.setRate_name(crewInfoVo.getAllRdata());
        }
        CrewInfoVo sub = new CrewInfoVo();
        sub = this.crewInfoDao.getRateInfo(crewInfoVo);

        crewInfoVo.setAmt_total_month(sub.getAmt_total_month());
        crewInfoVo.setLimit_day_amt(sub.getLimit_day_amt());
        crewInfoVo.setLimit_cont_amt(sub.getLimit_cont_amt());
        crewInfoVo.setLimit_cont_time(sub.getLimit_cont_time());
        crewInfoVo.setTime_from(sub.getTime_from());
        crewInfoVo.setTime_from2(sub.getTime_from2());
        crewInfoVo.setTime_from3(sub.getTime_from3());
        crewInfoVo.setTime_to(sub.getTime_to());
        crewInfoVo.setTime_to2(sub.getTime_to2());
        crewInfoVo.setTime_to3(sub.getTime_to3());
        crewInfoVo.setApply_day(sub.getApply_day());
        crewInfoVo.setDay_from(sub.getDay_from());
        crewInfoVo.setDay_to(sub.getDay_to());

        crewInfoVo.setRate_name(crewInfoVo.getAllRdata());
        result = this.crewInfoDao.updateAllRate(crewInfoVo);

        return result;
    }


    @Override
    public String updateHistory(CrewInfoVo crewInfoVo) throws Exception {

        String history = "Crew ID : " + crewInfoVo.getU_updCrewId() + ", ";


        if (!crewInfoVo.getCrew_name().equals(crewInfoVo.getUpdCrewName())) {
            history += "Crew Name : (Before) " + crewInfoVo.getCrew_name() + " / (After) " + crewInfoVo.getUpdCrewName() + ", ";
        }
        if (!crewInfoVo.getRate_name().equals(crewInfoVo.getUpdRdata())) {
            history += "Data usage policy : (Before) " + crewInfoVo.getRate_name() + " / (After) " + crewInfoVo.getUpdRdata() + ", ";
        }

        if (!crewInfoVo.getAmt_total_month().equals(crewInfoVo.getUpdMonth())) {
            history += "Monthly supply(MB) : (Before) " + crewInfoVo.getAmt_total_month() + " / (After) " + crewInfoVo.getUpdMonth() + ", ";
        }

        if (!crewInfoVo.getLimit_day_amt().equals(crewInfoVo.getUpdDay())) {
            history += "Daily use limit(MB) : (Before) " + crewInfoVo.getLimit_day_amt() + " / (After) " + crewInfoVo.getUpdDay() + ", ";
        }

        if (!crewInfoVo.getLimit_cont_amt().equals(crewInfoVo.getUpdUse())) {
            history += "Continuous use limit(MB) : (Before) " + crewInfoVo.getLimit_cont_amt() + " / (After) " + crewInfoVo.getUpdUse() + ", ";
        }

        if (!crewInfoVo.getLimit_cont_time().equals(crewInfoVo.getUpdMin())) {
            history += "Continuous use timeout(min) : (Before) " + crewInfoVo.getLimit_cont_time() + " / (After) " + crewInfoVo.getUpdMin() + ", ";
        }

        if (!crewInfoVo.getTime_from().equals(crewInfoVo.getUpdTimeFrom())) {
            history += "Start of Usage time limit range : (Before) " + crewInfoVo.getTime_from() + "hour / (After) " + crewInfoVo.getUpdTimeFrom() + "hour, ";
        }

        if (!crewInfoVo.getTime_to().equals(crewInfoVo.getUpdTimeTo())) {
            history += "End of Usage time limit range : (Before) " + crewInfoVo.getTime_to() + "hour / (After) " + crewInfoVo.getUpdTimeTo() + "hour, ";
        }

        if (!crewInfoVo.getTime_from2().equals(crewInfoVo.getUpdTimeFrom2())) {
            history += "Start of Usage time limit range2 : (Before) " + crewInfoVo.getTime_from2() + "hour / (After) " + crewInfoVo.getUpdTimeFrom2() + "hour, ";
        }

        if (!crewInfoVo.getTime_to2().equals(crewInfoVo.getUpdTimeTo2())) {
            history += "End of Usage time limit range2  : (Before) " + crewInfoVo.getTime_to2() + "hour / (After) " + crewInfoVo.getUpdTimeTo2() + "hour, ";
        }
        if (!crewInfoVo.getTime_from3().equals(crewInfoVo.getUpdTimeFrom3())) {
            history += "Start of Usage time limit range3 : (Before) " + crewInfoVo.getTime_from3() + "hour / (After) " + crewInfoVo.getUpdTimeFrom3() + "hour, ";
        }

        if (!crewInfoVo.getTime_to3().equals(crewInfoVo.getUpdTimeTo3())) {
            history += "End of Usage time limit range3 : (Before) " + crewInfoVo.getTime_to3() + "hour / (After) " + crewInfoVo.getUpdTimeTo3() + "hour, ";
        }

        String preApplyDays = formatApplyDays(crewInfoVo.getApply_day());
        String updApplyDays = formatApplyDays(crewInfoVo.getUpdApplyDays());
        if (!preApplyDays.equals(updApplyDays)) {
            history += "Day of use: (Before) " + preApplyDays + " / (After) " + updApplyDays + ", ";
        }

        if (!crewInfoVo.getDay_from().equals(crewInfoVo.getUpdDayFrom())) {
            String from = fixFormat(crewInfoVo.getUpdDayFrom());
            String pre_from = fixFormat(crewInfoVo.getDay_from());
            history += "Start of Period of use : (Before) " + pre_from + " / (After) " + from + ", ";
        }

        if (!crewInfoVo.getDay_to().equals(crewInfoVo.getUpdDayTo())) {
            String to = fixFormat(crewInfoVo.getUpdDayTo());
            String pre_to = fixFormat(crewInfoVo.getDay_to());
            history += "End of Period of use : (Before) " + pre_to + " / (After) " + to + ", ";
        }

        if (!crewInfoVo.getVal2().equals(crewInfoVo.getVal2())) {
            history += "Data Carry Over : (Before) " + crewInfoVo.getVal2() + " / (After) " + crewInfoVo.getUpdVal2() + ", ";
        }

        if (history.endsWith(", ")) {
            history = history.substring(0, history.length() - 2);
        }

        return history;
    }


    @Override
    public List<CrewInfoVo> shipNameList(String comp_id) throws Exception {
        List<CrewInfoVo> resList = this.crewInfoDao.selectShipNameList(comp_id);
        return resList;

    }

    @Override
    public List<CrewInfoVo> rateNameList(String s_name) throws Exception {
        List<CrewInfoVo> resList = this.crewInfoDao.selectRateNameList(s_name);
        return resList;
    }

    @Override
    public List<CrewInfoVo> crewIdList(String s_name) throws Exception {
        List<CrewInfoVo> resList = this.crewInfoDao.selectCrewIdList(s_name);
        return resList;

    }

    @Override
    public int updateAllWifi(CrewInfoVo crewInfoVo) throws Exception {
        return this.crewInfoDao.updateAllWifi(crewInfoVo);
    }

    @Override
    public List<CrewInfoVo> chkSubData(CrewInfoVo crewInfoVo) throws Exception {
        return this.crewInfoDao.chkSubData(crewInfoVo);
    }


    @Override
    public int deleteCrewInfo(CrewInfoVo crewInfoVo) throws Exception {
        return this.crewInfoDao.deleteCrewInfo(crewInfoVo);
    }

    @Override
    public CrewInfoVo getRateInfo(CrewInfoVo crewInfoVo) throws Exception {
        return this.crewInfoDao.getRateInfo(crewInfoVo);
    }

    @Override
    public int updateAllVal2(CrewInfoVo crewInfoVo) throws Exception {
        int result = 0;

        for (String crew_id : crewInfoVo.getSelectedCrewIds()) {
            crewInfoVo.setUpdCrewId(crew_id);

            //subData 확인 후 존재시 update 없으면 insert
            result = this.crewInfoDao.chkSubData(crewInfoVo).size();
            if (result == 0) {
                crewInfoVo.setAddSName(crewInfoVo.getU_updSName());
                crewInfoVo.setAddCrewId(crewInfoVo.getU_updCrewId());
                crewInfoVo.setAddVal2(crewInfoVo.getAllVal2());
                this.crewInfoDao.insertSubCrewInfo(crewInfoVo);
            }
        }
        result = this.crewInfoDao.updateAllVal2(crewInfoVo);

        return result;
    }

    @Override
    public int updateDefaultVal1(CrewInfoVo crewInfoVo) throws Exception {
        return this.crewInfoDao.updateDefaultVal1(crewInfoVo);
    }

    @Override
    public String selectDefaultRateName(CrewInfoVo crewInfoVo) throws Exception {
        return this.crewInfoDao.getDefaultRate(crewInfoVo);
    }

    @Override
    public int delPreData() throws Exception {
        return this.crewInfoDao.delPreData();
    }


    private String formatApplyDays(String applyDays) {

        if (applyDays == null || applyDays.equals(",,"))
            return "No day of use";

        String[] dayNumbers = applyDays.split(",");
        StringBuilder formattedDays = new StringBuilder();
        for (String dayNum : dayNumbers) {
            if (!dayNum.isEmpty()) {
                String day = getDayName(dayNum);
                if (day != null) {
                    if (formattedDays.length() > 0) formattedDays.append(", ");
                    formattedDays.append(day);
                }
            }
        }

        return formattedDays.toString();
    }

    private String getDayName(String dayNum) {

        switch (dayNum) {
            case "1":
                return "Sun";
            case "2":
                return "Mon";
            case "3":
                return "Tue";
            case "4":
                return "Wed";
            case "5":
                return "Thu";
            case "6":
                return "Fri";
            case "7":
                return "Sat";
            default:
                return null;
        }
    }

    public String fixFormat(String _day) {
        String fixday = "";
        String year = _day.substring(0, 4);
        String month = _day.substring(4, 6);
        String day = _day.substring(6, 8);

        fixday = year + "-" + month + "-" + day;

        return fixday;
    }


    @Override
    @Transactional
    public String setExcel(MultipartFile file, String csField, String saveDir, File destFile) throws Exception {
        String result = "";
        ArrayList<ArrayList<String>> excelList = new ArrayList<>();
        ArrayList<String> mapdata;


        try {

            Workbook tempWorkbook;
            if (file.getOriginalFilename().endsWith(".xlsx")) {
                tempWorkbook = new XSSFWorkbook(new FileInputStream(destFile));
            } else if (file.getOriginalFilename().endsWith(".xls")) {
                tempWorkbook = new HSSFWorkbook(new FileInputStream(destFile));
            } else {
                result = "File type Error.";
                return result;
            }

            Sheet sheet = tempWorkbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            int sr;
            if (csField.equals("on,true")) {
                sr = 1;
            } else {
                sr = 0;
            }


            for (int i = sr; i < rows; i++) {
                Row row = sheet.getRow(i);

                if (null != row) {
                    int cells = row.getPhysicalNumberOfCells();
                    mapdata = new ArrayList<>();

                    for (int j = 0; j < cells; j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            String value = "";

                            // 셀 타입에 따라 다른 처리
                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    int intValue = (int) cell.getNumericCellValue();
                                    value = String.valueOf(intValue);

                                    break;
                                case STRING:
                                    value = cell.getStringCellValue();
                                    break;
                            }
                            int chk_val = checkCellValue(value, j, i);

                            if (0 == chk_val) {
                                mapdata.add(value);

                            } else {
                                // 오류 메시지 처리
                                switch (chk_val) {
                                    case -1:
                                        result = "Don't spaces. Error : " + (i + 1);
                                        break;
                                    case -2:
                                        result = "Non-numeric characters are entered in numeric-only data.\nError : " + (i + 1);
                                        break;
                                    case -3:
                                        result = "Usage time is only 00 ~ 23.\nError : " + (i + 1);
                                        break;
                                    default:
                                        result = "Unknown error. Error : " + (i + 1);
                                }
                                return result;
                            }
                        }

                    }
                    excelList.add(mapdata);
                }
            }

            result = insertExcel(excelList);
        } catch (DuplicateKeyException e) {

            result = "이미 존재하는 아이디입니다." + crewInfoVo.getCrew_id();
        } catch (Exception e) {

            result = "Excel 처리 중 오류 발생: " + e.getMessage();
        }

        return result;
    }

    public String insertExcel(ArrayList<ArrayList<String>> compList) throws Exception {
        String resultStr = "Successfully processed.";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < compList.size(); i++) {

            ArrayList<String> mapdata = compList.get(i);

            CrewInfoVo crewInfoVo = new CrewInfoVo();
            crewInfoVo.setS_code(getShipCode(mapdata.get(4)));
            crewInfoVo.setApply_day(fixApplyDay(mapdata.get(15)));


            crewInfoVo.setS_name(mapdata.get(4));
            crewInfoVo.setCrew_name(mapdata.get(2));
            crewInfoVo.setCrew_id(mapdata.get(0));
            crewInfoVo.setAddPw(mapdata.get(1));
            if (mapdata.get(5) == null){
                crewInfoVo.setAmt_total_month("0");
            }else {
                crewInfoVo.setAmt_total_month(mapdata.get(5));
            }
            if (mapdata.get(6) == null){
                crewInfoVo.setLimit_day_amt("0");
            }else {
                crewInfoVo.setLimit_day_amt(mapdata.get(6));
            }
            if (mapdata.get(7) == null){
                crewInfoVo.setLimit_cont_amt("0");
            }else {
                crewInfoVo.setLimit_cont_amt(mapdata.get(7));
            }
            crewInfoVo.setLimit_cont_time(mapdata.get(8));
            crewInfoVo.setTime_from(mapdata.get(9));
            crewInfoVo.setTime_to(mapdata.get(10));
            crewInfoVo.setTime_from2(mapdata.get(11));
            crewInfoVo.setTime_to2(mapdata.get(12));
            crewInfoVo.setTime_from3(mapdata.get(13));
            crewInfoVo.setTime_to3(mapdata.get(14));
            // 날짜 형식으로 변환

            crewInfoVo.setDay_from(mapdata.get(16));
            crewInfoVo.setDay_to(mapdata.get(17));
            crewInfoVo.setRate_name(mapdata.get(18));


            if (mapdata.get(19).equals("허용")){
                crewInfoVo.setWifi_conn("1");
            }else {
                crewInfoVo.setWifi_conn("0");
            }

            if (mapdata.get(20).equals("사용")){
                crewInfoVo.setVal2("Y");
            }else {
                crewInfoVo.setVal2("N");
            }


            String chkCrewId = String.valueOf(CheckCrewId(crewInfoVo));


            if (chkCrewId != null && !chkCrewId.equals("1")) {
                if (null == crewInfoVo.getS_code()) {
                    resultStr = "The ship name in the " + (i + 1) + "th row does not exist.";
                    break;

                } else {

                    if (checkCrewFlag(crewInfoVo) == 1) {
                        // Update
                        int updateResult = crewInfoDao.updateExcelCrewInfo(crewInfoVo);

                        //subData update
                        int checkExcelSub = crewInfoDao.checkExcelSub(crewInfoVo).size();

                        //subData 존재여부에 따라 insert/update

                            int subUpdateResult = -1;
                            if(checkExcelSub > 0){
                                 subUpdateResult = crewInfoDao.updateExcelSubData(crewInfoVo);
                            }else {
                                 subUpdateResult = crewInfoDao.insertExcelSubData(crewInfoVo);
                            }
                            if (subUpdateResult == 0){
                                resultStr = "An error occurred SubData during processing.";
                                break;
                            }


                        //int subUpdateResult = crewInfoDao.
                        if (updateResult == 0) {
                            resultStr = "An error occurred during processing.";
                            break;
                        }
                    } else {
                        // Insert
                        int insertResult = crewInfoDao.insertExcelCrewInfo(crewInfoVo);

                            //subData update
                            int checkExcelSub = crewInfoDao.checkExcelSub(crewInfoVo).size();

                            //subData 존재여부에 따라 insert/update

                            int subUpdateResult = -1;
                            if(checkExcelSub > 0){
                                subUpdateResult = crewInfoDao.updateExcelSubData(crewInfoVo);
                            }else {
                                subUpdateResult = crewInfoDao.insertExcelSubData(crewInfoVo);
                            }
                            if (subUpdateResult == 0){
                                resultStr = "An error occurred SubData during processing.";
                                break;
                            }

                        if (insertResult == 0) {
                            resultStr = "An error occurred during processing.";
                            break;
                        }
                    }
                }
            } else {
                // Update
                int updateResult = crewInfoDao.updateCrewInfoExcel(crewInfoVo);

                        //subData update
                        int checkExcelSub = crewInfoDao.checkExcelSub(crewInfoVo).size();

                        //subData 존재여부에 따라 insert/update

                        int subUpdateResult = -1;
                        if(checkExcelSub > 0){
                            subUpdateResult = crewInfoDao.updateExcelSubData(crewInfoVo);
                        }else {
                            subUpdateResult = crewInfoDao.insertExcelSubData(crewInfoVo);
                        }
                        if (subUpdateResult == 0){
                            resultStr = "An error occurred SubData during processing.";
                            break;
                        }

                if (updateResult == 0) {
                    resultStr = "An error occurred during processing.";
                    break;
                }
            }
        }
        return resultStr;
    }

    @Override
    public List<CrewInfoVo> valList(CrewInfoVo crewInfoVo) throws Exception {
        List<CrewInfoVo> resList = this.crewInfoDao.selectValList(crewInfoVo);
        return resList;
    }

    @Override
    public List<CrewInfoVo> chkScode(CrewInfoVo crewInfoVo) throws Exception {
        List<CrewInfoVo> resList = this.crewInfoDao.chkScode(crewInfoVo);
        return resList;
    }

    @Override
    public List<CrewInfoVo> CheckRateName(CrewInfoVo crewInfoVo) throws Exception {
        List<CrewInfoVo> resList = this.crewInfoDao.checkRateName(crewInfoVo);
        return resList;

    }

    @Override
    public int updateCrewRate(String[] arrayRate) throws Exception {
        return this.crewInfoDao.updateCrewRate(arrayRate);
    }

    @Override
    public int insertCrewRate(String rateData) throws Exception {
        return this.crewInfoDao.insertCrewRate(rateData);
    }

    @Override
    public List<CrewInfoVo> CheckCrewName(CrewInfoVo crewInfoVo) throws Exception {
        List<CrewInfoVo> resList = this.crewInfoDao.checkCrewName(crewInfoVo);
        return resList;
    }

    @Override
    public int updateCrewName(String[] arrayCrew) throws Exception {
        return this.crewInfoDao.updateCrewName(arrayCrew);
    }

    @Override
    public int insertCrewName(String[] arrayCrew) throws Exception {
        return this.crewInfoDao.insertCrewName(arrayCrew);
    }


    private int checkCrewFlag(CrewInfoVo crewInfoVo) {
        String del_flag = this.crewInfoDao.checkCrewFlag(crewInfoVo);
        if (del_flag != null && del_flag.equals("Y")) {
            return 1;
        } else {
            return 0;
        }
    }

    private int CheckCrewId(CrewInfoVo crewInfoVo) {

        List<CrewInfoVo> crewId = this.crewInfoDao.selectCheckCrewId(crewInfoVo);

        if (crewId.size() > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private String getShipCode(String s_name) throws Exception {

        return this.crewInfoDao.getShipCode(s_name);
    }

    private int checkCellValue(String value, int cellIndex, int rowIndex) {

        if (rowIndex == 0) {
            return 0;
        }

        if (value.equals("")) {
            return -1;
        }
        if ((5 <= cellIndex && cellIndex <= 14) || 16 == cellIndex || 17 == cellIndex) {
            if (!Pattern.matches("^[0-9]*$", value)) {
                return -2;
            }
        }
        if (9 <= cellIndex && cellIndex <= 14) {
            if (Integer.parseInt(value) < 0 || 23 < Integer.parseInt(value)) {
                return -3;
            }
        }
        return 0;
    }


    public String fixApplyDay(String day) {
        String fixday = "";
        String[] arrday = day.split(",");

        for (int j = 0; j < arrday.length; j++) {
            String fday = "";
            switch (arrday[j].trim()) {
                case "일":
                    fday = "1";
                    break;
                case "월":
                    fday = "2";
                    break;
                case "화":
                    fday = "3";
                    break;
                case "수":
                    fday = "4";
                    break;
                case "목":
                    fday = "5";
                    break;
                case "금":
                    fday = "6";
                    break;
                case "토":
                    fday = "7";
                    break;
            }
            fixday += (j > 0 ? "," : "") + fday;
        }
        return fixday;
    }

}
