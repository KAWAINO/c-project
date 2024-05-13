package iwm_ko.menu.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import iwm_ko.menu.model.MenuVo;
import iwm_ko.menu.service.MenuService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class MenuController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="iwm_ko.menu.menuService")
    private MenuService menuService;
    
    @ModelAttribute("menuList")
    public List<MenuVo> menuList(@ModelAttribute("menuVo") MenuVo menuVo, HttpSession session) throws Exception {
    	
        menuVo.setUserGrade((String) session.getAttribute("user_grade"));

        return this.menuService.selectMenuList(menuVo);
    }
}
