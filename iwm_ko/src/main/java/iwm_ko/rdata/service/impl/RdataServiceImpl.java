package iwm_ko.rdata.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import iwm_ko.rdata.dao.RdataDao;
import iwm_ko.rdata.model.RdataVo;
import iwm_ko.rdata.service.RdataService;
import jakarta.annotation.Resource;

@Service("iwm_ko.rdata.RdataService")
//@MapperScan("iwm_ko.rdata.dao")
public class RdataServiceImpl  implements RdataService {
	
	@Autowired
	private MessageSource messageSource;

    @Resource(name="iwm_ko.rdata.rdataDao")
    private RdataDao rdataDao;

    @Override
    public List<RdataVo> selectRdataList(RdataVo rdataVo, Locale locale) throws Exception {
        List<RdataVo> resList = this.rdataDao.selectRdataList(rdataVo, locale);
        return resList;
    }

    @Override
    public List<RdataVo> selectRdataCount(RdataVo rdataVo) throws Exception {
        List<RdataVo> resList = this.rdataDao.selectRdataCount(rdataVo);
        return resList;

    }

    @Override
    public List<RdataVo> selectShipCompList(RdataVo rdataVo) throws Exception {
        List<RdataVo> resList = this.rdataDao.selectShipCompList(rdataVo);
        return resList;
    }


    @Override
    public List<RdataVo> shipNameList(String compId) throws Exception {
        List<RdataVo> resList = this.rdataDao.selectShipNameList(compId);
        return resList;
    }

    @Override
    public List<RdataVo> rdataList(String s_name) throws Exception {
        List<RdataVo> resList = this.rdataDao.selectRateNameList(s_name);
        return resList;
    }

    @Override
    public List<RdataVo> chkRdata(RdataVo rdataVo) throws Exception {
        List<RdataVo> resData = this.rdataDao.chkRdata(rdataVo);
        return resData;
    }

    @Override
    public int insertRdata(RdataVo rdataVo) throws Exception {
        return this.rdataDao.insertRdata(rdataVo);
    }


    @Override
    public RdataVo selectRdataInfo(HashMap<String, Object> map) throws Exception {
        return this.rdataDao.selectRdataInfo(map);
    }


    @Override
    public int updateRdata(RdataVo rdataVo) throws Exception {
        return this.rdataDao.updateRdata(rdataVo);
    }

    @Override
    public int deleteRdata(RdataVo rdataVo) throws Exception {
        return this.rdataDao.deleteRdata(rdataVo);
    }

    @Override
    public String updateHistory(RdataVo rdataVo) throws Exception {

        RdataVo vo = rdataVo;
        String history = "Data usage policy : " + vo.getRate_name() + ", ";

        if (!vo.getAmt_total_month().equals(vo.getUpdMonth())) {
            history += "Monthly supply(MB) : (Before) " + vo.getAmt_total_month() + " / (After) " + vo.getUpdMonth() + ", ";
        }

        if (!vo.getLimit_day_amt().equals(vo.getUpdDay())) {
            history += "Daily use limit(MB) : (Before) " + vo.getLimit_day_amt() + " / (After) " + vo.getUpdDay() + ", ";
        }

        if (!vo.getLimit_cont_amt().equals(vo.getUpdUse())) {
            history += "Continuous use limit(MB) : (Before) " + vo.getLimit_cont_amt() + " / (After) " + vo.getUpdUse() + ", ";
        }

        if (!vo.getLimit_cont_time().equals(vo.getUpdMin())) {
            history += "Continuous use timeout(min) : (Before) " + vo.getLimit_cont_time() + " / (After) " + vo.getUpdMin() + ", ";
        }

        if (!vo.getTime_from().equals(vo.getUpdTimeFrom())) {
            history += "Start of Usage time limit range : (Before) " + vo.getTime_from() + "hour / (After) " + vo.getUpdTimeFrom() + "hour, ";
        }

        if (!vo.getTime_to().equals(vo.getUpdTimeTo())) {
            history += "End of Usage time limit range : (Before) " + vo.getTime_to() + "hour / (After) " + vo.getUpdTimeTo() + "hour, ";
        }

        if (!vo.getTime_from2().equals(vo.getUpdTimeFrom2())) {
            history += "Start of Usage time limit range2 : (Before) " + vo.getTime_from2() + "hour / (After) " + vo.getUpdTimeFrom2() + "hour, ";
        }

        if (!vo.getTime_to2().equals(vo.getUpdTimeTo2())) {
            history += "End of Usage time limit range2 : (Before) " + vo.getTime_to2() + "hour / (After) " + vo.getUpdTimeTo2() + "hour, ";
        }

        if (!vo.getTime_from3().equals(vo.getUpdTimeFrom3())) {
            history += "Start of Usage time limit range3 : (Before) " + vo.getTime_from3() + "hour / (After) " + vo.getUpdTimeFrom3() + "hour, ";
        }

        if (!vo.getTime_to3().equals(vo.getUpdTimeTo3())) {
            history += "End of Usage time limit range3 : (Before) " + vo.getTime_to3() + "hour / (After) " + vo.getUpdTimeTo3() + "hour, ";
        }


        String preApplyDays = formatApplyDays(vo.getApply_day());
        String updApplyDays = formatApplyDays(vo.getUpdApplyDays());
        if (!preApplyDays.equals(updApplyDays)) {
            history += "Day of use: (Before) " + preApplyDays + " / (After) " + updApplyDays + ", ";
        }

        if(!vo.getUpdDayFrom().equals(vo.getDay_from())){
            String from=fixFormat(vo.getUpdDayFrom());
            String pre_from=fixFormat(vo.getDay_from());

            history+="Start of Period of use : (Before) "+pre_from+" / (After) "+from+", ";
        }

        if(!vo.getUpdDayTo().equals(vo.getDay_to())){
            String to=fixFormat(vo.getUpdDayTo());
            String pre_to=fixFormat(vo.getDay_to());

            history+="End of Period of use : (Before) "+pre_to+" / (After) "+to+", ";
        }

        history=history.substring(0, history.length()-2);



        return history;
    }

    @Override
    public List<RdataVo> crewIdList(String s_name) throws Exception {
        List<RdataVo> resList = this.rdataDao.selectCrewIdList(s_name);
        return resList;
    }

    @Override
    public String chkDel(RdataVo rdataVo) throws Exception {
        return this.rdataDao.chkDel(rdataVo);
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
            case "1": return "Sun";
            case "2": return "Mon";
            case "3": return "Tue";
            case "4": return "Wed";
            case "5": return "Thu";
            case "6": return "Fri";
            case "7": return "Sat";
            default: return null;
        }
    }

    public String fixFormat(String _day){
        String fixday="";
        String year=_day.substring(0,4);
        String month=_day.substring(4,6);
        String day=_day.substring(6,8);

        fixday=year+"-"+month+"-"+day;

        return fixday;
    }
}
