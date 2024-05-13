package iwm_ko.apinfo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import iwm_ko.apinfo.dao.ApInfoDao;
import iwm_ko.apinfo.model.ApInfoVo;
import iwm_ko.apinfo.service.ApInfoService;
import jakarta.annotation.Resource;

@Service("iwm_ko.apinfo.apInfoService")
public class ApInfoServiceImpl implements ApInfoService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;

	@Resource(name="iwm_ko.apinfo.apInfoDao")
	private ApInfoDao apInfoDao;

	// 대역폭 리스트 조회
	@Override
	public List<ApInfoVo> selectBandList(ApInfoVo apInfoVo) throws Exception {
		List<ApInfoVo> resList = this.apInfoDao.selectBandList(apInfoVo);
		return resList;
	}

	// apinfo 리스트 조회
	@Override
	public List<ApInfoVo> selectApInfoList(ApInfoVo apInfoVo, Locale locale) throws Exception {
		List<ApInfoVo> apInfoList = this.apInfoDao.selectApInfoList(apInfoVo, locale);	
		
		// conn_time 시간 포맷 설정
		for(int i = 0; i < apInfoList.size(); i++) {
			String time = "";
			String connTime = apInfoList.get(i).getConn_time();
			
			if(connTime != null ) {	
				if(connTime.equals("50000") || connTime.equals("0") || connTime == "null") {
					time = messageSource.getMessage("list.noLimit", null, locale);
				} else if(Integer.parseInt(connTime) % 60 == 0) {
					int hour = Integer.parseInt(connTime) / 60;
					time = String.valueOf(hour) + messageSource.getMessage("list.hourly", null, locale);
				} else if(Integer.parseInt(connTime) >= 60) {
					int hour = Integer.parseInt(connTime) / 60;
					int min = Integer.parseInt(connTime) % 60;
					time = String.valueOf(hour) 
							+ messageSource.getMessage("list.hourly", null, locale) 
							+ " " + String.valueOf(min) + messageSource.getMessage("list.minutes", null, locale) ;
				} else {
					time = connTime + messageSource.getMessage("list.minutes", null, locale) ;
				}
			} else {
				time = messageSource.getMessage("list.noLimit", null, locale);
			}
			apInfoList.get(i).setConn_time(time);
			
			String fwVersion = apInfoList.get(i).getFw_version();
			
			if(fwVersion == null || fwVersion.equals("null")) {
				apInfoList.get(i).setFw_version("0");
			}
		}
		return apInfoList;
	}

	@Override
	public int totalApInfoList(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.totalApInfoList(apInfoVo);
	}
	
	@Override
	public List<ApInfoVo> selectShipList(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.selectShipList(apInfoVo);
	}

	// mac 중복검사
	@Override
	public List<ApInfoVo> chkMac(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.chkMac(apInfoVo);
	}

	// ap_name 중복검사
	@Override
	public List<ApInfoVo> chkName(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.chkName(apInfoVo);
	}
	
	// flag 체크
	@Override
	public List<ApInfoVo> chkFlag(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.chkFlag(apInfoVo);
	}

	// 기존 ap 데이터 수정
	@Override
	public int changeAp(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.changeAp(apInfoVo);
	}

	// apinfo 신규등록
	@Override
	public int insertApInfo(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.insertApInfo(apInfoVo);
	}

	// 수정모달
	@Override
	public ApInfoVo selectApInfo(HashMap<String, Object> map) throws Exception {
		return this.apInfoDao.selectApInfo(map);
	}
	
	@Override
	public void connTimeFormat(ApInfoVo apInfoVo) throws Exception {

		// conn_time hour, min 설정
		String time = apInfoVo.getConn_time();
		String str1 = "";
		String str2 = "";
		
		if(time == null || Integer.parseInt(time) == 50000) {
			str1 = "";
		} else {
			str1 = String.valueOf(Integer.parseInt(time) / 60);
		}
		if(time == null || Integer.parseInt(time) == 50000) {
			str2 = "";
		} else {
			str2 = String.valueOf(Integer.parseInt(time) % 60);
		}

		apInfoVo.setUpdHour(str1);
		apInfoVo.setUpdMin(str2);
		
	}

	// sub data 중복검사
	@Override
	public List<ApInfoVo> chkSubData(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.chkSubData(apInfoVo);
	}

	// 기존 data 수정
	@Override
	public int changeApSub(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.changeApSub(apInfoVo);
	}

	// subdata 신규등록
	@Override
	public int insertApSub(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.insertApSub(apInfoVo);
	}

	// update
	@Override
	public int updateApInfo(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.updateApInfo(apInfoVo);
	}
	
	// subdata 중복검사2
	@Override
	public List<ApInfoVo> chkSubData2(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.chkSubData2(apInfoVo);
	}
	
	@Override
	public int updateApSubY(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.updateApSubY(apInfoVo);
	}

	@Override
	public int insertApSubY(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.insertApSubY(apInfoVo);
	}
	
	@Override
	public int updateApSubN(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.updateApSubN(apInfoVo);
	}

	@Override
	public int insertApSubN(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.insertApSubN(apInfoVo);
	}
	
	// 삭제
	@Override
	public int deleteApInfo(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.deleteApInfo(apInfoVo);
	}

	@Override
	public int reboot(ApInfoVo apInfoVo) throws Exception {	
		
//		logger.info("1. vResult :::: " + apInfoVo.getVResult());
		
		apInfoVo.setPFlag("8");
		apInfoVo.setPTermMac("000000000000");
		apInfoVo.setPDiscReason("0");
		
		this.apInfoDao.reboot(apInfoVo); // MyBatis를 통한 저장 프로시저 호출
		
		logger.info("Service vResult: " + apInfoVo.getVResult());

	    // 저장 프로시저 호출 후 vResult 필드에서 결과 값 가져오기
	    return apInfoVo.getVResult(); 
	}

	@Override
	public List<String> updChkName(ApInfoVo apInfoVo) throws Exception {
		return this.apInfoDao.updChkName(apInfoVo);
	}









}
