package iwm_ko.wapStat.service.impl;

import iwm_ko.wapStat.dao.WapStatDao;
import iwm_ko.wapStat.model.WapStatVo;
import iwm_ko.wapStat.service.WapStatService;
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

@Service("iwm_ko.wapStat.WapStatService")
//@MapperScan("iwm_ko.wapStat.dao")
public class WapStatServiceImpl implements WapStatService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="iwm_ko.wapStat.wapStatDao")
    private WapStatDao wapStatDao;

    @Override
    public List<WapStatVo> selectCompList(WapStatVo wapStatVo) throws Exception {
        List<WapStatVo> resList = this.wapStatDao.selectCompList(wapStatVo);
        return resList;
    }

    @Override
    public String selectTableName(WapStatVo wapStatVo) throws Exception {


        DateTimeFormatter nonHyphenFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate today = LocalDate.now();
        Calendar calendar = Calendar.getInstance();

        String startDateStr = wapStatVo.getSearchStartDate();
        String endDateStr = wapStatVo.getSearchEndDate();
        String formatStartDate, formatEndDate;

      //검색시 날짜 설정
        if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
            SimpleDateFormat hyphenFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat nonFormat = new SimpleDateFormat("yyyyMMdd");

            Date startDate = hyphenFormat.parse(startDateStr);
            Date endDate = hyphenFormat.parse(endDateStr);

            formatStartDate = nonFormat.format(startDate);
            formatEndDate = nonFormat.format(endDate);

            switch (wapStatVo.getCs_unit()) {
                case "hour":
                    formatStartDate += wapStatVo.getSearchStartHour() + "0000";
                    formatEndDate += wapStatVo.getSearchEndHour() + "5959";
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

        wapStatVo.setSearchStartDate(formatStartDate);
        wapStatVo.setSearchEndDate(formatEndDate);
        return this.wapStatDao.selectTableName(wapStatVo);
    }

    @Override
    public List<WapStatVo> selectWapStatList(WapStatVo wapStatVo) throws Exception {
        List<WapStatVo> resList = this.wapStatDao.selectWapStatList(wapStatVo);

        return resList;
    }

    @Override
    public List<WapStatVo> selectWapStatCount(WapStatVo wapStatVo) throws Exception {
        List<WapStatVo> resList= this.wapStatDao.selectWapStatCount(wapStatVo);
        return resList;
    }

    @Override
    public List<WapStatVo> wapSelectList(WapStatVo wapStatVo) throws Exception {
        List<WapStatVo> resList = this.wapStatDao.selectWapList(wapStatVo);
        return resList;
    }

    @Override
    public List<WapStatVo> shipNameList(String comp_id) throws Exception {
        List<WapStatVo> resList = this.wapStatDao.selectShipNameList(comp_id);
        return resList;
    }


}
