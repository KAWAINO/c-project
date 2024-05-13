package iwm_ko.autoUpdateLog.controller;

import iwm_ko.autoUpdateLog.model.AutoUpdateLogVo;
import iwm_ko.autoUpdateLog.service.AutoUpdateLogService;
import iwm_ko.comm.BaseVo;
import iwm_ko.comm.paging;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/autoUpdateLog")
public class AutoUpdateLogController extends BaseVo implements Serializable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "iwm_ko.autoUpdateLog.AutoUpdateLogService")
    private AutoUpdateLogService autoUpdateLogService;

    /**
     * 페이지 로딩
     */
    @RequestMapping("autoUpdateLog.do")
    public String autoUpdateLog(@ModelAttribute("autoUpdateLogVo") AutoUpdateLogVo autoUpdateLogVo, HttpServletRequest request, final HttpSession session, Model model)throws Exception{

        autoUpdateLogVo.setStartrow((autoUpdateLogVo.getPageno() - 1) * autoUpdateLogVo.getFetchrow());
        String sessionCompId = (String)session.getAttribute("comp_id");
        autoUpdateLogVo.setComp_id(sessionCompId);
        model.addAttribute("sessionCompId",sessionCompId);


        List<AutoUpdateLogVo> compList = this.autoUpdateLogService.selectCompList(autoUpdateLogVo);

        List<AutoUpdateLogVo> autoUpdateLogList = this.autoUpdateLogService.selectAutoUpdateLogList(autoUpdateLogVo);
        model.addAttribute("autoUpdateLogList",autoUpdateLogList);
        model.addAttribute("compList",compList);


        // 페이징 처리를 위한 총 갯수
        int totalCnt = this.autoUpdateLogService.selectCount(autoUpdateLogVo).size();



        //paging
        paging paging = new paging();
        String pagingHTML = paging.getPagingStr(autoUpdateLogVo.getPageno(), autoUpdateLogVo.getFetchrow() , totalCnt);
        model.addAttribute("pagingHTML", pagingHTML);
        model.addAttribute("totalCnt", totalCnt);

        //search data return
        model.addAttribute("autoUpdateLogVo", autoUpdateLogVo);


        return "autoUpdate/autoUpdate_log";
    }


    /**
     * 선주사별 선박명
     */
    @ResponseBody
    @PostMapping("/compList.ajax")
    public HashMap<String, Object> compListAjax(@RequestParam HashMap<String, Object> req, final HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            String comp_id = req.get("comp_id").toString();
            List<AutoUpdateLogVo> shipNameList = autoUpdateLogService.shipNameList(comp_id);

            resultMap.put("shipNameList", shipNameList);
            resultMap.put("result", "success");
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }

        return resultMap;
    }



}
