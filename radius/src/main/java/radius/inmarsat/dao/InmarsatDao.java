package radius.inmarsat.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import radius.inmarsat.model.InmarsatVo;

@Repository("radius.inmarsat.inmarsatDao")
public class InmarsatDao {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	private final String NAME_SPACE = "radius.inmarsat.";

	// inmarsatList
	public List<InmarsatVo> selectInmarsatList(InmarsatVo inmarsatVo) throws Exception {
		List<InmarsatVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectInmarsatList"), inmarsatVo);
		return resData;
	}

	// paging
	public int selectInmarsatCnt(InmarsatVo inmarsatVo) throws Exception {
		int inmarsatCnt = this.sqlSession.selectOne(NAME_SPACE.concat("selectInmarsatCnt"), inmarsatVo);
		return inmarsatCnt;
	}

	// 중복검사
	public List<InmarsatVo> chkInsertValues(InmarsatVo inmarsatVo) throws Exception {
		List<InmarsatVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("chkInsertValues"), inmarsatVo);
		return resData;
	}

	// 등록
	public int insertInmarsat(InmarsatVo inmarsatVo) throws Exception {
		int insData = this.sqlSession.insert(NAME_SPACE.concat("insertInmarsat"), inmarsatVo);
		return insData;
	}

	// 수정모달
	public InmarsatVo selectInmarsatInfo(HashMap<String, Object> map) {
		InmarsatVo resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectInmarsatInfo"), map);
		return resData;
	}

	// 수정
	public int updateInmarsat(InmarsatVo inmarsatVo) {
		int insData = this.sqlSession.update(NAME_SPACE.concat("updateInmarsat"), inmarsatVo);
		return insData;
	}

	// 삭제
	public int deleteInmarsat(InmarsatVo inmarsatVo) {
		int insData = this.sqlSession.delete(NAME_SPACE.concat("deleteInmarsat"), inmarsatVo);
		return insData;
	}

}
