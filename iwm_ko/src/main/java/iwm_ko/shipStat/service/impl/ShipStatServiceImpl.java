package iwm_ko.shipStat.service.impl;

import iwm_ko.shipStat.dao.ShipStatDao;
import iwm_ko.shipStat.model.ShipStatVo;
import iwm_ko.shipStat.service.ShipStatService;
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

@Service("iwm_ko.shipStat.ShipStatService")
//@MapperScan("iwm_ko.shipStat.dao")
public class ShipStatServiceImpl implements ShipStatService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="iwm_ko.shipStat.shipStatDao")
    private ShipStatDao shipStatDao;

    @Override
    public List<ShipStatVo> selectShipStatList(ShipStatVo shipStatVo) throws Exception {
        List<ShipStatVo> resList = this.shipStatDao.selectShipStatList(shipStatVo);
        return resList;
    }

    @Override
    public String selectTableName(ShipStatVo shipStatVo) throws Exception {

        DateTimeFormatter nonHyphenFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate today = LocalDate.now();
        Calendar calendar = Calendar.getInstance();

        String startDateStr = shipStatVo.getSearchStartDate();
        String endDateStr = shipStatVo.getSearchEndDate();
        String formatStartDate, formatEndDate;


        if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
            SimpleDateFormat hyphenFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat nonFormat = new SimpleDateFormat("yyyyMMdd");

            Date startDate = hyphenFormat.parse(startDateStr);
            Date endDate = hyphenFormat.parse(endDateStr);

            formatStartDate = nonFormat.format(startDate);
            formatEndDate = nonFormat.format(endDate);

            switch (shipStatVo.getCs_unit()) {
                case "hour":
                    formatStartDate += shipStatVo.getSearchStartHour() + "0000";
                    formatEndDate += shipStatVo.getSearchEndHour() + "5959";
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
            formatStartDate = today.format(nonHyphenFormatter) + "000000";
            formatEndDate = today.format(nonHyphenFormatter) + "235959";
        }

        shipStatVo.setSearchStartDate(formatStartDate);
        shipStatVo.setSearchEndDate(formatEndDate);
        return this.shipStatDao.selectTableName(shipStatVo);
    }


    @Override
    public List<ShipStatVo> selectCompList(ShipStatVo shipStatVo) throws Exception {
        List<ShipStatVo> resList = this.shipStatDao.selectCompList(shipStatVo);
        return resList;
    }

    @Override
    public List<ShipStatVo> selectShipStatCount(ShipStatVo shipStatVo) throws Exception {
        List<ShipStatVo> resList = this.shipStatDao.selectShipStatCount(shipStatVo);
        return resList;
    }

    @Override
    public List<ShipStatVo> shipNameList(String comp_id) throws Exception {
        List<ShipStatVo> resList = this.shipStatDao.selectShipNameList(comp_id);

        return resList;
    }
}
