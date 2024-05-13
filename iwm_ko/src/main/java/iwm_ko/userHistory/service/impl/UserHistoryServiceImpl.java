package iwm_ko.userHistory.service.impl;

import iwm_ko.userHistory.dao.UserHistoryDao;
import iwm_ko.userHistory.model.UserHistoryVo;
import iwm_ko.userHistory.service.UserHistoryService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("iwm_ko.userHistory.userHistoryService")
public class UserHistoryServiceImpl implements UserHistoryService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="iwm_ko.userHistory.userHistoryDao")
    private UserHistoryDao userHistoryDao;

    @Override
    @Transactional
    public void insertUserHistory(UserHistoryVo userHistory) throws Exception {
        try {
            logger.info("Starting transaction for insertUserHistory");
            userHistoryDao.insertUserHistory(userHistory);
            logger.info("Transaction completed successfully for insertUserHistory");
        } catch (Exception e) {
            logger.error("Transaction failed for insertUserHistory", e);
            throw e;
        }
    }

//    @Override
//    public String searchGuiCode(String menu) throws Exception{
//        return this.userHistoryDao.searchGuiCode(menu);
//    }

}
