package com.baijiazm.tjblog.controller.admin;

import com.baijiazm.tjblog.controller.BaseController;
import com.baijiazm.tjblog.entity.UserEntity;
import com.baijiazm.tjblog.entity.dto.LogAction;
import com.baijiazm.tjblog.exception.TipException;
import com.baijiazm.tjblog.service.ILogService;
import com.baijiazm.tjblog.service.IUserService;
import com.baijiazm.tjblog.utils.MyUtils;
import com.baijiazm.tjblog.webConst.WebConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin")
public class LogInOutController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogInOutController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private ILogService logService;

    @GetMapping(value = "/login")
    public String login() {
        return "/admin/login";
    }

    @PostMapping(value = "/login")
    public String doLogin(@RequestParam String userName,
                          @RequestParam String password,
                          @RequestParam(required = false) String remember,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          ModelMap modelMap) {

        Integer errorCount = cache.get("loginErrorCount");
        try {
            UserEntity userEntity = userService.login(userName, password);
            request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, userEntity);
            if (StringUtils.isNotBlank(remember)) {
                MyUtils.setCookie(response, userEntity.getId());
            }
            logService.insertLog(LogAction.LOGIN.getAction(), null,
                    request.getRemoteAddr(), userEntity.getId());
        } catch (Exception e) {
            errorCount = null == errorCount ? 1 : errorCount + 1;
            String info = null;
            if (errorCount > 3) {
                info = "您输入密码已经错误超过3次，请10分钟后尝试";
            } else {
                cache.set("loginErrorCount", errorCount, 10 * 60);
                if (e instanceof TipException) {
                    info = e.getMessage();
                } else {
                    info = "登录失败";
                    LOGGER.error(info, e);
                }
            }
            modelMap.addAttribute("info", info);
            modelMap.addAttribute("backLink", "login");
            return "/common/hintInfo";
        }
        return "/admin/index";
    }
}
