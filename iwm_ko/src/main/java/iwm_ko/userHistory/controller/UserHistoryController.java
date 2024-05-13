package iwm_ko.userHistory.controller;

import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userHistory")
public class UserHistoryController {

    @Autowired
    private UserHistoryService userHistoryService;

    @PostMapping("/insert")
    public ResponseEntity<?> insertUserHistory(@RequestBody UserHistoryVo userHistory) throws Exception {
        userHistoryService.insertUserHistory(userHistory);
        return ResponseEntity.ok().build();
    }
}

