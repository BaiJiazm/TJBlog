package com.baijiazm.tjblog.controller.admin;

import com.baijiazm.tjblog.controller.BaseController;
import com.baijiazm.tjblog.exception.TipException;
import com.baijiazm.tjblog.model.StatisticsVO;
import com.baijiazm.tjblog.model.entity.CommentEntity;
import com.baijiazm.tjblog.model.entity.ContentEntity;
import com.baijiazm.tjblog.model.entity.LogEntity;
import com.baijiazm.tjblog.service.ISiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller("adminIndexController")
@RequestMapping("/admin")
@Transactional(rollbackFor = TipException.class)
public class IndexController extends BaseController {

    @Autowired
    private ISiteService siteService;

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

        return "/admin/index";
    }
}
