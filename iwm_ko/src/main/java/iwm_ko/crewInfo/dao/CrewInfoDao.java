package iwm_ko.crewInfo.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.crewInfo.model.CrewInfoVo;

//@Mapper
@Repository("iwm_ko.crewInfo.crewInfoDao")
public class CrewInfoDao {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.crewInfo.";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<CrewInfoVo> selectCrewInfoList(CrewInfoVo crewInfoVo, Locale locale) throws Exception{
        List<CrewInfoVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCrewInfoList"),crewInfoVo);
        return resList;
    }

    public List<CrewInfoVo> selectShipCompList(CrewInfoVo crewInfoVo) throws Exception{
        List<CrewInfoVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipCompList"),crewInfoVo);
        return resList;
    }


    public List<CrewInfoVo> selectCrewInfoCount(CrewInfoVo crewInfoVo) throws Exception{
        List<CrewInfoVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCrewInfoCount"),crewInfoVo);
        return resList;
    }

    public List<CrewInfoVo> chkCrewInfo(CrewInfoVo crewInfoVo) throws Exception{
        List<CrewInfoVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("chkCrewInfo"),crewInfoVo);
        return resData;
    }

    public int insertCrewInfo(CrewInfoVo crewInfoVo) throws Exception {
        return sqlSession.insert(NAME_SPACE.concat("insertCrewInfo"),crewInfoVo);
    }

    public CrewInfoVo selectCrewInfo(HashMap<String, Object> map) throws Exception{
        CrewInfoVo resData = this.sqlSession.selectOne(NAME_SPACE.concat("selectCrewInfo"), map);
        return resData;
    }


    public List<CrewInfoVo> selectRdataList(String s_name) {
        List<CrewInfoVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectRdataList"),s_name);
        return resList;
    }

    public int insertSubCrewInfo(CrewInfoVo crewInfoVo) throws Exception{
        return sqlSession.insert(NAME_SPACE.concat("insertSubCrewInfo"),crewInfoVo);
    }
    public List<CrewInfoVo> chkSubCrewInfo(CrewInfoVo crewInfoVo) throws Exception{
        List<CrewInfoVo> resData = this.sqlSession.selectList(NAME_SPACE.concat("chkSubCrewInfo"),crewInfoVo);
        return resData;
    }

    public int updateInsertCrewInfo(CrewInfoVo crewInfoVo) {
        return sqlSession.update(NAME_SPACE.concat("updateInsertCrewInfo"),crewInfoVo);
    }


    public String chkFlag(CrewInfoVo crewInfoVo) throws Exception {
        return sqlSession.selectOne(NAME_SPACE.concat("chkFlag"), crewInfoVo);
    }


    public String chkFlagN(CrewInfoVo crewInfoVo) throws Exception {
        return sqlSession.selectOne(NAME_SPACE.concat("chkFlagN"), crewInfoVo);
    }

    public int updateCrewInfo(CrewInfoVo crewInfoVo) {
        return sqlSession.update(NAME_SPACE.concat("updateCrewInfo"),crewInfoVo);
    }

    public int deleteCrewInfo(CrewInfoVo crewInfoVo) throws Exception{
        return sqlSession.update(NAME_SPACE.concat("deleteCrewInfo"),crewInfoVo);
    }

    public int updateAllDefault(CrewInfoVo crewInfoVo) {
        return sqlSession.update(NAME_SPACE.concat("updateAllDefault"),crewInfoVo);
    }

    public String selectVal1FromCrewInfoSub(CrewInfoVo crewInfoVo) throws Exception{
        return sqlSession.selectOne(NAME_SPACE.concat("selectVal1FromCrewInfoSub"),crewInfoVo);
    }

    public CrewInfoVo getRateInfo(CrewInfoVo crewInfoVo) throws Exception{
        return sqlSession.selectOne(NAME_SPACE.concat("getRateInfo"),crewInfoVo);
    }

    public int updateCrewInfoDefault(CrewInfoVo crewInfoVo) throws Exception{
        return sqlSession.update(NAME_SPACE.concat("updateCrewInfoDefault"),crewInfoVo);
    }

    public int updateAllRate(CrewInfoVo crewInfoVo) throws Exception{
        return sqlSession.update(NAME_SPACE.concat("updateAllRate"),crewInfoVo);
    }

    public String getDefaultRate(CrewInfoVo crewInfoVo) throws Exception{
        return sqlSession.selectOne("getDefaultRate",crewInfoVo);
    }

    public int checkSubData(String s_code, String crew_id) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("s_code", s_code);
        params.put("crew_id", crew_id);
        return sqlSession.selectOne("checkSubData", params);
    }

    public List<CrewInfoVo> selectShipNameList(String comp_id) throws Exception{
        List<CrewInfoVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectShipNameList"),comp_id);
        return resList;

    }

    public List<CrewInfoVo> selectRateNameList(String s_name) throws Exception{
        List<CrewInfoVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectRateNameList"),s_name);
        return resList;
    }

    public List<CrewInfoVo> selectCrewIdList(String s_name) throws Exception{
        List<CrewInfoVo>resList = this.sqlSession.selectList(NAME_SPACE.concat("selectCrewIdList"),s_name);
        return resList;

    }

    public int updateAllWifi(CrewInfoVo crewInfoVo) throws Exception{
        return this.sqlSession.update(NAME_SPACE.concat("updateAllWifi"),crewInfoVo);
    }

    public List<CrewInfoVo> chkSubData(CrewInfoVo crewInfoVo) throws Exception{
        return this.sqlSession.selectList(NAME_SPACE.concat("chkSubData"),crewInfoVo);
    }

    public String getVal1(CrewInfoVo crewInfoVo) throws Exception{
        return this.sqlSession.selectOne(NAME_SPACE.concat("getVal1"),crewInfoVo);
    }

    public int getAllDefault(CrewInfoVo crewInfoVo) throws Exception{
        return this.sqlSession.update(NAME_SPACE.concat("allDefault"),crewInfoVo);
    }

    public int updateAllVal2(CrewInfoVo crewInfoVo) throws Exception{
        return this.sqlSession.update(NAME_SPACE.concat("updateAllVal2"),crewInfoVo);
    }

    public int updateDefaultVal1(CrewInfoVo crewInfoVo) throws Exception{
        return this.sqlSession.update(NAME_SPACE.concat("updateDefaultVal1"),crewInfoVo);
    }

    public int delPreData() throws Exception{
        return this.sqlSession.update(NAME_SPACE.concat("delPreData"));
    }

    public String getShipCode(String s_name) throws Exception{
        return this.sqlSession.selectOne(NAME_SPACE.concat("getShipCode"),s_name);
    }

    public List<CrewInfoVo> selectCheckCrewId(CrewInfoVo crewInfoVo) {
        return this.sqlSession.selectList(NAME_SPACE.concat("selectCheckCrewId"),crewInfoVo);
    }

    public String checkCrewFlag(CrewInfoVo crewInfoVo) {
        return this.sqlSession.selectOne(NAME_SPACE.concat("checkCrewFlag"),crewInfoVo);

    }

    public int updateExcelCrewInfo(CrewInfoVo crewInfoVo) {
        return this.sqlSession.update(NAME_SPACE.concat("updateExcelCrewInfo"),crewInfoVo);

    }

    public int insertExcelCrewInfo(CrewInfoVo crewInfoVo) {
        return this.sqlSession.insert(NAME_SPACE.concat("insertExcelCrewInfo"),crewInfoVo);

    }

    public List<CrewInfoVo> selectValList(CrewInfoVo crewInfoVo) throws Exception{
        List<CrewInfoVo> resList= this.sqlSession.selectList(NAME_SPACE.concat("selectValList"),crewInfoVo);
        return resList;
    }

    public List<CrewInfoVo> chkScode(CrewInfoVo crewInfoVo) throws Exception{
        List<CrewInfoVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("chkScode"),crewInfoVo);
        return resList;
    }


    public List<CrewInfoVo> checkRateName(CrewInfoVo crewInfoVo) throws Exception{
        List<CrewInfoVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("checkRateName"),crewInfoVo);
        return resList;
    }

    public int updateCrewRate(String[] arrayRate) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("arrayRate", arrayRate);
        return this.sqlSession.update(NAME_SPACE.concat("updateCrewRate"), paramMap);
    }

    public int insertCrewRate(String rateData) throws Exception{
        return this.sqlSession.insert(NAME_SPACE.concat("insertCrewRate"),rateData);
    }

    public List<CrewInfoVo> checkCrewName(CrewInfoVo crewInfoVo) {
        List<CrewInfoVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("checkCrewName"),crewInfoVo);
        return resList;
    }

    public int updateCrewName(String[] arrayCrew) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("arrayCrew", arrayCrew);
        return this.sqlSession.update(NAME_SPACE.concat("updateCrewName"),paramMap);
    }

    public int insertCrewName(String[] arrayCrew) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("arrayCrew", arrayCrew);
        return this.sqlSession.insert(NAME_SPACE.concat("insertCrewName"),paramMap  );
    }


    public int updateCrewInfoExcel(CrewInfoVo crewInfoVo) {
        return this.sqlSession.update(NAME_SPACE.concat("updateCrewInfoExcel"),crewInfoVo);
    }

    public List<CrewInfoVo> checkExcelSub(CrewInfoVo crewInfoVo) throws Exception{
        List<CrewInfoVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("checkExcelSub"),crewInfoVo);
        return resList;
    }

    public int insertExcelSubData(CrewInfoVo crewInfoVo) throws Exception{
        return this.sqlSession.insert(NAME_SPACE.concat("insertExcelSubData"),crewInfoVo);
    }

    public int updateExcelSubData(CrewInfoVo crewInfoVo) throws Exception{
        return this.sqlSession.update(NAME_SPACE.concat("updateExcelSubData"),crewInfoVo);
    }
}
