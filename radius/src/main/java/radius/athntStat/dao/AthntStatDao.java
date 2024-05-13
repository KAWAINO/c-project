package radius.athntStat.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import radius.athntStat.model.AthntStatVo;

@Repository("radius.athntStat.athntStatDao")
public class AthntStatDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "radius.athntStat.";
	
	// 테이블명 검색
	public String selectTableName(AthntStatVo athntStatVo) throws Exception{
		 String tableName = this.sqlSession.selectOne(NAME_SPACE.concat("selectTableName"), athntStatVo);
		 return tableName;
	}
	
	//
	public String selectStatWeek(String startDate) throws Exception{
		String week = this.sqlSession.selectOne(NAME_SPACE.concat("selectStatWeek"), startDate);
		return week;
	}

	// 인증 결과 조회
	public List<AthntStatVo> selelctReplyList(AthntStatVo athntStatVo) throws Exception {
		List<AthntStatVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selelctReplyList"), athntStatVo);
		return resData;
	}

	// 결과 원인 조회
	public List<AthntStatVo> selectCauseList(AthntStatVo athntStatVo) throws Exception {
		List<AthntStatVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectCauseList"), athntStatVo);
		return resData;
	}
	
	// statList 조회
	public List<AthntStatVo> selectStatList(AthntStatVo athntStatVo) throws Exception {
		List<AthntStatVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectStatList"), athntStatVo);
		return resData;
	}

	public int selectStatCnt(AthntStatVo athntStatVo) throws Exception{
		int resCnt = this.sqlSession.selectOne(NAME_SPACE.concat("selectStatCnt"), athntStatVo);
		return resCnt;
	}


	

}
