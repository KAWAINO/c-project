package iwm_ko.traffic.service;

import iwm_ko.traffic.model.TrafficVo;

import java.util.HashMap;
import java.util.List;

public interface TrafficService {
public List<TrafficVo> selectTrafficList(TrafficVo trafficVo) throws Exception;

    public List<TrafficVo> selectCompList(TrafficVo trafficVo) throws Exception;

    public List<TrafficVo> shipNameList(String comp_id) throws Exception;

    public List<TrafficVo> chkSch(TrafficVo trafficVo) throws  Exception;

    public int insertTraffic(TrafficVo trafficVo) throws Exception;

    int selectSeq() throws Exception;

    int insertTrafficShip(TrafficVo trafficVo)throws Exception;

    public TrafficVo selectCompInfo(HashMap<String, Object> req) throws Exception;

    public int updateTraffic(TrafficVo trafficVo) throws Exception;

    public int deleteTraffic(TrafficVo trafficVo) throws Exception;

    public int deleteTrafficSub(TrafficVo trafficVo) throws Exception;
}
