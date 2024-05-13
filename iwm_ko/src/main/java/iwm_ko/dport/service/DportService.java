package iwm_ko.dport.service;

import iwm_ko.dport.model.DportVo;

import java.util.Collection;
import java.util.List;

public interface DportService {
    List<DportVo> selectDportList(DportVo dportVo)throws Exception;

    List<DportVo> chkDport(DportVo dportVo) throws Exception;

    int insertDport(DportVo dportVo) throws  Exception;

    int deleteDport(DportVo dportVo) throws Exception;
}
