package radius.chart.service;

import java.util.List;

import radius.chart.model.ChartVo;

public interface ChartService {

	/**
	 * chartCntList
	 * @param chartVo
	 * @return
	 * @throws Exception
	 */
	List<ChartVo> selectAccessCntList(ChartVo chartVo) throws Exception;
	
	/**
	 * 
	 * @param chartVo
	 * @return
	 * @throws Exception
	 */
	List<ChartVo> selectAccCntList(ChartVo chartVo) throws Exception;
	
	/**
	 * 
	 * @param chartVo
	 * @return
	 * @throws Exception
	 */
	List<ChartVo> selectAccTableList(ChartVo chartVo) throws Exception;

}
