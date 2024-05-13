package iwm_ko.crewInfo.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import iwm_ko.crewInfo.model.CrewInfoVo;

public interface CrewInfoService {

    public List<CrewInfoVo> selectCrewInfoList(CrewInfoVo crewInfoVo, Locale locale) throws Exception;

    public List<CrewInfoVo> selectShipCompList(CrewInfoVo crewInfoVo) throws Exception;

    public List<CrewInfoVo> selectCrewInfoCount(CrewInfoVo crewInfoVo) throws Exception;

    public List<CrewInfoVo> chkCrewInfo(CrewInfoVo crewInfoVo) throws Exception;
    
    public int insertCrewInfo(CrewInfoVo crewInfoVo) throws Exception;

    public CrewInfoVo selectCrewInfo(HashMap<String, Object> map) throws Exception;


    public List<CrewInfoVo> selectRdataList(String s_name) throws  Exception;

    public int insertSubCrewInfo(CrewInfoVo crewInfoVo) throws  Exception;


    public List<CrewInfoVo> chkSubCrewInfo(CrewInfoVo crewInfoVo) throws Exception;


    public int updateInsertCrewInfo(CrewInfoVo crewInfoVo) throws Exception;
    public int deleteCrewInfo(CrewInfoVo crewInfoVo) throws  Exception;



    int updateCrewInfo(CrewInfoVo crewInfoVo) throws Exception;

    public String chkFlag(CrewInfoVo crewInfoVo)throws Exception;

   public int updateAllDefault(CrewInfoVo crewInfoVo) throws Exception;

   public int updateAllRate(CrewInfoVo crewInfoVo) throws Exception;

    public String updateHistory(CrewInfoVo crewInfoVo) throws Exception;



    public List<CrewInfoVo> shipNameList(String compId) throws Exception;

    public List<CrewInfoVo> rateNameList(String s_name) throws Exception;

    public List<CrewInfoVo> crewIdList(String s_name) throws Exception;

    int updateAllWifi(CrewInfoVo crewInfoVo) throws Exception;

    List<CrewInfoVo> chkSubData(CrewInfoVo crewInfoVo)throws Exception;

    CrewInfoVo getRateInfo(CrewInfoVo crewInfoVo)throws Exception;

    int updateAllVal2(CrewInfoVo crewInfoVo) throws Exception;


    int updateDefaultVal1(CrewInfoVo crewInfoVo) throws Exception;

    String selectDefaultRateName(CrewInfoVo crewInfoVo) throws Exception;

    int delPreData() throws Exception;

    @Transactional
    String setExcel(MultipartFile file, String csField, String saveDir, File destFile) throws Exception;


    String insertExcel(ArrayList<ArrayList<String>> compList) throws Exception;

    List<CrewInfoVo> valList(CrewInfoVo crewInfoVo)throws Exception;

    List<CrewInfoVo> chkScode(CrewInfoVo crewInfoVo) throws Exception;


    List<CrewInfoVo> CheckRateName(CrewInfoVo crewInfoVo) throws Exception;

    int updateCrewRate(String[] arrayRate) throws Exception;

    int insertCrewRate(String rateData) throws Exception;

    List<CrewInfoVo> CheckCrewName(CrewInfoVo crewInfoVo)throws Exception;

    int updateCrewName(String[] arrayCrew) throws Exception;

    int insertCrewName(String[] arrayCrew) throws Exception;


}
