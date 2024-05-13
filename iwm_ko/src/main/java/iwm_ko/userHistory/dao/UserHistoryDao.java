package iwm_ko.userHistory.dao;

import iwm_ko.userHistory.model.UserHistoryVo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository("iwm_ko.userHistory.userHistoryDao")
public class UserHistoryDao {
    @Autowired
    private SqlSession sqlSession;

    private final String NAME_SPACE = "iwm_ko.userHistory.";

    public void insertUserHistory(UserHistoryVo userHistoryVo) throws Exception {
        this.sqlSession.selectList(NAME_SPACE.concat("insertUserHistory"),userHistoryVo);
    }

//    public String searchGuiCode(String menu) throws Exception {
//        return this.sqlSession.selectOne(NAME_SPACE.concat("searchGuiCode"),menu);
//    }
}
