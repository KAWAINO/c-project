package iwm_ko.crewStat.service.impl;

import iwm_ko.crewStat.dao.CrewStatDao;
import iwm_ko.crewStat.model.CrewStatVo;
import iwm_ko.crewStat.service.CrewStatService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("iwm_ko.crewStat.CrewStatService")
//@MapperScan("iwm_ko.crewStat.dao")
public class CrewStatServiceImpl implements CrewStatService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name="iwm_ko.crewStat.crewStatDao")
    private CrewStatDao crewStatDao;

    @Override
    public List<CrewStatVo> selectCompList(CrewStatVo crewStatVo) throws Exception {
        List<CrewStatVo> resList = this.crewStatDao.selectCompList(crewStatVo);
        return resList;
    }

    @Override
    public String selectTableName(CrewStatVo crewStatVo) throws Exception {
        DateTimeFormatter nonHyphenFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate today = LocalDate.now();
        Calendar calendar = Calendar.getInstance();

        String startDateStr = crewStatVo.getSearchStartDate();
        String endDateStr = crewStatVo.getSearchEndDate();
        String formatStartDate, formatEndDate;

        //검색시 날짜 설정
        if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
            SimpleDateFormat hyphenFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat nonFormat = new SimpleDateFormat("yyyyMMdd");

            Date startDate = hyphenFormat.parse(startDateStr);
            Date endDate = hyphenFormat.parse(endDateStr);

            formatStartDate = nonFormat.format(startDate);
            formatEndDate = nonFormat.format(endDate);

            switch (crewStatVo.getCs_unit()) {
                case "hour":
                    formatStartDate += crewStatVo.getSearchStartHour() + "0000";
                    formatEndDate += crewStatVo.getSearchEndHour() + "5959";
                    break;
                case "day":
                    formatStartDate += "000000";
                    formatEndDate += "235959";
                    break;
                case "week":
                    calendar.setTime(startDate);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    formatStartDate = nonFormat.format(calendar.getTime()) + "000000";

                    calendar.setTime(endDate);
                    calendar.add(Calendar.DAY_OF_WEEK, 6);
                    formatEndDate = nonFormat.format(calendar.getTime()) + "235959";
                    break;
                case "month":
                    calendar.setTime(startDate);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    formatStartDate = nonFormat.format(calendar.getTime()) + "000000";

                    calendar.setTime(endDate);
                    calendar.add(Calendar.MONTH, 1);
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    formatEndDate = nonFormat.format(calendar.getTime()) + "235959";
                    break;
            }
        } else {
            //페이지 첫 접속시 기본 날짜
            formatStartDate = today.format(nonHyphenFormatter) + "000000";
            formatEndDate = today.format(nonHyphenFormatter) + "235959";
        }

        crewStatVo.setSearchStartDate(formatStartDate);
        crewStatVo.setSearchEndDate(formatEndDate);
        return this.crewStatDao.selectTableName(crewStatVo);
    }

    @Override
    public List<CrewStatVo> selectCrewStatList(CrewStatVo crewStatVo) throws Exception {
        List<CrewStatVo> resList = this.crewStatDao.selectCrewStatList(crewStatVo);
        return resList;
    }

    @Override
    public List<CrewStatVo> selectCrewStatCount(CrewStatVo crewStatVo) throws Exception {
       List<CrewStatVo> resList = this.crewStatDao.selectCrewStatCount(crewStatVo);
       return resList;
    }

    @Override
    public List<CrewStatVo> crewSelectList(CrewStatVo crewStatVo) throws Exception {
        List<CrewStatVo> resList = this.crewStatDao.crewSelectList(crewStatVo);

        return resList;
    }

    @Override
    public List<CrewStatVo> shipNameList(String comp_id) throws Exception {
        List<CrewStatVo> resList = this.crewStatDao.selectShipNameList(comp_id);
        return resList;
    }


}
