package radius.radacctInfo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import radius.radacctInfo.model.RadacctInfoVo;

@Repository("radius.radacctInfo.radacctInfoDao")
public class RadacctInfoDao {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String NAME_SPACE = "radius.radacctInfo.";
	
	// Account Status 조회
	public List<RadacctInfoVo> selectAcctStatusList(RadacctInfoVo radacctInfoVo) throws Exception {
		List<RadacctInfoVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectAcctStatusList"), radacctInfoVo);
		return resData;
	}
	
	// 리스트 조회
	public List<RadacctInfoVo> selectRadacctInfoList(RadacctInfoVo radacctInfoVo) throws Exception {
		List<RadacctInfoVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("selectRadacctInfoList"), radacctInfoVo);
		return resData;
	}

	// 페이징 카운트
	public int selectRadacctInfoListCnt(RadacctInfoVo radacctInfoVo) throws Exception {
		int resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectRadacctInfoListCnt"), radacctInfoVo);
		return resData;
	}

	

}
