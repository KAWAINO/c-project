package iwm_ko.swUpdate.service.impl;

import iwm_ko.swUpdate.dao.SwUpdateDao;
import iwm_ko.swUpdate.model.SwUpdateVo;
import iwm_ko.swUpdate.service.SwUpdateService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("iwm_ko.swUpdate.SwUpdateService")
//@MapperScan("iwm_ko.swUpdate.dao")
public class SwUpdateServiceImpl implements SwUpdateService {

    @Resource(name="iwm_ko.swUpdate.swUpdateDao")
    private SwUpdateDao swUpdateDao;

    @Override
    public List<SwUpdateVo> selectCompList(SwUpdateVo swUpdateVo) throws Exception {
        List<SwUpdateVo> resList = this.swUpdateDao.selectCompList(swUpdateVo);
        return resList;
    }

    @Override
    public List<SwUpdateVo> selectSwUpdateList(SwUpdateVo swUpdateVo) throws Exception {
        List<SwUpdateVo> resList = this.swUpdateDao.selectSwUpdateList(swUpdateVo);
        return resList;

    }

    @Override
    public List<SwUpdateVo> shipNameList(String comp_id) throws Exception {
        List<SwUpdateVo> resLost = this.swUpdateDao.selectShipNameList(comp_id);
        return resLost;

    }

    @Override
    public List<SwUpdateVo> chkSch(SwUpdateVo swUpdateVo) throws Exception {
        List<SwUpdateVo> resList = this.swUpdateDao.chkSch(swUpdateVo);
        return resList;
    }

    @Override
    public int selectSeq() throws Exception {
        return this.swUpdateDao.selectSeq();
    }

    @Override
    public int insertSwUpdate(SwUpdateVo swUpdateVo) throws Exception {
        return this.swUpdateDao.insertSwUpdate(swUpdateVo);
    }

    @Override
    public int insertSwUpdateSub(SwUpdateVo swUpdateVo) throws Exception {
        return this.swUpdateDao.insertSwUpdateSub(swUpdateVo);

    }

    @Override
    public SwUpdateVo selectSwUpdateInfo(HashMap<String, Object> req) throws Exception {
        return this.swUpdateDao.selectSwUpdateInfo(req);
    }

    @Override
    public int updateSwUpdate(SwUpdateVo swUpdateVo) throws Exception {
        return this.swUpdateDao.updateSwUpdate(swUpdateVo);
    }

    @Override
    public int deleteSwUpdate(SwUpdateVo swUpdateVo) throws Exception {
        return this.swUpdateDao.deleteSwUpdate(swUpdateVo);
    }

    @Override
    public int deleteSwUpdateSub(SwUpdateVo swUpdateVo) throws Exception {
        return this.swUpdateDao.deleteSwUpdateSub(swUpdateVo);

    }
}
