package iwm_ko.clog.service;

import java.util.List;
import java.util.Locale;

import iwm_ko.clog.model.ClogVo;

public interface ClogService {
	
	void searchFormatDate(ClogVo clogVo) throws Exception;

	void defaultFormatDate(ClogVo clogVo) throws Exception;

	// menuList 조회
	List<ClogVo> getMenuList(ClogVo clogVo) throws Exception;

	// logList 조회
	List<ClogVo> selectClogList(ClogVo clogVo, Locale locale) throws Exception;

	// 페이징
	int totalClogList(ClogVo clogVo) throws Exception;

}
