package iwm_ko.autoUpdateLog.service;

import iwm_ko.autoUpdateLog.model.AutoUpdateLogVo;

import java.util.Collection;
import java.util.List;

public interface AutoUpdateLogService {
    List<AutoUpdateLogVo> selectAutoUpdateLogList(AutoUpdateLogVo autoUpdateLogVo) throws Exception;

    List<AutoUpdateLogVo> selectCount(AutoUpdateLogVo autoUpdateLogVo)throws Exception;

    List<AutoUpdateLogVo> shipNameList(String comp_id) throws Exception;

    List<AutoUpdateLogVo> selectCompList(AutoUpdateLogVo autoUpdateLogVo) throws Exception;
}
