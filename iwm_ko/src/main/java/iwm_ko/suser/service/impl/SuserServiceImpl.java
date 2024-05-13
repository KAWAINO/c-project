package iwm_ko.suser.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import iwm_ko.cuser.model.CuserVo;
import iwm_ko.suser.dao.SuserDao;
import iwm_ko.suser.model.SuserVo;
import iwm_ko.suser.service.SuserService;
import jakarta.annotation.Resource;

@Service("iwm_ko.suser.suserService")
public class SuserServiceImpl implements SuserService{

	@Resource(name="iwm_ko.suser.suserDao")
	private SuserDao suserDao;
	
	// 등급조회
	@Override
	public List<SuserVo> selectGradeList(SuserVo suserVo) throws Exception {
		List<SuserVo> resList = this.suserDao.selectGradeList(suserVo);
		return resList;
	}

	// 유저 리스트 조회
	@Override
	public List<SuserVo> selectSuserList(SuserVo suserVo) throws Exception {
		List<SuserVo> resList = this.suserDao.selectSuserList(suserVo);
		return resList;
	}

	// 페이징
	@Override
	public int totalSuserList(SuserVo suserVo) throws Exception {
		return this.suserDao.totalSuserList(suserVo);
	}

	// 중복 검사
	@Override
	public List<SuserVo> chkInsertSuser(SuserVo suserVo) throws Exception {
		List<SuserVo> resList = this.suserDao.chkInsertSuser(suserVo);
		return resList;
	}

	// del_flag 체크
	@Override
	public List<SuserVo> chkDelFlag(SuserVo suserVo) throws Exception {
		List<SuserVo> resList = this.suserDao.chkDelFlag(suserVo);
		return resList;
	}

	// dle_flag가 Y이면 기존 유저 수정
	@Override
	public int changeUser(SuserVo suserVo) throws Exception {
		return this.suserDao.changeUser(suserVo);
	}

	// 등록
	@Override
	public int insertSuser(SuserVo suserVo) throws Exception {
		return this.suserDao.insertSuser(suserVo);
	}

	// 수정모달
	@Override
	public SuserVo selectSuserInfo(HashMap<String, Object> map) throws Exception {
		return this.suserDao.selectSuserInfo(map);
	}

	// 수정
	@Override
	public int updateSuser(SuserVo suserVo) throws Exception {
		return this.suserDao.updateSuser(suserVo);
	}

	// 삭제
	@Override
	public int deleteSuser(SuserVo suserVo) throws Exception {
		return this.suserDao.deleteSuser(suserVo);
	}


}
