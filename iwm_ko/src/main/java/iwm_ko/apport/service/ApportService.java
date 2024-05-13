package iwm_ko.apport.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import iwm_ko.apport.model.ApportVo;

public interface ApportService {

	// shipList
	List<ApportVo> selectShipList(ApportVo apportVo) throws Exception;

	// portList
	List<ApportVo> selectPortList(ApportVo apportVo, Locale locale) throws Exception;

	// chkName
	List<ApportVo> chkName(ApportVo apportVo) throws Exception;

	// chkRange
	List<ApportVo> chkRange(ApportVo apportVo) throws Exception;

	// changePort
	int insertPort(ApportVo apportVo) throws Exception;

	// chgPort
	int chgPort(ApportVo apportVo) throws Exception;

	// 수정모달
	ApportVo selectApport(HashMap<String, Object> map) throws Exception;

	// 삭제
	int deleteApport(ApportVo apportVo) throws Exception;

	// chkPort
	List<ApportVo> chkPort(ApportVo apportVo) throws Exception;

	// chgAllPort
	int chgAllPort(ApportVo apportVo) throws Exception;

	// insertAllPort
	int insertAllPort(ApportVo apportVo) throws Exception;

	// shipCode
	List<ApportVo> shipCode(ApportVo apportVo) throws Exception;

	// delShipcode
	int delShipcode(ApportVo apportVo) throws Exception;

	// insShipcode
	int insShipCode(ApportVo apportVo) throws Exception;

	// chkShipCode
	List<ApportVo> chkShipCode(ApportVo apportVo) throws Exception;

	// chgApAllPort
	int chgApAllPort(ApportVo apportVo) throws Exception;

	// insertApAllPort
	int insertApAllPort(ApportVo apportVo) throws Exception;

	
	List<ApportVo> updateChkRange(ApportVo apportVo) throws Exception;


}
