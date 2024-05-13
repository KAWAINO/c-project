package iwm_ko.report.service;

import iwm_ko.report.model.ReportVo;

import java.util.List;

public interface ReportService {
    List<ReportVo> selectCompList(ReportVo reportVo) throws Exception;

    List<ReportVo> shipNameList(String comp_id) throws Exception;

    String chkTable(ReportVo reportVo) throws Exception;

    List<ReportVo> getDataList(ReportVo reportVo) throws Exception;

    String preTable(ReportVo reportVo) throws Exception;

    String getShipName(ReportVo reportVo);

    List<ReportVo> selectShipList(ReportVo reportVo);
}
