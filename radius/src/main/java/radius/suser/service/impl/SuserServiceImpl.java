package radius.suser.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import radius.suser.dao.SuserDao;
import radius.suser.model.SuserVo;
import radius.suser.service.SuserService;

@Service("radius.suser.suserService")
//@MapperScan("radius.suser.dao")
public class SuserServiceImpl implements SuserService {
	
	@Resource(name="radius.suser.suserDao")
	private SuserDao suserDao;

	// 운영자관리 리스트 조회
	public List<SuserVo> selectSuserList(SuserVo suserVo) throws Exception {
		List<SuserVo> resList = this.suserDao.selectSuserList(suserVo);
		return resList;
	}

	// 페이징
	public List<SuserVo> getTotalSuserList(SuserVo suserVo) throws Exception {
		List<SuserVo> resList = this.suserDao.getTotalSuserList(suserVo);
		return resList;
	}

	// 운영자 등록 중복검사
	public List<SuserVo> chkInsertSuser(SuserVo suserVo) throws Exception {
		List<SuserVo> resList = this.suserDao.chkInsertSuser(suserVo);
		return resList;
	}

	// 운영자 등록
	public int insertSuser(SuserVo suserVo) throws Exception {
		return this.suserDao.insertSuser(suserVo);
	}

	// 수정모달 이동
	public SuserVo selectSuserInfo(HashMap<String, Object> map) throws Exception {
		return this.suserDao.selectSuserInfo(map);
	}

	// 운영자 수정
	public int updateSuser(SuserVo suserVo) throws Exception {
		return this.suserDao.updateSuser(suserVo);
	}

	// 운영자 삭제
	public int deleteSuser(SuserVo suserVo) throws Exception {
		return this.suserDao.deleteSuser(suserVo);
	}

}
