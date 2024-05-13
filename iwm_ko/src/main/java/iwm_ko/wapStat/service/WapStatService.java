package iwm_ko.wapStat.service;

import iwm_ko.wapStat.model.WapStatVo;

import java.util.Collection;
import java.util.List;

public interface WapStatService {
    public List<WapStatVo> selectCompList(WapStatVo wapStatVo) throws Exception;

    public String selectTableName(WapStatVo wapStatVo) throws Exception;

    public List<WapStatVo> selectWapStatList(WapStatVo wapStatVo) throws Exception;

    public List<WapStatVo> selectWapStatCount(WapStatVo wapStatVo) throws Exception;

    public List<WapStatVo> wapSelectList(WapStatVo wapStatVo) throws Exception;

    public List<WapStatVo> shipNameList(String comp_id) throws Exception;


}
