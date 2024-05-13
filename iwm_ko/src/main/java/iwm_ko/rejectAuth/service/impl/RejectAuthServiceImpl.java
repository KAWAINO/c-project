package iwm_ko.rejectAuth.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import iwm_ko.rejectAuth.dao.RejectAuthDao;
import iwm_ko.rejectAuth.model.RejectAuthVo;
import iwm_ko.rejectAuth.service.RejectAuthService;
import jakarta.annotation.Resource;

@Service("iwm_ko.rejectAuth.rejectAuthService")
public class RejectAuthServiceImpl implements RejectAuthService {

	@Resource(name="iwm_ko.rejectAuth.rejectAuthDao")
	private RejectAuthDao rejectAuthDao;

	@Override
	public List<RejectAuthVo> selectRejectAuthList(RejectAuthVo rejectAuthVo) throws Exception {
		return this.rejectAuthDao.selectRejectAuthList(rejectAuthVo);
	}

	// 페이징
	@Override
	public int totalRejectAuthList(RejectAuthVo rejectAuthVo) throws Exception {
		return this.rejectAuthDao.totalRejectAuthList(rejectAuthVo);
	}

	// chkMac
	@Override
	public List<RejectAuthVo> chkMac(RejectAuthVo rejectAuthVo) throws Exception {
		return this.rejectAuthDao.chkMac(rejectAuthVo);
	}

	// chgAuth
	@Override
	public int insertAuth(RejectAuthVo rejectAuthVo) throws Exception {
		return this.rejectAuthDao.insertAuth(rejectAuthVo);
	}

	// 수정모달
	@Override
	public RejectAuthVo selectRejectAuthInfo(HashMap<String, Object> map) throws Exception {
		return this.rejectAuthDao.selectRejectAuthInfo(map);
	}

	// 수정
	@Override
	public int updateRejectAuth(RejectAuthVo rejectAuthVo) throws Exception {
		return this.rejectAuthDao.updateRejectAuth(rejectAuthVo);
	}

	// 삭제
	@Override
	public int deleteRejectAuth(RejectAuthVo rejectAuthVo) throws Exception {
		return this.rejectAuthDao.deleteRejectAuth(rejectAuthVo);
	}

}
