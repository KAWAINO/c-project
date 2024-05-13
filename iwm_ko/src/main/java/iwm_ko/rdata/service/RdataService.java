package iwm_ko.rdata.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import iwm_ko.rdata.model.RdataVo;

public interface RdataService {

    public List<RdataVo> selectRdataList(RdataVo rdataVo, Locale locale) throws Exception;

    public List<RdataVo> selectRdataCount(RdataVo rdataVo) throws Exception;

    public List<RdataVo> selectShipCompList(RdataVo rdataVo) throws Exception;

    public List<RdataVo> shipNameList(String compId) throws Exception;
    
    public List<RdataVo> rdataList(String s_name) throws Exception;
    
    public List<RdataVo> chkRdata(RdataVo rdataVo) throws Exception;

    public int insertRdata(RdataVo rdataVo) throws Exception;

    public RdataVo selectRdataInfo(HashMap<String, Object> map) throws Exception;

    public int updateRdata(RdataVo rdataVo) throws Exception;

    public int deleteRdata(RdataVo rdataVo) throws  Exception;


    public String updateHistory(RdataVo rdataVo) throws Exception;

    public  List<RdataVo> crewIdList(String sName) throws Exception;

    String chkDel(RdataVo rdataVo) throws Exception;
}
