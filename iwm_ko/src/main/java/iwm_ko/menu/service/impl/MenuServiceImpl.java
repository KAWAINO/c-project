package iwm_ko.menu.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import iwm_ko.menu.dao.MenuDao;
import iwm_ko.menu.model.MenuVo;
import iwm_ko.menu.service.MenuService;
import jakarta.annotation.Resource;

@Service("iwm_ko.menu.menuService")
public class MenuServiceImpl implements MenuService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="iwm_ko.menu.menuDao")
    private MenuDao menuDao;

	@Override
	public List<MenuVo> selectMenuList(MenuVo menuVo) throws Exception {
		List<MenuVo> resList = this.menuDao.selectMenuList(menuVo);   
		return resList;
	}
}
