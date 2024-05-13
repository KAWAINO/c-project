package iwm_ko.alarmHis.service;

import java.util.List;

import iwm_ko.alarmHis.model.AlarmHisVo;

public interface AlarmHisService {
	
	// 페이지 로딩 시 검색기간 default 설정
	public void defaultFormatDate(AlarmHisVo alarmHisVo) throws Exception;

	// 검색 시 검색기간 default 설정
	public void searchFormatDate(AlarmHisVo alarmHisVo) throws Exception;

	// 알람원인 리스트 조회
	public List<AlarmHisVo> getAlarmCauseList(AlarmHisVo alarmHisVo) throws Exception;

	// 알람이력 리스트 조회
	public List<AlarmHisVo> getAlarmHisList(AlarmHisVo alarmHisVo) throws Exception;

	// 페이징
	public int totalAlarmHisList(AlarmHisVo alarmHisVo) throws Exception;

}
