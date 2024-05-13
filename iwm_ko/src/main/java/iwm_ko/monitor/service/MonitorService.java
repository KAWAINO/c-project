package iwm_ko.monitor.service;

import iwm_ko.monitor.model.MonitorVo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface MonitorService {

    // 선주사 리스트 조회
	public List<MonitorVo> getCompList(MonitorVo monitorVo) throws Exception;

//    public List<MonitorVo> selectMenuList(MonitorVo monitorVo)throws Exception;

    // 선박명 조회
	public List<MonitorVo> getShipList(MonitorVo monitorVo) throws Exception;

	// 최초 로딩 시 디폴드 날짜 설정
	public void defaultFormatDate(MonitorVo monitorVo) throws Exception;

	// 테이블 조회
	public String getTable(MonitorVo monitorVo) throws Exception;

	// getTimeList
	public List<MonitorVo> getTimeList(MonitorVo monitorVo, String day, String hour) throws Exception;

	// getDataList
	public List<MonitorVo> getDataList(MonitorVo monitorVo) throws Exception;

	// getDataTimeList
	public List<MonitorVo> getDataTimeList(MonitorVo monitorVo) throws Exception;

	// 선박명 조회
	public String getShipName(MonitorVo monitorVo) throws Exception;

	// 검색조 조건별 날짜 포맷
	public void processDateFormat(MonitorVo monitorVo) throws Exception;
	
	// 바그래프 전월 데이터 조회
	public void lastShipChartDateFormat(MonitorVo monitorVo) throws Exception;
	
	// 바그래프 금월데이터 조회
	public void thisShipChartDateFormat(MonitorVo monitorVo) throws Exception;



	
}
