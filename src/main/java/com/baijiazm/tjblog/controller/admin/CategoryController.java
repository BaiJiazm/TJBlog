package com.baijiazm.tjblog.controller.admin;

import com.baijiazm.tjblog.controller.BaseController;
import com.baijiazm.tjblog.dto.Types;
import com.baijiazm.tjblog.model.entity.MetaEntity;
import com.baijiazm.tjblog.service.IMetaService;
import com.baijiazm.tjblog.webConst.WebConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 */
@Controller
@RequestMapping("admin/category")
public class CategoryController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Resource
    private IMetaService metasService;

    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
        List<MetaEntity> categories = metasService.getMetaList(Types.CATEGORY.getType(), null, WebConst.MAX_POSTS);
        List<MetaEntity> tags = metasService.getMetaList(Types.TAG.getType(), null, WebConst.MAX_POSTS);
        request.setAttribute("categories", categories);
        request.setAttribute("tags", tags);
        return "admin/category";
    }

    @PostMapping(value = "save")
    public String saveCategory(@RequestParam String cname, @RequestParam Integer mid, ModelMap modelMap) {
        try {
            metasService.saveMeta(Types.CATEGORY.getType(), cname, mid);
        } catch (Exception e) {
            return errorHint(modelMap, "分类保存失败", "/admin/category");
        }
        return errorHint(modelMap, "分类保存成功", "/admin/category");
    }

    @RequestMapping(value = "delete")
    public String delete(@RequestParam int mid, ModelMap modelMap) {
        try {
            metasService.delete(mid);
        } catch (Exception e) {
            String msg = "删除失败";
            LOGGER.error(msg, e);
            return errorHint(modelMap, msg, "/admin/category");
        }
        return errorHint(modelMap, "删除成功", "/admin/category");
    }
}
