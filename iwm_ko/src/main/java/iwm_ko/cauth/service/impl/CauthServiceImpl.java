package iwm_ko.cauth.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import iwm_ko.cauth.dao.CauthDao;
import iwm_ko.cauth.model.CauthVo;
import iwm_ko.cauth.service.CauthService;
import jakarta.annotation.Resource;

@Service("iwm_ko.cauth.cauthService")
public class CauthServiceImpl implements CauthService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="iwm_ko.cauth.cauthDao")
	private CauthDao cauthDao;

	// 등급별 리스트 조회
	@Override
	public List<CauthVo> selectSearchList(CauthVo cauthVo) throws Exception {
		
		List<CauthVo> resList = this.cauthDao.selectSearchList(cauthVo);
		
		for(int i = 0; i < resList.size(); i++) {
			
			if(resList.get(i).getGui_code().equals("301")) {
				logger.info("gui_name ::::: " + resList.get(i).getGui_code() + " | " + resList.get(i).getGui_name());
				resList.remove(i);
			}  
			
			if(resList.get(i).getGui_code().equals("302")) {
				logger.info("gui_name ::::: " + resList.get(i).getGui_code() + " | " + resList.get(i).getGui_name());
				resList.remove(i);
			}
			
			if(resList.get(i).getGui_code().equals("303")) {
				logger.info("gui_name ::::: " + resList.get(i).getGui_code() + " | " + resList.get(i).getGui_name());
				resList.remove(i);
			}
			
			if(resList.get(i).getGui_code().equals("304")) {
				logger.info("gui_name ::::: " + resList.get(i).getGui_code() + " | " + resList.get(i).getGui_name());
				resList.remove(i);
			}
			
			if(resList.get(i).getGui_code().equals("305")) {
				logger.info("gui_name ::::: " + resList.get(i).getGui_code() + " | " + resList.get(i).getGui_name());
				resList.remove(i);
			}
		}
		return resList;
	}

	// 리스트 카운트
	@Override
	public List<CauthVo> listCnt(CauthVo cauthVo) throws Exception {
		List<CauthVo> resList = this.cauthDao.listCnt(cauthVo);
		return resList;
	}
	
	// 등급별 권한 변경
	@Override
	public int updateGrade(CauthVo cauthVo) throws Exception {
		return this.cauthDao.updateGrade(cauthVo);
	}

}
