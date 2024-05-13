package iwm_ko.enableTime.service;

import java.util.HashMap;
import java.util.List;

import iwm_ko.enableTime.model.EnableTimeVo;

public interface EnableTimeService {

	List<EnableTimeVo> selectEnableTimeList(EnableTimeVo enableTimeVo) throws Exception;

	// FROM to TO time check
	String getOverlabTime(EnableTimeVo enableTimeVo) throws Exception;

	// 추가
	int insertTime(EnableTimeVo enableTimeVo) throws Exception;

	// 페이징
	int totalEnableTimeList(EnableTimeVo enableTimeVo) throws Exception;

	// 수정모달이동
	EnableTimeVo enableTimeInfo(HashMap<String, Object> req) throws Exception;

	// 삭제
	int deleteEnableTime(EnableTimeVo enableTimeVo) throws Exception;

}
