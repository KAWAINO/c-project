package iwm_ko.autoUpdate.service.impl;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import iwm_ko.autoUpdate.dao.AutoUpdateDao;
import iwm_ko.autoUpdate.model.AutoUpdateVo;
import iwm_ko.autoUpdate.service.AutoUpdateService;
import jakarta.annotation.Resource;

@Service("iwm_ko.autoUpdate.AutoUpdateService")
//@MapperScan("iwm_ko.autoUpdate.dao")
public class AutoUpdateServiceImpl implements AutoUpdateService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="iwm_ko.autoUpdate.autoUpdateDao")
    private AutoUpdateDao autoUpdateDao;


    @Override
    public List<AutoUpdateVo> selectAutoUpdateList(AutoUpdateVo autoUpdateVo) throws Exception {
        List<AutoUpdateVo> resList = this.autoUpdateDao.selectAutoUpdateList(autoUpdateVo);
        return resList;
    }

    @Override
    public List<AutoUpdateVo> selectAutoUpdateCount(AutoUpdateVo autoUpdateVo) throws Exception {
        List<AutoUpdateVo> resList = this.autoUpdateDao.selectAutoUpdateCount(autoUpdateVo);
        return resList;
    }

    @Override
    public List<AutoUpdateVo> chkAutoUpdate(AutoUpdateVo autoUpdateVo) throws Exception {
        List<AutoUpdateVo> resList = this.autoUpdateDao.chkAutoUpdate(autoUpdateVo);
        return resList;
    }

    @Override
    public int insertAutoUpdate(AutoUpdateVo autoUpdateVo) throws Exception {
        return this.autoUpdateDao.insertAutoUpdate(autoUpdateVo);
    }

    @Override
    public String selectSeq() throws Exception {
        return this.autoUpdateDao.selectSeq();
    }

    @Override
    public AutoUpdateVo selectAutoUpdateInfo(HashMap<String, Object> req) throws Exception {
        return this.autoUpdateDao.selectAutoUpdateInfo(req);
    }

    @Override
    public int updateAutoUpdate(AutoUpdateVo autoUpdateVo) throws Exception {
        return this.autoUpdateDao.updateAutoUpdate(autoUpdateVo);

    }

    @Override
    public int deleteAutoUpdate(AutoUpdateVo autoUpdateVo) throws Exception {
        return this.autoUpdateDao.deleteAutoUpdate(autoUpdateVo);

    }
}
