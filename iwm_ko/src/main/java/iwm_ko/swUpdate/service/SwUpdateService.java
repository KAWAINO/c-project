package iwm_ko.swUpdate.service;

import iwm_ko.swUpdate.model.SwUpdateVo;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface SwUpdateService {
    List<SwUpdateVo> selectCompList(SwUpdateVo swUpdateVo) throws Exception;

    public List<SwUpdateVo> selectSwUpdateList(SwUpdateVo swUpdateVo) throws Exception;

    public List<SwUpdateVo> shipNameList(String comp_id) throws Exception;

    public List<SwUpdateVo> chkSch(SwUpdateVo swUpdateVo) throws Exception;

    public int selectSeq() throws  Exception;

    int insertSwUpdate(SwUpdateVo swUpdateVo)throws Exception;

    int insertSwUpdateSub(SwUpdateVo swUpdateVo)throws Exception;

    SwUpdateVo selectSwUpdateInfo(HashMap<String, Object> req) throws Exception;

    int updateSwUpdate(SwUpdateVo swUpdateVo) throws Exception;

    int deleteSwUpdate(SwUpdateVo swUpdateVo) throws Exception;

    int deleteSwUpdateSub(SwUpdateVo swUpdateVo)throws Exception;
}
