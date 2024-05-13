package radius.chart.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import radius.chart.dao.ChartDao;
import radius.chart.model.ChartVo;
import radius.chart.service.ChartService;

@Service("radius.chart.chartService")
//@MapperScan("radius.chart.dao")
public class ChartServiceImpl implements ChartService {

	@Resource(name = "radius.chart.chartDao")
	private ChartDao chartDao;

	// accessCnt
	@Override
	public List<ChartVo> selectAccessCntList(ChartVo chartVo) throws Exception {
		List<ChartVo> resList = this.chartDao.selectAccessCntList(chartVo);
		return resList;
	}
	
	// accCnt
	@Override
	public List<ChartVo> selectAccCntList(ChartVo chartVo) throws Exception {
		List<ChartVo> resList = this.chartDao.selectAccCntList(chartVo);
		return resList;
	}
	
	// accTable
	@Override
	public List<ChartVo> selectAccTableList(ChartVo chartVo) throws Exception {
		List<ChartVo> resList = this.chartDao.selectAccTableList(chartVo);
		return resList;
	}

}