package iwm_ko.autoUpdateLog.service.impl;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Service;

import iwm_ko.autoUpdateLog.dao.AutoUpdateLogDao;
import iwm_ko.autoUpdateLog.model.AutoUpdateLogVo;
import iwm_ko.autoUpdateLog.service.AutoUpdateLogService;
import jakarta.annotation.Resource;

@Service("iwm_ko.autoUpdateLog.AutoUpdateLogService")
//@MapperScan("iwm_ko.autoUpdateLog.dao")
public class AutoUpdateLogServiceImpl implements AutoUpdateLogService {

    @Resource(name="iwm_ko.autoUpdateLog.autoUpdateLogDao")
    private AutoUpdateLogDao autoUpdateLogDao;


    @Override
    public List<AutoUpdateLogVo> selectAutoUpdateLogList(AutoUpdateLogVo autoUpdateLogVo) throws Exception {
        List<AutoUpdateLogVo> resList = this.autoUpdateLogDao.selectAutoUpdateLogList(autoUpdateLogVo);
        return resList;
    }

    @Override
    public List<AutoUpdateLogVo> selectCount(AutoUpdateLogVo autoUpdateLogVo) throws Exception {
        List<AutoUpdateLogVo>resList = this.autoUpdateLogDao.selectCount(autoUpdateLogVo);
        return resList;
    }



    @Override
    public List<AutoUpdateLogVo> shipNameList(String comp_id) throws Exception {
        List<AutoUpdateLogVo> resList = this.autoUpdateLogDao.selectShipNameList(comp_id);
        return resList;
    }

    @Override
    public List<AutoUpdateLogVo> selectCompList(AutoUpdateLogVo autoUpdateLogVo) throws Exception {
        List<AutoUpdateLogVo> resList = this.autoUpdateLogDao.selectCompList(autoUpdateLogVo);
        return resList;
    }
}
