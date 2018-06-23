package com.baijiazm.tjblog.controller.admin;

import com.baijiazm.tjblog.controller.BaseController;
import com.baijiazm.tjblog.dto.LogActions;
import com.baijiazm.tjblog.exception.TipException;
import com.baijiazm.tjblog.model.StatisticsVO;
import com.baijiazm.tjblog.model.entity.CommentEntity;
import com.baijiazm.tjblog.model.entity.ContentEntity;
import com.baijiazm.tjblog.model.entity.LogEntity;
import com.baijiazm.tjblog.model.entity.UserEntity;
import com.baijiazm.tjblog.service.ILogService;
import com.baijiazm.tjblog.service.ISiteService;
import com.baijiazm.tjblog.service.IUserService;
import com.baijiazm.tjblog.utils.MyUtils;
import com.baijiazm.tjblog.webConst.WebConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller("adminIndexController")
@RequestMapping("/admin")
@Transactional(rollbackFor = TipException.class)
public class IndexController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ISiteService siteService;

    @Resource
    private ILogService logService;

    @Resource
    private IUserService userService;

    @GetMapping(value = {"", "/index"})
    public String index(HttpServletRequest request) {
        StatisticsVO statisticsVO = siteService.getStatistics();
        List<ContentEntity> contents = siteService.getRecentContents(5);
        List<CommentEntity> comments = siteService.getRecentComments(5);
        List<LogEntity> logEntitys = siteService.getLogs(1, 5);

        request.setAttribute("statistics", statisticsVO);
        request.setAttribute("articles", contents);
        request.setAttribute("comments", comments);
        request.setAttribute("logs", logEntitys);

        return "admin/index";
    }

    /**
     * 个人设置页面
     */
    @GetMapping(value = "profile")
    public String profile() {
        return "admin/profile";
    }


    /**
     * 保存个人信息
     */
    @PostMapping(value = "/profile")
    public String saveProfile(@RequestParam String screenName, @RequestParam String email, HttpServletRequest request, HttpSession session) {
        UserEntity users = this.user(request);
        if (StringUtils.isNotBlank(screenName) && StringUtils.isNotBlank(email)) {
            userService.updateScreenNameEmailByUid(screenName, email, users.getId());
            logService.insertLog(LogActions.UP_INFO.getAction(), screenName + " " + email, request.getRemoteAddr(), this.getId(request));

            //更新session中的数据
            UserEntity original = (UserEntity) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setScreenName(screenName);
            original.setEmail(email);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY, original);
        }
        return "admin/index";
    }

    /**
     * 修改密码页面
     */
    @GetMapping(value = "password")
    public String password() {
        return "admin/password";
    }

    /**
     * 修改密码
     */
    @PostMapping(value = "/password")
    public String upPwd(@RequestParam String oldPassword, @RequestParam String password,
                        HttpServletRequest request, HttpSession session, ModelMap modelMap) {
        UserEntity users = this.user(request);
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(password)) {
            modelMap.addAttribute("info", "请确认信息输入完整");
            modelMap.addAttribute("backLink", "password");
            return "admin/hintPage";
        }

        if (!users.getPassword().equals(MyUtils.MD5encode(users.getName() + oldPassword))) {
            modelMap.addAttribute("info", "旧密码错误");
            modelMap.addAttribute("backLink", "password");
            return "admin/hintPage";
        }
        if (password.length() < 6 || password.length() > 14) {
            modelMap.addAttribute("info", "请输入6-14位密码");
            modelMap.addAttribute("backLink", "password");
            return "admin/hintPage";
        }

        try {
            UserEntity temp = new UserEntity();
            temp.setId(users.getId());
            String pwd = MyUtils.MD5encode(users.getName() + password);
            temp.setPassword(pwd);
            userService.updatePassWordByUid(temp);
            logService.insertLog(LogActions.UP_PWD.getAction(), null, request.getRemoteAddr(), this.getId(request));

            //更新session中的数据
            UserEntity original = (UserEntity) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setPassword(pwd);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY, original);
            return "admin/index";
        } catch (Exception e) {
            String msg = "密码修改失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            modelMap.addAttribute("info", msg);
            modelMap.addAttribute("backLink", "password");
            return "admin/hintPage";
        }
    }

    /**
     * 获取请求绑定的登录对象
     *
     * @param request
     * @return
     */
    public UserEntity user(HttpServletRequest request) {
        return MyUtils.getLoginUser(request);
    }

    public Integer getId(HttpServletRequest request) {
        return this.user(request).getId();
    }

    public String render_404() {
        return "comm/error_404";
    }
}
