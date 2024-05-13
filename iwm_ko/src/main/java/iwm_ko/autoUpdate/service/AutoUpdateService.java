package iwm_ko.autoUpdate.service;

import iwm_ko.autoUpdate.model.AutoUpdateVo;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface AutoUpdateService {
    List<AutoUpdateVo> selectAutoUpdateList(AutoUpdateVo autoUpdateVo) throws Exception;

    List<AutoUpdateVo> selectAutoUpdateCount(AutoUpdateVo autoUpdateVo)throws Exception;

    List<AutoUpdateVo> chkAutoUpdate(AutoUpdateVo autoUpdateVo) throws Exception;

    int insertAutoUpdate(AutoUpdateVo autoUpdateVo) throws Exception;

    String selectSeq() throws Exception;

    AutoUpdateVo selectAutoUpdateInfo(HashMap<String, Object> req) throws Exception;

    int updateAutoUpdate(AutoUpdateVo autoUpdateVo) throws Exception;

    int deleteAutoUpdate(AutoUpdateVo autoUpdateVo) throws Exception;
}
