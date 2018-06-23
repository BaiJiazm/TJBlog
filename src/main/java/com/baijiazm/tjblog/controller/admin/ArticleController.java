package com.baijiazm.tjblog.controller.admin;

import com.baijiazm.tjblog.controller.BaseController;
import com.baijiazm.tjblog.dto.LogActions;
import com.baijiazm.tjblog.dto.Types;
import com.baijiazm.tjblog.exception.TipException;
import com.baijiazm.tjblog.model.entity.ContentEntity;
import com.baijiazm.tjblog.model.entity.MetaEntity;
import com.baijiazm.tjblog.model.entity.UserEntity;
import com.baijiazm.tjblog.service.IContentService;
import com.baijiazm.tjblog.service.ILogService;
import com.baijiazm.tjblog.service.IMetaService;
import com.baijiazm.tjblog.webConst.WebConst;
import com.github.pagehelper.PageInfo;
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
import java.util.List;

@Controller("adminArticleController")
@RequestMapping("/admin/article")
@Transactional(rollbackFor = TipException.class)
public class ArticleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private IMetaService metaService;

    @Autowired
    private IContentService contentsService;

    @Resource
    private ILogService logService;

    @GetMapping(value = "")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit,
                        HttpServletRequest request) {
        PageInfo<ContentEntity> contentsPaginator = contentsService.getArticleWithPage("created desc", page, limit);
        request.setAttribute("articles", contentsPaginator);
        return "admin/articleList";
    }

    @GetMapping(value = "/publish")
    public String newArticle(HttpServletRequest request) {
        List<MetaEntity> categories = metaService.getMetasByType(Types.CATEGORY.getType());
        request.setAttribute("categories", categories);
        return "admin/articleEdit";
    }

    @PostMapping(value = "/publish")
    public String publishArticle(ContentEntity content, HttpServletRequest request, ModelMap modelMap) {
        UserEntity user = user(request);
        content.setAuthorId(user.getId());
        content.setType(Types.ARTICLE.getType());
        if (StringUtils.isBlank(content.getCategories())) {
            content.setCategories("default");
        }
        String result = contentsService.publish(content);
        if (!result.equals(WebConst.SUCCESS_RESULT)) {
            modelMap.addAttribute("info", result);
            modelMap.addAttribute("backLink", "articleEdit");
            return "admin/hintPage";
        }
        return "redirect:/admin/article";
    }

    @GetMapping(value = "/{cid}")
    public String editArticle(@PathVariable String cid, HttpServletRequest request) {
        ContentEntity contents = contentsService.getContents(cid);
        request.setAttribute("contents", contents);
        List<MetaEntity> categories = metaService.getMetas(Types.CATEGORY.getType());
        request.setAttribute("categories", categories);
        request.setAttribute("active", "article");
        return "admin/articleEdit";
    }

    @RequestMapping(value = "/delete/{cid}")
    public String delete(@PathVariable int cid, HttpServletRequest request, ModelMap modelMap) {
        String result = contentsService.deleteByCid(cid);
        logService.insertLog(LogActions.DEL_ARTICLE.getAction(), cid + "", request.getRemoteAddr(), this.getUid(request));
        if (!WebConst.SUCCESS_RESULT.equals(result)) {
            modelMap.addAttribute("info", result);
            modelMap.addAttribute("backLink", "articleList");
            return "admin/hintPage";
        }
        return "redirect:/admin/article";
    }

}
