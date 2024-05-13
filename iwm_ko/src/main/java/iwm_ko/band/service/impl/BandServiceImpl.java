package iwm_ko.band.service.impl;

import iwm_ko.band.dao.BandDao;
import iwm_ko.band.model.BandVo;
import iwm_ko.band.service.BandService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iwm_ko.band.BandService")
//@MapperScan("iwm_ko.band.dao")
public class BandServiceImpl implements BandService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="iwm_ko.band.bandDao")
    private BandDao bandDao;

    @Override
    public List<BandVo> selectBandList(BandVo bandVo) throws Exception {
        List<BandVo> resList = this.bandDao.selectBandList(bandVo);
        return resList;
    }

    @Override
    public List<BandVo> chkBand(BandVo bandVo) throws Exception {
        List<BandVo> resList = this.bandDao.chkBand(bandVo);
        return resList;
    }

    @Override
    public int insertBand(BandVo bandVo) throws Exception {
        return this.bandDao.insertBand(bandVo);
    }

    @Override
    public int deleteBand(BandVo bandVo) throws Exception {
        return this.bandDao.deleteBand(bandVo);
    }
}
