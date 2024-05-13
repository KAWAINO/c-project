package iwm_ko.traffic.controller;

import iwm_ko.traffic.model.TrafficVo;
import iwm_ko.traffic.service.TrafficService;
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
@RequestMapping("/traffic")
public class TrafficController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "iwm_ko.traffic.TrafficService")
    private TrafficService trafficService;
    @Autowired
    private UserHistoryService userHistoryService;

//    private String gui_code;
//
//    @PostConstruct
//    public void init() throws Exception {
//        gui_code = userHistoryService.searchGuiCode("traffic");
//    }

    /**
     * 페이지 로딩
     */
    @RequestMapping("traffic.do")
    public String traffic(@ModelAttribute("trafficVo") TrafficVo trafficVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {


        String sessionCompId = (String)session.getAttribute("comp_id");
        trafficVo.setComp_id(sessionCompId);
        model.addAttribute("sessionCompId",sessionCompId);

        List<TrafficVo> compList = this.trafficService.selectCompList(trafficVo);
        model.addAttribute("compList",compList);

            // list검색
            List<TrafficVo> trafficList = this.trafficService.selectTrafficList(trafficVo);
            model.addAttribute("trafficList", trafficList);


            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("501");
            userHistory.setJob_name("조회");
            userHistory.setGui_remark("Search Data");
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("");
            userHistoryService.insertUserHistory(userHistory);


        //search data return
        model.addAttribute("trafficVo", trafficVo);


        return "traffic/traffic";
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

            List<TrafficVo> shipNameList = trafficService.shipNameList(comp_id);

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
    @RequestMapping(value="trafficAdd.ajax", method= RequestMethod.POST)
    public HashMap<String, Object> trafficAdd(TrafficVo trafficVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        trafficVo.setComp_id((String) session.getAttribute("comp_id"));
        try {
            //중복검사
            int chkSch = this.trafficService.chkSch(trafficVo).size();
            if(chkSch > 0) {
                resultMap.put("result", "-2");
            }else {
                //시퀀스 설정
                trafficVo.setAdd_seq(this.trafficService.selectSeq());

                //insert
                int insResult = this.trafficService.insertTraffic(trafficVo);
                insResult += this.trafficService.insertTrafficShip(trafficVo);

                String sql = "insert into TB_TRAF_SCH (SEQ_TRAF_SCH,\n" +
                        " SCH_NAME, SCH_UNIT, SCH_INTERVAL,SCH_HOUR, SCH_MIN ,MODIFY_DATE)values ("
                        +trafficVo.getAdd_seq() +"," + trafficVo.getAdd_sch() + "," + trafficVo.getAdd_unit() + ","+
                        trafficVo.getAdd_interval() + ",";

                if (trafficVo.getAdd_hour() == null){
                    sql += 0+",";
                }else if (trafficVo.getAdd_hour() != null){
                    sql += trafficVo.getAdd_hour() + ",";
                }
                sql += trafficVo.getAdd_min() + ", NOW());";
                // 로그 기록
                UserHistoryVo userHistory = new UserHistoryVo();
                userHistory.setGui_code("501");
                userHistory.setJob_name("추가");
                userHistory.setGui_remark("Schedule name : " +trafficVo.getAdd_sch() + ", Unit : " +trafficVo.getAdd_unit() + " , Period : " +trafficVo.getAdd_interval() );
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
    @RequestMapping(value = "trafficSetUpdateData.ajax", method = RequestMethod.POST)
    public HashMap<String, Object> trafficSetUpdateData(@RequestParam HashMap<String, Object> req, final HttpSession session) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            TrafficVo trafficVo = this.trafficService.selectCompInfo(req);



            resultMap.put("trafficVo", trafficVo);
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
    @RequestMapping(value="trafficUpdate.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> trafficUpdate(TrafficVo trafficVo,HttpServletRequest request, final HttpSession session,Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();


        try {
            //update
            int updResult = this.trafficService.updateTraffic(trafficVo);

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("501");
            userHistory.setJob_name("수정");
            userHistory.setGui_remark("Schedule name : "+trafficVo.getUpd_sch()+", Unit : "+trafficVo.getUpd_unit()+", Period : "+trafficVo.getUpd_interval());
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            String sql = "update TB_TRAF_SCH set SCH_NAME= " + trafficVo.getUpd_sch() + " , SCH_UNIT= " +trafficVo.getUpd_unit()
                    +", SCH_INTERVAL= " + trafficVo.getUpd_interval() + ",SCH_HOUR=";

            if (trafficVo.getUpd_hour() == null){
                sql += "0 ,";
            } else if (trafficVo.getUpd_hour() != null){
                sql += trafficVo.getUpd_hour() + " ,";
            }
            sql += "SCH_MIN = " + trafficVo.getUpd_min() +", MODIFY_DATE=NOW(),GUI_FLAG='Y' where SEQ_TRAF_SCH= " + trafficVo.getSeq_traf_sch();
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
        @RequestMapping(value="trafficDelete.ajax", method=RequestMethod.POST)
        public HashMap<String, Object> trafficDelete(TrafficVo trafficVo,HttpServletRequest request, final HttpSession session,Model model) throws Exception {
            HashMap<String, Object> resultMap = new HashMap<String, Object>();

            try {
                //update
                int updResult = this.trafficService.deleteTraffic(trafficVo);

                // 로그 기록
                UserHistoryVo userHistory = new UserHistoryVo();
                userHistory.setGui_code("501");
                userHistory.setJob_name("삭제");
                userHistory.setGui_remark("Delete Schedule");
                userHistory.setUser_id((String) session.getAttribute("user_id"));
                userHistory.setSql_str("delete from TB_TRAF_SCH_SHIP where SEQ_TRAF_SCH=" + trafficVo.getSeq_traf_sch());

                userHistoryService.insertUserHistory(userHistory);
                updResult += this.trafficService.deleteTrafficSub(trafficVo);
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
