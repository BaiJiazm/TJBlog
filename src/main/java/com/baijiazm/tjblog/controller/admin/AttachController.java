package com.baijiazm.tjblog.controller.admin;

import com.baijiazm.tjblog.controller.BaseController;
import com.baijiazm.tjblog.service.ILogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 附件管理
 */
@Controller
@RequestMapping("admin/attach")
public class AttachController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachController.class);

    @Resource
    private ILogService logService;

    /**
     * 附件页面
     */
    @GetMapping(value = "")
    public String setting(HttpServletRequest request, ModelMap modelMap) {
        return errorHint(modelMap, "此功能区敬请期待", "/admin/index");
    }
}
