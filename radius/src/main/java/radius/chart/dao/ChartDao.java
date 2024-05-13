package radius.chart.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import radius.chart.model.ChartVo;

@Repository("radius.chart.chartDao")
public class ChartDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "radius.chart.";

	// accessCnt
	public List<ChartVo> selectAccessCntList(ChartVo chartVo) throws Exception {
		List<ChartVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectAccessCntList"), chartVo);
		return resData;
	}
	
	// accCnt
	public List<ChartVo> selectAccCntList(ChartVo chartVo) throws Exception {
		List<ChartVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectAccCntList"), chartVo);
		return resData;
	}

	// accTable
	public List<ChartVo> selectAccTableList(ChartVo chartVo) throws Exception {
		List<ChartVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectAccTableList"), chartVo);
		return resData;
	}
}
