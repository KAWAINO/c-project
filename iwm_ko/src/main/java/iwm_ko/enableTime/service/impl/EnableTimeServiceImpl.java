package iwm_ko.enableTime.service.impl;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import iwm_ko.cuser.model.CuserVo;
import iwm_ko.enableTime.dao.EnableTimeDao;
import iwm_ko.enableTime.model.EnableTimeVo;
import iwm_ko.enableTime.service.EnableTimeService;
import jakarta.annotation.Resource;

@Service("iwm_ko.enableTime.enableTimeService")
public class EnableTimeServiceImpl implements EnableTimeService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="iwm_ko.enableTime.enableTimeDao")
	private EnableTimeDao enableTimeDao;

	@Override
	public List<EnableTimeVo> selectEnableTimeList(EnableTimeVo enableTimeVo) throws Exception {
		
		List<EnableTimeVo> result = this.enableTimeDao.selectEnableTimeList(enableTimeVo); 
		
		for(int i = 0; i < result.size(); i++) {
			
			String from = result.get(i).getFrom_time();
			String to = result.get(i).getTo_time();
			
			String hour = from.substring(0, 2);
			String min = from.substring(2, 4);
			String hour2 = to.substring(0, 2);
			String min2 = to.substring(2, 4);
			
			result.get(i).setFrom_time(hour+":"+min);
			result.get(i).setTo_time(hour2+":"+min2);
		}
		
		return result;
	}
	
	// FROM to To time check
	@Override
	public String getOverlabTime(EnableTimeVo enableTimeVo) throws Exception {
		
		List<EnableTimeVo> chkTime = this.enableTimeDao.chkTime(enableTimeVo);
		
		StringBuilder strBuilder = new StringBuilder();
		
		for(EnableTimeVo item : chkTime) {
			String caseResult = item.getCase_result();
			if(caseResult != null && !caseResult.isEmpty()) {
				strBuilder.append(caseResult).append(", ");
			}
		}
		
		// strBuilder의 길이가 2보다 크다면(","의 길이)
		if(strBuilder.length() > 2) {
			// 마지막의 쉼표와 공백(", ") 제거
			strBuilder.setLength(strBuilder.length() -2);
		}
		return strBuilder.toString();
	}

	// 추가
	@Override
	public int insertTime(EnableTimeVo enableTimeVo) throws Exception {
		return this.enableTimeDao.insertTime(enableTimeVo);
	}

	// 페이징
	@Override
	public int totalEnableTimeList(EnableTimeVo enableTimeVo) throws Exception {
		return this.enableTimeDao.totalEnableTimeList(enableTimeVo);
	}

	// 수정모달이동
	@Override
	public EnableTimeVo enableTimeInfo(HashMap<String, Object> req) throws Exception {

		// 00:00식으로 들어오는 req의 from_time을 0000식으로 변환
		String from_time = (String) req.get("from_time");
		String fmtFrom_time = from_time.replace(":", "");
		req.put("from_time", fmtFrom_time);
		
		EnableTimeVo enableTimeInfo = this.enableTimeDao.enableTimeInfo(req);
	
		String from = enableTimeInfo.getFrom_time();
		String to = enableTimeInfo.getTo_time();
		
		String hour = from.substring(0, 2);
		String min = from.substring(2, 4);
		String hour2 = to.substring(0, 2);
		String min2 = to.substring(2, 4);

		enableTimeInfo.setFromHour(hour);
		enableTimeInfo.setFromMin(min);
		enableTimeInfo.setToHour(hour2);
		enableTimeInfo.setToMin(min2);
		
		return enableTimeInfo;
	}

	// 삭제
	@Override
	public int deleteEnableTime(EnableTimeVo enableTimeVo) throws Exception {
		return this.enableTimeDao.deleteEnableTime(enableTimeVo);
	}


}
