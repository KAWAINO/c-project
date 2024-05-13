package iwm_ko.sauth.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import iwm_ko.sauth.dao.SauthDao;
import iwm_ko.sauth.model.SauthVo;
import iwm_ko.sauth.service.SauthService;
import jakarta.annotation.Resource;

@Service("iwm_ko.sauth.sauthService")
public class SauthServiceImpl implements SauthService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="iwm_ko.sauth.sauthDao")
	private SauthDao sauthDao;

	// 선박명 조회
	@Override
	public List<SauthVo> selectShipList(SauthVo sauthVo) throws Exception {
		List<SauthVo> resList = this.sauthDao.selectShipList(sauthVo);
		return resList;
	}

	// 리스트 조회
	@Override
	public List<SauthVo> selectSearchList(SauthVo sauthVo) throws Exception {
		List<SauthVo> resList = this.sauthDao.selectSearchList(sauthVo);
		return resList;
	}

	// 업데이트
	@Override
	public int updateGrade(SauthVo sauthVo) throws Exception {
		return this.sauthDao.updateGrade(sauthVo);
	}
}
