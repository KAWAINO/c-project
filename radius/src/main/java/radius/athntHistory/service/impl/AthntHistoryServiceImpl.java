package radius.athntHistory.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import radius.athntHistory.dao.AthntHistoryDao;
import radius.athntHistory.model.AthntHistoryVo;
import radius.athntHistory.service.AthntHistoryService;

@Service("radius.athntHistory.athntHistoryService")
//@MapperScan("radius.athntHistory.dao")
public class AthntHistoryServiceImpl implements AthntHistoryService {
	
	@Resource(name = "radius.athntHistory.athntHistoryDao")
	private AthntHistoryDao accessDao;
	
	// 인증 결과 조회
	public List<AthntHistoryVo> selelctReplyList() throws Exception {
		List<AthntHistoryVo> resList = this.accessDao.selelctReplyList();
		return resList;
	}

	// 결과 원인 조회
	public List<AthntHistoryVo> selectCauseList() throws Exception {
		List<AthntHistoryVo> resList = this.accessDao.selectCauseList();
		return resList;
	}
	
	// 인증 이력 리스트 조회
	public List<AthntHistoryVo> selectAthntHistoryList(AthntHistoryVo athntHistoryVo) throws Exception {
		List<AthntHistoryVo> resList = this.accessDao.selectAthntHistoryList(athntHistoryVo);
		return resList;
	}

	
	public int selectAccessCnt(AthntHistoryVo athntHistoryVo) throws Exception {
		int statCnt = this.accessDao.selectAccessCnt(athntHistoryVo);
		return statCnt;
	}

}
