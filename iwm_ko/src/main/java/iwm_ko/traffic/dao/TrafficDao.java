package iwm_ko.traffic.dao;

import iwm_ko.traffic.model.TrafficVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

//@Mapper
@Repository("iwm_ko.traffic.trafficDao")
public class TrafficDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.traffic.";


    public List<TrafficVo> selectTrafficList(TrafficVo trafficVo) {
        List<TrafficVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectTrafficList"),trafficVo);
        return resList;
    }

    public List<TrafficVo> selectCompList(TrafficVo trafficVo) throws Exception{
        List<TrafficVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCompList"),trafficVo);
        return resList;
    }

    public List<TrafficVo> selectShipNameList(String comp_id) throws Exception{
        List<TrafficVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipNameList"),comp_id);
        return resList;
    }

    public List<TrafficVo> chkSch(TrafficVo trafficVo) throws Exception{
        List<TrafficVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("chkSch"),trafficVo);
        return resList;
    }

    public int insertTraffic(TrafficVo trafficVo) throws Exception{

        return sqlSession.insert(NAME_SPACE.concat("insertTraffic"),trafficVo);

    }

    public int selectSeq() throws Exception{
        return sqlSession.selectOne(NAME_SPACE.concat("selectSeq"));

    }

    public int insertTrafficShip(TrafficVo trafficVo) throws Exception{
        return sqlSession.insert(NAME_SPACE.concat("insertTrafficShip"),trafficVo);

    }

    public TrafficVo selectTrafficInfo(HashMap<String, Object> req) {
        TrafficVo resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectTrafficInfo"),req    );
        return resData;
    }

    public int updateTraffic(TrafficVo trafficVo) throws Exception{
        return sqlSession.update(NAME_SPACE.concat("updateTraffic"),trafficVo);
    }

    public int deleteTraffic(TrafficVo trafficVo) throws Exception{
        return sqlSession.delete(NAME_SPACE.concat("deleteTraffic"),trafficVo   );

    }

    public int deleteTrafficSub(TrafficVo trafficVo) throws Exception{
        return sqlSession.delete(NAME_SPACE.concat("deleteTrafficSub"),trafficVo);
    }
}
