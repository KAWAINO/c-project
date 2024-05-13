package iwm_ko.crewcdr.service.impl;

import iwm_ko.crewcdr.dao.CrewcdrDao;
import iwm_ko.crewcdr.model.CrewcdrVo;
import iwm_ko.crewcdr.service.CrewcdrService;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import java.util.List;

@Service("iwm_ko.crewcdr.CrewcdrService")
//@MapperScan("iwm_ko.crewcdr.dao")
public class CrewcdrServiceImpl implements CrewcdrService{

    @Resource(name="iwm_ko.crewcdr.crewcdrDao")
    private CrewcdrDao crewcdrDao;

    @Override
    public List<CrewcdrVo> selectCrewcdrList(CrewcdrVo crewcdrVo) throws Exception {
        List<CrewcdrVo> resList = this.crewcdrDao.selectCrewcdrList(crewcdrVo);
        return resList;
    }

    @Override
    public String selectTableName(CrewcdrVo crewcdrVo) throws Exception {
        return this.crewcdrDao.selectTableName(crewcdrVo);
    }

    @Override
    public List<CrewcdrVo> selectShipCompList(CrewcdrVo crewcdrVo) throws Exception {
        List<CrewcdrVo> resList = this.crewcdrDao.selectShipCompList(crewcdrVo);
        return resList;
    }

    @Override
    public List<CrewcdrVo> selectCrewcdrCount(CrewcdrVo crewcdrVo) throws Exception {
        List<CrewcdrVo> resList = this.crewcdrDao.selectCrewcdrCount(crewcdrVo);
        return resList;
    }

    @Override
    public List<CrewcdrVo> crewIdList(String s_code) throws Exception {
        List<CrewcdrVo> resList = this.crewcdrDao.selectCrewIdList(s_code);
        return resList;
    }

    @Override
    public List<CrewcdrVo> shipNameList(String comp_id) throws Exception {
        List<CrewcdrVo> resList = this.crewcdrDao.selectShipNameList(comp_id);
        return resList;
    }


}
