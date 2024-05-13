package iwm_ko.dport.controller;

import iwm_ko.dport.model.DportVo;
import iwm_ko.dport.service.DportService;
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
@RequestMapping("/dport")
public class DportController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "iwm_ko.dport.DportService")
    private DportService dportService;

    @Autowired
    private UserHistoryService userHistoryService;

//    private String gui_code;
//
//    @PostConstruct
//    public void init() throws Exception {
//        gui_code = userHistoryService.searchGuiCode("dport");
//    }


    /**
     * 페이지 로딩
     */
    @RequestMapping("dport.do")
    public String crewInfo(@ModelAttribute("dportVo") DportVo dportVo, HttpServletRequest request, final HttpSession session, Model model)throws Exception{

        String sessionCompId = (String)session.getAttribute("comp_id");
        dportVo.setComp_id(sessionCompId);
        model.addAttribute("sessionCompId",sessionCompId);

        List<DportVo> dportList = this.dportService.selectDportList(dportVo);
        model.addAttribute("dportList",dportList);



        // 로그 기록
        UserHistoryVo userHistory = new UserHistoryVo();
        userHistory.setGui_code("710");
        userHistory.setJob_name("검색");
        userHistory.setGui_remark("Data Search");
        userHistory.setUser_id((String) session.getAttribute("user_id"));
        userHistory.setSql_str("");
        userHistoryService.insertUserHistory(userHistory);

        model.addAttribute("dportVo", dportVo);


        return "dport/dport";
    }


    /**
     * 등록
     */
    @ResponseBody
    @RequestMapping(value="dportAdd.ajax", method= RequestMethod.POST)
    public HashMap<String, Object> dportAdd(DportVo dportVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            int insResult = 0;
            //중복검사
            int chkDport = this.dportService.chkDport(dportVo).size();
            if(chkDport > 0) {
                resultMap.put("result", "-2");
            }else {
                insResult = this.dportService.insertDport(dportVo);

                //로그 기록
                UserHistoryVo userHistory = new UserHistoryVo();
                userHistory.setGui_code("710");
                userHistory.setJob_name("추가");
                userHistory.setGui_remark("Port Range : "+dportVo.getAdd_from()+" ~ "+dportVo.getAdd_to());
                userHistory.setUser_id((String) session.getAttribute("user_id"));
                userHistory.setSql_str("insert into TB_DEFAULT_PORT (PORT_FROM, PORT_TO, MODIFY_DATE) values (" + dportVo.getAdd_from()+ ", "+dportVo.getAdd_to()+ ", NOW())");
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
    @RequestMapping(value="dportDelete.ajax", method=RequestMethod.POST)
    public HashMap<String, Object> dportDelete(DportVo dportVo, HttpServletRequest request, final HttpSession session, Model model) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            //update

            int updResult = this.dportService.deleteDport(dportVo);

            // 로그 기록
            UserHistoryVo userHistory = new UserHistoryVo();
            userHistory.setGui_code("710");
            userHistory.setJob_name("삭제");
            userHistory.setGui_remark("Port : "+dportVo.getPort_from());
            userHistory.setUser_id((String) session.getAttribute("user_id"));
            userHistory.setSql_str("delete from TB_DEFAULT_PORT where PORT_FROM=" + dportVo.getPort_from());

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
