package iwm_ko.crewStat.service;

import iwm_ko.crewStat.model.CrewStatVo;

import java.util.List;

public interface CrewStatService {
    public List<CrewStatVo> selectCompList(CrewStatVo crewStatVo) throws Exception;

    String selectTableName(CrewStatVo crewStatVo) throws Exception;

    public List<CrewStatVo> selectCrewStatList(CrewStatVo crewStatVo) throws Exception;

    public List<CrewStatVo> selectCrewStatCount(CrewStatVo crewStatVo) throws Exception;

    public List<CrewStatVo> crewSelectList(CrewStatVo crewStatVo) throws  Exception;

    public List<CrewStatVo> shipNameList(String compId) throws Exception;
}
