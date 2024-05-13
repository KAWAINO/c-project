package iwm_ko.report.service.impl;

import iwm_ko.report.dao.ReportDao;
import iwm_ko.report.model.ReportVo;
import iwm_ko.report.service.ReportService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iwm_ko.report.ReportService")
//@MapperScan("iwm_ko.report.dao")
public class ReportServiceImpl implements ReportService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="iwm_ko.report.reportDao")
    private ReportDao reportDao;

    @Override
    public List<ReportVo> selectCompList(ReportVo reportVo) throws Exception {
        List<ReportVo> resList = this.reportDao.selectCompList(reportVo);
        return resList;
    }

    @Override
    public List<ReportVo> shipNameList(String comp_id) throws Exception {
        List<ReportVo> resList  = this.reportDao.selectShipNameList(comp_id);
        return resList;
    }

    @Override
    public String chkTable(ReportVo reportVo) throws Exception {
        return this.reportDao.chkTable(reportVo);
    }

    @Override
    public List<ReportVo> getDataList(ReportVo reportVo) throws Exception {
        List<ReportVo> resList = this.reportDao.getDataList(reportVo);
        return resList;
    }

    @Override
    public String preTable(ReportVo reportVo) throws Exception {
        return this.reportDao.preTable(reportVo);

    }

    @Override
    public String getShipName(ReportVo reportVo) {
        return this.reportDao.getShipName(reportVo);
    }

    @Override
    public List<ReportVo> selectShipList(ReportVo reportVo) {
        List<ReportVo> resList= this.reportDao.selectShipList(reportVo);
        return resList;
    }
}
