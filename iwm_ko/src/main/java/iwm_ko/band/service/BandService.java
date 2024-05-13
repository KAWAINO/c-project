package iwm_ko.band.service;

import iwm_ko.band.model.BandVo;

import java.util.Collection;
import java.util.List;

public interface BandService {
    List<BandVo> selectBandList(BandVo bandVo) throws Exception;

    List<BandVo> chkBand(BandVo bandVo) throws Exception;

    int insertBand(BandVo bandVo) throws Exception;

    int deleteBand(BandVo bandVo) throws Exception;
}
