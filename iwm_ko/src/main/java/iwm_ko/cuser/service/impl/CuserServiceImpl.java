package iwm_ko.cuser.service.impl;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import iwm_ko.cuser.dao.CuserDao;
import iwm_ko.cuser.model.CuserVo;
import iwm_ko.cuser.service.CuserService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Service("iwm_ko.cuser.cuserService")
public class CuserServiceImpl implements CuserService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="iwm_ko.cuser.cuserDao")
	private CuserDao cuserDao;
	

	// grade 리스트 조회
	@Override
	public List<CuserVo> selectGradeList(CuserVo cuserVo) throws Exception {
		List<CuserVo> resList = this.cuserDao.selectGradeList(cuserVo);
		return resList;
	}
	
	// comp 리스트 조회
	@Override
	public List<CuserVo> selectCompList(CuserVo cuserVo) throws Exception {
		return this.cuserDao.selectCompList(cuserVo);
	}
	
	// cuser 리스트 조회
	@Override
	public List<CuserVo> selectCuserList(CuserVo cuserVo) throws Exception {
		List<CuserVo> resList = this.cuserDao.selectCuserList(cuserVo);		
		return resList;
	}

	// paging 위한 total 리스트 조회
	@Override
	public int totalCuserList(CuserVo cuserVo) throws Exception {
		return this.cuserDao.totalCuserList(cuserVo);
	}

	// 운영자등록 중복검사
	@Override
	public List<CuserVo> chkInsertCuser(CuserVo cuserVo) throws Exception {
		List<CuserVo> resList = this.cuserDao.chkInsertCuser(cuserVo);
		return resList;
	}
	
	// del_flag 체크
	@Override
	public List<CuserVo> chkDelFlag(CuserVo cuserVo) throws Exception {
		List<CuserVo> resList = this.cuserDao.chkDelFlag(cuserVo);
		return resList;
	}
	
	// 기존 유저 수정
	@Override
	public int changeUser(CuserVo cuserVo) throws Exception {
		return this.cuserDao.changeUser(cuserVo); 
	}

	// iwm 운영자 등록
	@Override
	public int insertCuser(CuserVo cuserVo) throws Exception {
		return this.cuserDao.insertCuser(cuserVo);
	}
	
	// Lock 추가
	@Override
	public int insertFailCnt(CuserVo cuserVo) throws Exception {
		return this.cuserDao.insertFailCnt(cuserVo);
	}
	
	// 수정 modal 이동
	@Override
	public CuserVo selectCuserInfo(HashMap<String, Object> map) throws Exception {
		return this.cuserDao.selectCuserInfo(map);
	}

	// iwm 운영자 수정
	@Override
	public int updateCuser(CuserVo cuserVo) throws Exception {
		return this.cuserDao.updateCuser(cuserVo);
	}

	// iwm 운영자 삭제
	@Override
	public int deleteCuser(CuserVo cuserVo) throws Exception {
		return this.cuserDao.deleteCuser(cuserVo);
	}
	
	// TB_USER_INFO_CENTER_LOCK 정보 삭제
	@Override
	public int deleteLock(CuserVo cuserVo) throws Exception {
		return this.cuserDao.deleteLock(cuserVo);
	}

	// 로그인 실패 횟수 조회
	@Override
	public String getLockCnt() throws Exception {
		return this.cuserDao.getLockCnt();
	}

	// 계정 잠금 해제
	@Override
	public int unLockUser(CuserVo cuserVo) throws Exception {
		return this.cuserDao.unLockUser(cuserVo);
	}
}
