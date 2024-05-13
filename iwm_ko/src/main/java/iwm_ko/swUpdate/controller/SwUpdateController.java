package iwm_ko.swUpdate.controller;

import iwm_ko.swUpdate.model.SwUpdateVo;
import iwm_ko.swUpdate.service.SwUpdateService;
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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/swupdate")
public class SwUpdateController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "iwm_ko.swUpdate.SwUpdateService")
    private SwUpdateService swUpdateService;
    @Autowired
    private UserHistoryService userHistoryService;

//    private String gui_code;
//
//    @PostConstruct
//    public void init() throws Exception {
//        gui_code = userHistoryService.searchGuiCode("swupdate");
//    }

    /**
     * 페이지 로딩
     */
    @RequestMapping("swupdate.do")
    public String swUpdate(@ModelAttribute("swUpdate") SwUpdateVo swUpdateVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {

        String sessionCompId = (String)session.getAttribute("comp_id");
        swUpdateVo.setComp_id(sessionCompId);
        model.addAttribute("sessionCompId",sessionCompId);

        List<SwUpdateVo> compList = this.swUpdateService.selectCompList(swUpdateVo);
        model.addAttribute("compList",compList);

        // list검색
        List<SwUpdateVo> swUpdateList = this.swUpdateService.selectSwUpdateList(swUpdateVo);
        model.addAttribute("swUpdateList", swUpdateList);

        // 로그 기록
        UserHistoryVo userHistory = new UserHistoryVo();
        userHistory.setGui_code("502");
        userHistory.setJob_name("조회");
        userHistory.setGui_remark("Search Data");
        userHistory.setUser_id((String) session.getAttribute("user_id"));
        userHistory.setSql_str("");
        userHistoryService.insertUserHistory(userHistory);

        //search data return
        model.addAttribute("swUpdateVo", swUpdateVo);

        return "swupdate/swupdate";
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

            List<SwUpdateVo> shipNameList = swUpdateService.shipNameList(comp_id);

            resultMap.put("shipNameList", shipNameList);
            resultMap.put("result", "success");
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "error");
        }

        return resultMap;
    }

    /**
     * 등록
     */
    @ResponseBody
    @RequestMapping(value="swUpdateAdd.ajax", method= RequestMethod.POST)
    public HashMap<String, Object> swUpdateAdd(SwUpdateVo swUpdateVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        swUpdateVo.setComp_id((String) session.getAttribute("comp_id"));
        try {
            //중복검사
            int chkSch = this.swUpdateService.chkSch(swUpdateVo).size();
            if(chkSch > 0) {
                resultMap.put("result", "-2");
            }else {
                //시퀀스 설정
                swUpdateVo.setAdd_seq(this.swUpdateService.selectSeq());

                //insert
                int insResult = this.swUpdateService.insertSwUpdate(swUpdateVo);
                insResult += this.swUpdateService.insertSwUpdateSub(swUpdateVo);

                String sql = "insert into TB_SW_SCH (SEQ_SW_SCH,\n" +
                        " SCH_NAME, SCH_UNIT, SCH_INTERVAL,SCH_HOUR, SCH_MIN ,MODIFY_DATE)values ("
                        +swUpdateVo.getAdd_seq() +"," + swUpdateVo.getAdd_sch() + "," + swUpdateVo.getAdd_unit() + ","+
                        swUpdateVo.getAdd_interval() + ",";

                if (swUpdateVo.getAdd_hour() == null){
                    sql += 0+",";
                }else if (swUpdateVo.getAdd_hour() != null){
                    sql += swUpdateVo.getAdd_hour() + ",";
                }
                sql += swUpdateVo.getAdd_min() + ", NOW());";
                // 로그 기록
                UserHistoryVo userHistory = new UserHistoryVo();
                userHistory.setGui_code("502");
                userHistory.setJob_name("추가");
                userHistory.setGui_remark("Schedule name : " +swUpdateVo.getAdd_sch() + ", Unit : " +swUpdateVo.getAdd_unit() + " , Period : " +swUpdateVo.getAdd_interval() );
                userHistory.setUser_id((String) session.getAttribute("user_id"));
                userHistory.setSql_str(sql);


                userHistoryService.insertUserHistory(userHistory);

                if(insResult > 1) {

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
    @RequestMapping(value = "swUpdateSetUpdateData.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> swUpdateSetUpdateData(@RequestParam HashMap<String, Object> req, final HttpSession session) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            SwUpdateVo swUpdateVo = this.swUpdateService.selectSwUpdateInfo(req);

            resultMap.put("swUpdateVo", swUpdateVo);
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
    @RequestMapping(value="swUpdateUpdate.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> swUpdateUpdate(SwUpdateVo swUpdateVo,HttpServletRequest request, final HttpSession session,Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();


        try {
            //update
            int updResult = this.swUpdateService.updateSwUpdate(swUpdateVo);

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("502");
            userHistory.setJob_name("수정");
            userHistory.setGui_remark("Schedule name : "+swUpdateVo.getUpd_sch()+", Unit : "+swUpdateVo.getUpd_unit()+", Period : "+swUpdateVo.getUpd_interval());
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            String sql = "update TB_SW_SCH set SCH_NAME= " + swUpdateVo.getUpd_sch() + " , SCH_UNIT= " +swUpdateVo.getUpd_unit()
                    +", SCH_INTERVAL= " + swUpdateVo.getUpd_interval() + ",SCH_HOUR=";

            if (swUpdateVo.getUpd_hour() == null){
                sql += "0 ,";
            } else if (swUpdateVo.getUpd_hour() != null){
                sql += swUpdateVo.getUpd_hour() + " ,";
            }
            sql += "SCH_MIN = " + swUpdateVo.getUpd_min() +", MODIFY_DATE=NOW(),GUI_FLAG='Y' where SEQ_SW_SCH= " + swUpdateVo.getSeq_sw_sch();
            userHistory.setSql_str(sql);

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


    /**
     *  삭제
     */
    @ResponseBody
    @RequestMapping(value="swUpdateDelete.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> swUpdateDelete(SwUpdateVo swUpdateVo,HttpServletRequest request, final HttpSession session,Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            //update
            int updResult = this.swUpdateService.deleteSwUpdate(swUpdateVo);

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("502");
            userHistory.setJob_name("삭제");
            userHistory.setGui_remark("Delete Schedule");
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("delete from TB_SW_SCH_SHIP where SEQ_SW_SCH=" + swUpdateVo.getSeq_sw_sch());

            userHistoryService.insertUserHistory(userHistory);
            updResult += this.swUpdateService.deleteSwUpdateSub(swUpdateVo);
            if(updResult > 1) {
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
