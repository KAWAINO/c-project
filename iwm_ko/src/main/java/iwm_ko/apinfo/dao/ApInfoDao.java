package iwm_ko.apinfo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.apinfo.model.ApInfoVo;

@Repository("iwm_ko.apinfo.apInfoDao")
public class ApInfoDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "iwm_ko.apInfo.";

	// 대역폭 리스트 조회
	public List<ApInfoVo> selectBandList(ApInfoVo apInfoVo) throws Exception {
		List<ApInfoVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectBandList"), apInfoVo);
		return resData;
	}

	// apinfo 리스트 조회
	public List<ApInfoVo> selectApInfoList(ApInfoVo apInfoVo, Locale locale) throws Exception {
		List<ApInfoVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectApInfoList"), apInfoVo);
		return resData;
	}

	// 페이징
	public int totalApInfoList(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.selectOne(NAME_SPACE.concat("totalApInfoList"), apInfoVo);
	}
	
	public List<ApInfoVo> selectShipList(ApInfoVo apInfoVo) throws Exception{
		return this.sqlSession.selectList(NAME_SPACE.concat("selectShipList"), apInfoVo);
	}

	// mac 중복검사
	public List<ApInfoVo> chkMac(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkMac"), apInfoVo);
	}

	// ap_name 중복검사
	public List<ApInfoVo> chkName(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkName"), apInfoVo);
	}
	
	// flag 체크
	public List<ApInfoVo> chkFlag(ApInfoVo apInfoVo) {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkFlag"), apInfoVo);
	}

	// 기존 ap 데이터 수정
	public int changeAp(ApInfoVo apInfoVo) throws Exception{
		return this.sqlSession.update(NAME_SPACE.concat("changeAp"), apInfoVo);
	}

	// apinfo 신규등록
	public int insertApInfo(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.insert(NAME_SPACE.concat("insertApInfo"), apInfoVo);
	}

	// 수정모달
	public ApInfoVo selectApInfo(HashMap<String, Object> map) throws Exception{
		return this.sqlSession.selectOne(NAME_SPACE.concat("selectApInfo"), map);
	}

	// subdata 중복검사
	public List<ApInfoVo> chkSubData(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkSubData"), apInfoVo);
	}

	// 기존 data 수정
	public int changeApSub(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("changeApSub"), apInfoVo);
	}

	// subdata 신규등록
	public int insertApSub(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.insert(NAME_SPACE.concat("insertApSub"), apInfoVo);
	}

	// update
	public int updateApInfo(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("updateApInfo"), apInfoVo);
	}
	
	// subdata 중복검사2
	public List<ApInfoVo> chkSubData2(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("chkSubData2"), apInfoVo);
	}

	public int updateApSubY(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("updateApSubY"), apInfoVo);
	}

	public int insertApSubY(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.insert(NAME_SPACE.concat("insertApSubY"), apInfoVo);
	}

	public int updateApSubN(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("updateApSubN"), apInfoVo);
	}

	public int insertApSubN(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.insert(NAME_SPACE.concat("insertApSubN"), apInfoVo);
	}

	// 삭제
	public int deleteApInfo(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("deleteApInfo"), apInfoVo);
	}

	public int reboot(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.update(NAME_SPACE.concat("reboot"), apInfoVo);
	}

	public List<String> updChkName(ApInfoVo apInfoVo) throws Exception {
		return this.sqlSession.selectList(NAME_SPACE.concat("updChkName"), apInfoVo);
	}



}
