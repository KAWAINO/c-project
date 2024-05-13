package iwm_ko.noauth.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import iwm_ko.noauth.dao.NoAuthDao;
import iwm_ko.noauth.model.NoAuthVo;
import iwm_ko.noauth.service.NoAuthService;
import jakarta.annotation.Resource;

@Service("iwm_ko.noauth.noAuthService")
public class NoAuthServiceImpl implements NoAuthService{

	@Resource(name="iwm_ko.noauth.noAuthDao")
	private NoAuthDao noAuthDao;

	// authList
	@Override
	public List<NoAuthVo> selectAuthList(NoAuthVo noAuthVo) throws Exception {
		return this.noAuthDao.selectAuthList(noAuthVo);
	}

	// 페이징
	@Override
	public int totalAuthList(NoAuthVo noAuthVo) throws Exception {
		return this.noAuthDao.totalAuthList(noAuthVo);
	}

	// chkMac
	@Override
	public List<NoAuthVo> chkMac(NoAuthVo noAuthVo) throws Exception {
		return this.noAuthDao.chkMac(noAuthVo);
	}

	// chkName
	@Override
	public List<NoAuthVo> chkName(NoAuthVo noAuthVo) throws Exception {
		return this.noAuthDao.chkName(noAuthVo);
	}

	// chkFlag
	@Override
	public List<NoAuthVo> chkFlag(NoAuthVo noAuthVo) throws Exception {
		return this.noAuthDao.chkFlag(noAuthVo);
	}

	// chgAuth
	@Override
	public int chgAuth(NoAuthVo noAuthVo) throws Exception {
		return this.noAuthDao.chgAuth(noAuthVo);
	}

	// insAuth
	@Override
	public int insAuth(NoAuthVo noAuthVo) throws Exception {
		return this.noAuthDao.insAuth(noAuthVo);
	}

	// 수정모달이동
	@Override
	public NoAuthVo selectAuthInfo(HashMap<String, Object> map) throws Exception {
		return this.noAuthDao.selectAuthInfo(map);
	}

	// 수정
	@Override
	public int updateAuth(NoAuthVo noAuthVo) throws Exception {
		return this.noAuthDao.updateAuth(noAuthVo);
	}

	// 삭제
	@Override
	public int deleteAuth(NoAuthVo noAuthVo) throws Exception {
		return this.noAuthDao.deleteAuth(noAuthVo);
	}

	@Override
	public List<String> updChkName(NoAuthVo noAuthVo) throws Exception {
		return this.noAuthDao.updChkName(noAuthVo);
	}
}
