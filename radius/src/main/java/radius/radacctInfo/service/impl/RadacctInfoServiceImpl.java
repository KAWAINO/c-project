package radius.radacctInfo.service.impl;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import radius.athntStat.model.AthntStatVo;
import radius.radacctInfo.dao.RadacctInfoDao;
import radius.radacctInfo.model.RadacctInfoVo;
import radius.radacctInfo.service.RadacctInfoService;

@Service("radius.radacctInfo.radacctInfoService")
//@MapperScan("radius.radacctInfo.dao")
public class RadacctInfoServiceImpl implements RadacctInfoService {

	@Resource(name = "radius.radacctInfo.radacctInfoDao")
	private RadacctInfoDao radacctInfoDao;

	// Account Status 조회
	@Override
	public List<RadacctInfoVo> selectAcctStatusList(RadacctInfoVo radacctInfoVo) throws Exception {
		List<RadacctInfoVo> resList = this.radacctInfoDao.selectAcctStatusList(radacctInfoVo);
		return resList;
	}
	
	// 리스트 조회
	@Override
	public List<RadacctInfoVo> selectRadacctInfoList(RadacctInfoVo radacctInfoVo) throws Exception {
		List<RadacctInfoVo> resList = this.radacctInfoDao.selectRadacctInfoList(radacctInfoVo);
		return resList;
	}

	// 페이징 카운트
	@Override
	public int selectRadacctInfoListCnt(RadacctInfoVo radacctInfoVo) throws Exception {
		int resList	= this.radacctInfoDao.selectRadacctInfoListCnt(radacctInfoVo);
		return resList;
	}


}
