package iwm_ko.slog.service;

import java.util.List;
import java.util.Locale;

import iwm_ko.slog.model.SlogVo;

public interface SlogService {
	
	void searchFormatDate(SlogVo slogVo) throws Exception;

	void defaultFormatDate(SlogVo slogVo) throws Exception;

	// 메뉴리스트 조회
	List<SlogVo> getMenuList(SlogVo slogVo) throws Exception;
	
	// 선박명 조회
	List<SlogVo> selectShipList(SlogVo slogVo) throws Exception;

	// 로그리스트 조회
	List<SlogVo> selectSlogList(SlogVo slogVo, Locale locale) throws Exception;

	// 페이징
	int totalSlogList(SlogVo slogVo) throws Exception;



}
