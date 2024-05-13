package iwm_ko.menu.service;

import java.util.List;

import iwm_ko.menu.model.MenuVo;

public interface MenuService {

	List<MenuVo> selectMenuList(MenuVo menuVo) throws Exception;

}
