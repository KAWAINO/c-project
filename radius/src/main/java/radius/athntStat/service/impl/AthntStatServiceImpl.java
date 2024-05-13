package radius.athntStat.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import radius.athntStat.dao.AthntStatDao;
import radius.athntStat.model.AthntStatVo;
import radius.athntStat.service.AthntStatService;


@Service("radius.athntStat.athntStatService")
//@MapperScan("radius.athntStat.dao")
public class AthntStatServiceImpl implements AthntStatService {

	@Resource(name = "radius.athntStat.athntStatDao")
	private AthntStatDao athntStatDao;
	
	// 테이블명 검색
	public String selectTableName(AthntStatVo athntStatVo) throws Exception {
		String tableName = this.athntStatDao.selectTableName(athntStatVo);
		return tableName;
	}
	
	// 
	public String selectStatWeek(String startDate) throws Exception {
		String week = this.athntStatDao.selectStatWeek(startDate);
		return week;
	}

	// 인증 결과 조회
	public List<AthntStatVo> selelctReplyList(AthntStatVo athntStatVo) throws Exception {
		List<AthntStatVo> resList = this.athntStatDao.selelctReplyList(athntStatVo);
		return resList;
	}

	// 결과 원인 조회
	public List<AthntStatVo> selectCauseList(AthntStatVo athntStatVo) throws Exception {
		List<AthntStatVo> resList = this.athntStatDao.selectCauseList(athntStatVo);
		return resList;
	}

	// statList 조회
	public List<AthntStatVo> selectStatList(AthntStatVo athntStatVo) throws Exception {
		List<AthntStatVo> resList = this.athntStatDao.selectStatList(athntStatVo);
		return resList;
	}

	//
	public int selectStatCnt(AthntStatVo athntStatVo) throws Exception {
		int statCnt = this.athntStatDao.selectStatCnt(athntStatVo);
		return statCnt;
	}

}
