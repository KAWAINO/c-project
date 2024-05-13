package iwm_ko.band.controller;

import iwm_ko.band.model.BandVo;
import iwm_ko.band.service.BandService;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/band")
public class BandController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "iwm_ko.band.BandService")
    private BandService bandService;
    
    @Autowired
    private UserHistoryService userHistoryService;

    /**
     * 페이지 로딩
     */
    @RequestMapping("band.do")
    public String crewInfo(@ModelAttribute("bandVo") BandVo bandVo, HttpServletRequest request, final HttpSession session, Model model)throws Exception{

        String sessionCompId = (String)session.getAttribute("comp_id");
        bandVo.setComp_id(sessionCompId);
        model.addAttribute("sessionCompId",sessionCompId);

        List<BandVo> bandList = this.bandService.selectBandList(bandVo);
        model.addAttribute("bandList",bandList);

        // 로그 기록
        UserHistoryVo userHistory = new UserHistoryVo();
        userHistory.setGui_code("709");
        userHistory.setJob_name("검색");
        userHistory.setGui_remark("Data Search");
        userHistory.setUser_id((String) session.getAttribute("user_id"));
        userHistory.setSql_str("");
        userHistoryService.insertUserHistory(userHistory);

        model.addAttribute("bandVo", bandVo);

        return "band/band";
    }

    /**
     * 등록
     */
    @ResponseBody
    @RequestMapping(value="bandAdd.ajax", method= RequestMethod.POST)
    public HashMap<String, Object> bandAdd(BandVo bandVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            int insResult = 0;
            //중복검사
            int chkBand = this.bandService.chkBand(bandVo).size();
            if(chkBand > 0) {
                resultMap.put("result", "-2");
            }else {
                insResult = this.bandService.insertBand(bandVo);

                //로그 기록
                UserHistoryVo userHistory = new UserHistoryVo();
                userHistory.setGui_code("709");
                userHistory.setJob_name("추가");
                userHistory.setGui_remark("Bandwidth : "+bandVo.getAdd_band());
                userHistory.setUser_id((String) session.getAttribute("user_id"));
                userHistory.setSql_str("insert into TB_BAND_WIDTH ( BAND_WIDTH, MODIFY_DATE ) values" +
                        " (" + bandVo.getAdd_band() +", NOW())");
                userHistoryService.insertUserHistory(userHistory);

                resultMap.put("result", "1");
            }
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "0");
        }

        return resultMap;
    }



    /**
     *  삭제
     */
    @ResponseBody
    @RequestMapping(value="bandDelete.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> bandDelete(BandVo bandVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            //update

            int updResult = this.bandService.deleteBand(bandVo);

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("709");
            userHistory.setJob_name("삭제");
            userHistory.setGui_remark("Bandwidth : " + bandVo.getBand_width());
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("delete from TB_BAND_WIDTH where BAND_WIDTH=" + bandVo.getBand_width());

            userHistoryService.insertUserHistory(userHistory);

            if(updResult > 0) {
                resultMap.put("result", "1");
            }else {
                resultMap.put("result", "0");
            }
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "0");
        }
        return resultMap;
    }


}
