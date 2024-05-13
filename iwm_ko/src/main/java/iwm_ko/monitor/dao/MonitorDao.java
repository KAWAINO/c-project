package iwm_ko.monitor.dao;

import iwm_ko.monitor.model.MonitorVo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper
@Repository("iwm_ko.monitor.monitorDao")
public class MonitorDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.monitor.";

	public List<MonitorVo> getCompList(MonitorVo monitorVo) throws Exception{
		return this.sqlSession.selectList(NAME_SPACE.concat("getCompList"), monitorVo);
	}
    
//    public List<MonitorVo> selectMenuList(MonitorVo monitorVo) throws Exception{
//        List<MonitorVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectMenuList"),monitorVo);
//        return resList;
//    }

	public List<MonitorVo> getShipList(MonitorVo monitorVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("getShipList"), monitorVo);
	}

	public String getTable(MonitorVo monitorVo) throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("getTable"), monitorVo);
	}

	public List<MonitorVo> getTimeList(MonitorVo monitorVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("getTimeList"), monitorVo);
	}

	public List<MonitorVo> getDataList(MonitorVo monitorVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("getDataList"), monitorVo);
	}

	public List<MonitorVo> getDataTimeList(MonitorVo monitorVo) throws Exception{
		return this.sqlSession.selectList(NAME_SPACE.concat("getDataTimeList"), monitorVo);
	}

	public String getShipName(MonitorVo monitorVo) throws Exception{
		return this.sqlSession.selectOne(NAME_SPACE.concat("getShipName"), monitorVo);
	}

}
