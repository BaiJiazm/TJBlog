package com.baijiazm.tjblog.controller;

import com.baijiazm.tjblog.model.entity.UserEntity;
import com.baijiazm.tjblog.utils.MapCache;
import com.baijiazm.tjblog.utils.MyUtils;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseController {

    protected MapCache cache = MapCache.single();

    /**
     * 获取请求绑定的登录对象
     *
     * @param request
     * @return
     */
    public UserEntity user(HttpServletRequest request) {
        return MyUtils.getLoginUser(request);
    }

    public static String THEME = "themes/default";

    /**
     * 主页的页面主题
     *
     * @param viewName
     * @return
     */
    public String render(String viewName) {
        return THEME + "/" + viewName;
    }

    public BaseController title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
        return this;
    }

    public BaseController keywords(HttpServletRequest request, String keywords) {
        request.setAttribute("keywords", keywords);
        return this;
    }

    public Integer getUid(HttpServletRequest request) {
        return this.user(request).getId();
    }

    public String render_404() {
        return "comm/error_404";
    }

    public String errorHint(ModelMap modelMap, String info, String backLink) {
        modelMap.addAttribute("info", info);
        modelMap.addAttribute("backLink", backLink);
        return this.render("hintPage");
    }
}
