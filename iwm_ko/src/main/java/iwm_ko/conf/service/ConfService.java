package iwm_ko.conf.service;

import iwm_ko.conf.model.ConfVo;
import iwm_ko.crewInfo.model.CrewInfoVo;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface ConfService {
    List<ConfVo> selectConfList(ConfVo confVo) throws Exception;

    List<ConfVo> selectConfCount(ConfVo confVo) throws Exception;

    List<ConfVo> chkConf(ConfVo confVo) throws Exception;

    int insertConf(ConfVo confVo) throws Exception;

    ConfVo selectConfInfo(HashMap<String, Object> req) throws Exception;

    int updateConf(ConfVo confVo) throws  Exception;

    int deleteConf(ConfVo confVo) throws Exception;
}
