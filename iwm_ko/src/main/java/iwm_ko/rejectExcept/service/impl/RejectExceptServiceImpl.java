package iwm_ko.rejectExcept.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import iwm_ko.cuser.model.CuserVo;
import iwm_ko.rejectExcept.dao.RejectExceptDao;
import iwm_ko.rejectExcept.model.RejectExceptVo;
import iwm_ko.rejectExcept.service.RejectExceptService;
import jakarta.annotation.Resource;

@Service("iwm_ko.rejectExcept.rejectExceptService")
public class RejectExceptServiceImpl implements RejectExceptService {

	@Resource(name="iwm_ko.rejectExcept.rejectExceptDao")
	private RejectExceptDao rejectExceptDao;

	@Override
	public List<RejectExceptVo> selectRejectExceptList(RejectExceptVo rejectExceptVo) throws Exception {
		return this.rejectExceptDao.selectRejectExcepList(rejectExceptVo);
	}

	// 페이징
	@Override
	public int totalRejectExceptList(RejectExceptVo rejectExceptVo) throws Exception {
		return this.rejectExceptDao.totalRejectExceptList(rejectExceptVo);
	}

	// chkMac
	@Override
	public List<CuserVo> chkMac(RejectExceptVo rejectExceptVo) throws Exception {
		return this.rejectExceptDao.chkMac(rejectExceptVo);
	}

	// 등록
	@Override
	public int insertAuth(RejectExceptVo rejectExceptVo) throws Exception {
		return this.rejectExceptDao.insertAuth(rejectExceptVo);
	}

	// 수정모달
	@Override
	public RejectExceptVo selectRejectExceptInfo(HashMap<String, Object> map) throws Exception {
		return this.rejectExceptDao.selectRejectExceptInfo(map);
	}

	// 수정
	@Override
	public int updateAuth(RejectExceptVo rejectExceptVo) throws Exception {
		return this.rejectExceptDao.updateAuth(rejectExceptVo);
	}

	// 삭제
	@Override
	public int deleteAuth(RejectExceptVo rejectExceptVo) throws Exception {
		return this.rejectExceptDao.deleteAuth(rejectExceptVo);
	}
}
