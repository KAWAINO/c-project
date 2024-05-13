package apms.suser.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import apms.suser.dao.SuserDao;
import apms.suser.model.GradeVo;
import apms.suser.model.SuserVo;
import apms.suser.service.SuserService;
import jakarta.annotation.Resource;

@Service("apms.suser.suserService")
public class SuserServiceImpl implements SuserService {
	@Resource(name="apms.suser.suserDao")
	private SuserDao suserDao;
	
	public List<GradeVo> selectUserGrade() throws Exception{
		List<GradeVo> resList = this.suserDao.selectUserGrade();
		return resList;
	}
	
	public List<SuserVo> selectUserList(SuserVo suserVo) throws Exception{
		List<SuserVo> resList = this.suserDao.selectUserList(suserVo);
		return resList;
	}
	
	public List<SuserVo> chkInsertSuser(SuserVo suserVo) throws Exception{
		List<SuserVo> resList = this.suserDao.chkInsertSuser(suserVo);
		return resList;
	}
	
	public int insertSuser(SuserVo suserVo) throws Exception{
		return this.suserDao.insertSuser(suserVo);
	}
	
	public SuserVo selectSuserInfo(HashMap<String, Object> map) throws Exception{
		return this.suserDao.selectSuserInfo(map);
	}
	
	public int updateSuser(SuserVo suserVo) throws Exception{
		return this.suserDao.updateSuser(suserVo);
	}
	
	public int deleteSuser(SuserVo suserVo) throws Exception{
		return this.suserDao.deleteSuser(suserVo);
	}
}
