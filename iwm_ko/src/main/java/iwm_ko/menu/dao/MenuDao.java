package iwm_ko.menu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iwm_ko.menu.model.MenuVo;

//@Mapper
@Repository("iwm_ko.menu.menuDao")
public class MenuDao {

	@Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAME_SPACE = "iwm_ko.menu.";

	public List<MenuVo> selectMenuList(MenuVo menuVo) throws Exception {
		List<MenuVo> resList = this.sqlSession.selectList(NAME_SPACE.concat("selectMenuList"), menuVo);
        return resList;
	}
}
