package radius.inmarsat.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import radius.inmarsat.dao.InmarsatDao;
import radius.inmarsat.model.InmarsatVo;
import radius.inmarsat.service.InmarsatService;

@Service("radius.inmarsat.inmarsatService")
//@MapperScan("radius.inmarsat.dao")
public class InmarsatServiceImpl implements InmarsatService{

	@Resource(name = "radius.inmarsat.inmarsatDao")
	private InmarsatDao inmarsatDao;

	// inmarsatList
	@Override
	public List<InmarsatVo> selectInmarsatList(InmarsatVo inmarsatVo) throws Exception {
		List<InmarsatVo> resList = inmarsatDao.selectInmarsatList(inmarsatVo);
		return resList;
	}

	// paging
	@Override
	public int selectInmarsatCnt(InmarsatVo inmarsatVo) throws Exception {
		int inmarsatCnt = this.inmarsatDao.selectInmarsatCnt(inmarsatVo);
		return inmarsatCnt;
	}

	// 중복체크
	@Override
	public List<InmarsatVo> chkInsertValues(InmarsatVo inmarsatVo) throws Exception {
		List<InmarsatVo> resList = inmarsatDao.chkInsertValues(inmarsatVo);
		return resList;
	}

	// 등록
	@Override
	public int insertInmarsat(InmarsatVo inmarsatVo) throws Exception {
		int insList = this.inmarsatDao.insertInmarsat(inmarsatVo);
		return insList;
	}

	// 수정모달
	@Override
	public InmarsatVo selectInmarsatInfo(HashMap<String, Object> map) throws Exception {
		InmarsatVo modal = this.inmarsatDao.selectInmarsatInfo(map);
		return modal;
	}

	// 수정
	@Override
	public int updateInmarsat(InmarsatVo inmarsatVo) throws Exception {
		int insList = this.inmarsatDao.updateInmarsat(inmarsatVo);
		return insList;
	}

	// 삭제
	@Override
	public int deleteInmarsat(InmarsatVo inmarsatVo) throws Exception {
		int insList = this.inmarsatDao.deleteInmarsat(inmarsatVo);
		return insList;
	}
}
