package iwm_ko.apport.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import iwm_ko.apport.dao.ApportDao;
import iwm_ko.apport.model.ApportVo;
import iwm_ko.apport.service.ApportService;
import jakarta.annotation.Resource;

@Service("iwm_ko.apport.apportService")
public class ApportServiceImpl implements ApportService {
	
	@Autowired
	private MessageSource messageSource;

	@Resource(name="iwm_ko.apport.apportDao")
	private ApportDao apportDao;

	// shipList
	@Override
	public List<ApportVo> selectShipList(ApportVo apportVo) throws Exception {
		return this.apportDao.selectShipList(apportVo);
	}

	// portList
	@Override
	public List<ApportVo> selectPortList(ApportVo apportVo, Locale locale) throws Exception {	
		
		List<ApportVo> resList = this.apportDao.selectPortList(apportVo, locale); 
		
		for(int i = 0; i < resList.size(); i++) {
			if(resList.get(i).getUse_flag().equals("Y")) {
				resList.get(i).setUse_flag(messageSource.getMessage("list.allow", null, locale));
			} else if(resList.get(i).getUse_flag().equals("N")) {
				resList.get(i).setUse_flag(messageSource.getMessage("list.block", null, locale));
			}
		}
		
		return resList; 
	}

	// chkName
	@Override
	public List<ApportVo> chkName(ApportVo apportVo) throws Exception {
		return this.apportDao.chkName(apportVo);
	}

	// chkRange
	@Override
	public List<ApportVo> chkRange(ApportVo apportVo) throws Exception {
		return this.apportDao.chkRange(apportVo);
	}

	// changePort
	@Override
	public int insertPort(ApportVo apportVo) throws Exception {
		return this.apportDao.insertPort(apportVo);
	}

	// chgPort
	@Override
	public int chgPort(ApportVo apportVo) throws Exception {
		return this.apportDao.chgPort(apportVo);
	}

	// 수정모달
	@Override
	public ApportVo selectApport(HashMap<String, Object> map) throws Exception {
		return this.apportDao.selectApport(map);
	}

	// 삭제
	@Override
	public int deleteApport(ApportVo apportVo) throws Exception {
		return this.apportDao.deleteApport(apportVo);
	}

	// chkPort
	@Override
	public List<ApportVo> chkPort(ApportVo apportVo) throws Exception {
		return this.apportDao.chkPort(apportVo);
	}

	// chgAllPort
	@Override
	public int chgAllPort(ApportVo apportVo) throws Exception {
		return this.apportDao.chgAllPort(apportVo);
	}

	// insertAllPort
	@Override
	public int insertAllPort(ApportVo apportVo) throws Exception {
		return this.apportDao.insertAllPort(apportVo);
	}

	// shipCode
	@Override
	public List<ApportVo> shipCode(ApportVo apportVo) throws Exception {
		return this.apportDao.shipCode(apportVo);
	}

	// delShipcode
	@Override
	public int delShipcode(ApportVo apportVo) throws Exception {
		return this.apportDao.delShipCode(apportVo);
	}

	// insShipCode
	@Override
	public int insShipCode(ApportVo apportVo) throws Exception {
		return this.apportDao.insShipCode(apportVo);
	}

	// chkShipCode
	@Override
	public List<ApportVo> chkShipCode(ApportVo apportVo) throws Exception {
		return this.apportDao.chkShipCode(apportVo);
	}

	// chgApAllPort
	@Override
	public int chgApAllPort(ApportVo apportVo) throws Exception {
		return this.apportDao.chgApAllPort(apportVo);
	}

	// insertApAllPort
	@Override
	public int insertApAllPort(ApportVo apportVo) throws Exception {
		return this.apportDao.insertApAllPort(apportVo);
	}

	@Override
	public List<ApportVo> updateChkRange(ApportVo apportVo) throws Exception {
		return this.apportDao.updateChkRange(apportVo);
	}

}
