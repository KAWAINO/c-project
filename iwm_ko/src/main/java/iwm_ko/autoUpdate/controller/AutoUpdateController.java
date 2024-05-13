package iwm_ko.autoUpdate.controller;

import iwm_ko.autoUpdate.model.AutoUpdateVo;
import iwm_ko.autoUpdate.service.AutoUpdateService;
import iwm_ko.comm.paging;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/autoUpdate")
public class AutoUpdateController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "iwm_ko.autoUpdate.AutoUpdateService")
    private AutoUpdateService autoUpdateService;
    
    @Resource(name = "iwm_ko.userHistory.userHistoryService")
    private UserHistoryService userHistoryService;


    /**
     * 페이지 로딩
     */
    @RequestMapping("autoUpdate.do")
    public String autoUpdate(@ModelAttribute("autoUpdateVo") AutoUpdateVo autoUpdateVo, HttpServletRequest request, final HttpSession session, Model model)throws Exception{

        autoUpdateVo.setStartrow(((autoUpdateVo.getPageno() - 1) * autoUpdateVo.getFetchrow()));

        String sessionCompId = (String)session.getAttribute("comp_id");
        autoUpdateVo.setComp_id(sessionCompId);
        model.addAttribute("sessionCompId",sessionCompId);


        List<AutoUpdateVo> autoUpdateList = this.autoUpdateService.selectAutoUpdateList(autoUpdateVo);
        model.addAttribute("autoUpdateList",autoUpdateList);

        // 페이징 처리를 위한 총 갯수
        int totalCnt = this.autoUpdateService.selectAutoUpdateCount(autoUpdateVo).size();



        //paging
        paging paging = new paging();
        String pagingHTML = paging.getPagingStr(autoUpdateVo.getPageno(), autoUpdateVo.getFetchrow() , totalCnt);
        model.addAttribute("pagingHTML", pagingHTML);
        model.addAttribute("totalCnt", totalCnt);

        //search data return
        model.addAttribute("autoUpdateVo", autoUpdateVo);


        return "autoUpdate/autoUpdate";
    }




    /**
     * 등록
     */
    @ResponseBody
    @RequestMapping(value="autoUpdateAdd.ajax", method= RequestMethod.POST)
    public HashMap<String, Object> autoUpdateAdd(AutoUpdateVo autoUpdateVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            //중복검사
            int chkAU = this.autoUpdateService.chkAutoUpdate(autoUpdateVo).size();
            if(chkAU > 0) {
                resultMap.put("result", "-2");
            }else {
                autoUpdateVo.setSeq(this.autoUpdateService.selectSeq());

             int insResult = this.autoUpdateService.insertAutoUpdate(autoUpdateVo);
             if (insResult == 1){
                 resultMap.put("result", "1");
             }else {
                 resultMap.put("result", "0");
             }

            }
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "0");
        }

        return resultMap;
    }


    /**
     * 수정 Modal 이동
     */
    @ResponseBody
    @RequestMapping(value = "autoUpdateSetUpdateData.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> autoUpdateSetUpdateData(@RequestParam HashMap<String, Object> req, final HttpSession session) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            AutoUpdateVo autoUpdateVo = this.autoUpdateService.selectAutoUpdateInfo(req);

            resultMap.put("autoUpdateVo", autoUpdateVo);
            resultMap.put("result", "success");
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }
        return resultMap;
    }

    /**
     *  수정
     */
    @ResponseBody
    @RequestMapping(value="autoUpdateUpdate.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> autoUpdateUpdate(AutoUpdateVo autoUpdateVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();


        try {
            //update
            int updResult = this.autoUpdateService.updateAutoUpdate(autoUpdateVo);

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


    /**
     *  삭제
     */
    @ResponseBody
    @RequestMapping(value="autoUpdateDelete.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> autoUpdateDelete(AutoUpdateVo autoUpdateVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            //update

            int updResult = this.autoUpdateService.deleteAutoUpdate(autoUpdateVo);

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
