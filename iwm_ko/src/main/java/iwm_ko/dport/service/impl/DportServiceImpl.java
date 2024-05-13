package iwm_ko.dport.service.impl;

import iwm_ko.dport.dao.DportDao;
import iwm_ko.dport.model.DportVo;
import iwm_ko.dport.service.DportService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iwm_ko.dport.DportService")
//@MapperScan("iwm_ko.dport.dao")
public class DportServiceImpl implements DportService {


    @Resource(name="iwm_ko.dport.dportDao")
    private DportDao dportDao;


    @Override
    public List<DportVo> selectDportList(DportVo dportVo) throws Exception {
        List<DportVo> resList = this.dportDao.selectDportList(dportVo);
        return resList;
    }

    @Override
    public List<DportVo> chkDport(DportVo dportVo) throws Exception {
        List<DportVo> resList = this.dportDao.chkDport(dportVo);
        return resList;
    }

    @Override
    public int insertDport(DportVo dportVo) throws Exception {
        return this.dportDao.insertDport(dportVo);
    }

    @Override
    public int deleteDport(DportVo dportVo) throws Exception {
        return this.dportDao.deleteDport(dportVo);
    }
}
