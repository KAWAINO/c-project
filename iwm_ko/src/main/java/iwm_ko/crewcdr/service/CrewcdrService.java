package iwm_ko.crewcdr.service;

import iwm_ko.crewcdr.model.CrewcdrVo;

import java.util.List;

public interface CrewcdrService {

    public List<CrewcdrVo> selectCrewcdrList(CrewcdrVo crewcdrVo) throws Exception;

    public String selectTableName(CrewcdrVo crewcdrVo) throws Exception;

    public List<CrewcdrVo> selectShipCompList(CrewcdrVo crewcdrVo) throws Exception;

    public List<CrewcdrVo> selectCrewcdrCount(CrewcdrVo crewcdrVo)throws Exception;

    public List<CrewcdrVo> crewIdList(String sCode) throws Exception;

    public List<CrewcdrVo> shipNameList(String comp_id) throws Exception;
}
