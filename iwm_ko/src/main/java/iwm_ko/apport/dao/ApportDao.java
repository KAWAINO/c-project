package iwm_ko.apport.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.apport.model.ApportVo;

@Repository("iwm_ko.apport.apportDao")
public class ApportDao {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.apport.";

	// shipList
	public List<ApportVo> selectShipList(ApportVo apportVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("selectShipList"), apportVo);
	}

	// portList
	public List<ApportVo> selectPortList(ApportVo apportVo, Locale locale) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("selectPortList"), apportVo);
	}

	// chkName
	public List<ApportVo> chkName(ApportVo apportVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkName"), apportVo);
	}

	// chkRange
	public List<ApportVo> chkRange(ApportVo apportVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkRange"), apportVo);
	}

	// changePort
	public int insertPort(ApportVo apportVo) throws Exception{
		return this.sqlSession.insert(NAME_SPACE.concat("insertPort"), apportVo);
	}

	// chgPort
	public int chgPort(ApportVo apportVo) throws Exception{
		return this.sqlSession.update(NAME_SPACE.concat("chgPort"), apportVo);
	}

	// 수정모달
	public ApportVo selectApport(HashMap<String, Object> map) throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("selectApport"), map);
	}

	// 삭제
	public int deleteApport(ApportVo apportVo) throws Exception {
		return this.sqlSession.delete(NAME_SPACE.concat("deleteApport"), apportVo);
	}

	// chkPort
	public List<ApportVo> chkPort(ApportVo apportVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkPort"), apportVo);
	}

	// chgAllPort
	public int chgAllPort(ApportVo apportVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("chgAllPort"), apportVo);
	}

	// insertAllPort
	public int insertAllPort(ApportVo apportVo) throws Exception {
		return this.sqlSession.insert(NAME_SPACE.concat("insertAllPort"), apportVo);
	}

	// shipCode
	public List<ApportVo> shipCode(ApportVo apportVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("shipCode"), apportVo);
		
	}

	// delShipCode
	public int delShipCode(ApportVo apportVo) throws Exception {
		return this.sqlSession.delete(NAME_SPACE.concat("delShipCode"), apportVo);
	}

	// insShipCode
	public int insShipCode(ApportVo apportVo) throws Exception {
		return this.sqlSession.insert(NAME_SPACE.concat("insShipCode"), apportVo);
	}

	// chkShipCode
	public List<ApportVo> chkShipCode(ApportVo apportVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkShipCode"), apportVo);
	}

	// chgApAllPort
	public int chgApAllPort(ApportVo apportVo) {
		return this.sqlSession.update(NAME_SPACE.concat("chgApAllPort"), apportVo);
	}

	// insertApAllPort
	public int insertApAllPort(ApportVo apportVo) {
		return this.sqlSession.insert(NAME_SPACE.concat("insertApAllPort"), apportVo);
	}

	
	public List<ApportVo> updateChkRange(ApportVo apportVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("updateChkRange"), apportVo);
	}


}
