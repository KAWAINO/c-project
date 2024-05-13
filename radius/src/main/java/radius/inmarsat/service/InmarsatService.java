package radius.inmarsat.service;

import java.util.HashMap;
import java.util.List;

import radius.inmarsat.model.InmarsatVo;

public interface InmarsatService {

	/**
	 * inmarsatList
	 * @param inmarsatVo
	 * @return
	 * @throws Exception
	 */
	List<InmarsatVo> selectInmarsatList(InmarsatVo inmarsatVo) throws Exception;

	/**
	 * paging
	 * @param inmarsatVo
	 * @return
	 * @throws Exception
	 */
	int selectInmarsatCnt(InmarsatVo inmarsatVo) throws Exception;

	/**
	 * 중복체크
	 * @param inmarsatVo
	 * @return
	 * @throws Exception
	 */
	List<InmarsatVo> chkInsertValues(InmarsatVo inmarsatVo) throws Exception;

	/**
	 * 등록
	 * @param inmarsatVo
	 * @return
	 * @throws Exception
	 */
	int insertInmarsat(InmarsatVo inmarsatVo) throws Exception;

	/**
	 * 수정모달
	 * @param req
	 * @return
	 * @throws Exception
	 */
	InmarsatVo selectInmarsatInfo(HashMap<String, Object> map) throws Exception;

	/**
	 * 수정
	 * @param inmarsatVo
	 * @return
	 * @throws Exception
	 */
	int updateInmarsat(InmarsatVo inmarsatVo) throws Exception;

	/**
	 * 삭제
	 * @param inmarsatVo
	 * @return
	 * @throws Exception
	 */
	int deleteInmarsat(InmarsatVo inmarsatVo) throws Exception;

}
