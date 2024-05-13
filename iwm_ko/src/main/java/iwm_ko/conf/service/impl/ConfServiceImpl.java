package iwm_ko.conf.service.impl;

import iwm_ko.conf.dao.ConfDao;
import iwm_ko.conf.model.ConfVo;
import iwm_ko.conf.service.ConfService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("iwm_ko.conf.ConfService")
//@MapperScan("iwm_ko.conf.dao")
public class ConfServiceImpl implements ConfService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="iwm_ko.conf.confDao")
    private ConfDao confDao;


    @Override
    public List<ConfVo> selectConfList(ConfVo confVo) throws Exception {
        List<ConfVo> resList = this.confDao.selectConfList(confVo);
        return resList;
    }

    @Override
    public List<ConfVo> selectConfCount(ConfVo confVo) throws Exception {
        List<ConfVo> resList = this.confDao.selectConfCount(confVo);
        return resList;
    }

    @Override
    public List<ConfVo> chkConf(ConfVo confVo) throws Exception {
        List<ConfVo> resList = this.confDao.chkConf(confVo);
        return resList;
    }

    @Override
    public int insertConf(ConfVo confVo) throws Exception {
        return this.confDao.insertConf(confVo);
    }

    @Override
    public ConfVo selectConfInfo(HashMap<String, Object> req) throws Exception {
        return this.confDao.selectConfInfo(req);
    }

    @Override
    public int updateConf(ConfVo confVo) throws Exception {
        return this.confDao.updateConf(confVo);
    }

    @Override
    public int deleteConf(ConfVo confVo) throws Exception {
        return this.confDao.deleteConf(confVo);
    }
}
