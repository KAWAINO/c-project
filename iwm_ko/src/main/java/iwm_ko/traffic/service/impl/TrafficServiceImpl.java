package iwm_ko.traffic.service.impl;

import iwm_ko.traffic.dao.TrafficDao;
import iwm_ko.traffic.model.TrafficVo;
import iwm_ko.traffic.service.TrafficService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("iwm_ko.traffic.TrafficService")
//@MapperScan("iwm_ko.traffic.dao")
public class TrafficServiceImpl implements TrafficService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name="iwm_ko.traffic.trafficDao")
    private TrafficDao trafficDao;


    @Override
    public List<TrafficVo> selectTrafficList(TrafficVo trafficVo) throws Exception {
        List<TrafficVo> resList = this.trafficDao.selectTrafficList(trafficVo);
        return resList;
    }

    @Override
    public List<TrafficVo> selectCompList(TrafficVo trafficVo) throws Exception {
        List<TrafficVo> resList = this.trafficDao.selectCompList(trafficVo);
        return resList;
    }

    @Override
    public List<TrafficVo> shipNameList(String comp_id) throws Exception {
        List<TrafficVo> resList = this.trafficDao.selectShipNameList(comp_id);
        return resList;
    }

    @Override
    public List<TrafficVo> chkSch(TrafficVo trafficVo) throws Exception {
        List<TrafficVo>resList = this.trafficDao.chkSch(trafficVo);
        return resList;
    }

    @Override
    public int insertTraffic(TrafficVo trafficVo) throws Exception {
        return this.trafficDao.insertTraffic(trafficVo);
    }

    @Override
    public int selectSeq() throws Exception {
        return this.trafficDao.selectSeq();
    }

    @Override
    public int insertTrafficShip(TrafficVo trafficVo) throws Exception {
        return this.trafficDao.insertTrafficShip(trafficVo);

    }

    @Override
    public TrafficVo selectCompInfo(HashMap<String, Object> req) throws Exception {
        return this.trafficDao.selectTrafficInfo(req);
    }

    @Override
    public int updateTraffic(TrafficVo trafficVo) throws Exception {
        return this.trafficDao.updateTraffic(trafficVo);
    }

    @Override
    public int deleteTraffic(TrafficVo trafficVo) throws Exception {
        return this.trafficDao.deleteTraffic(trafficVo);
    }

    @Override
    public int deleteTrafficSub(TrafficVo trafficVo) throws Exception {
        return this.trafficDao.deleteTrafficSub(trafficVo);
    }
}
