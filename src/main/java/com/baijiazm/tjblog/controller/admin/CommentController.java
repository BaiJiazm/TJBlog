package com.baijiazm.tjblog.controller.admin;

import com.baijiazm.tjblog.controller.BaseController;
import com.baijiazm.tjblog.model.entity.CommentEntity;
import com.baijiazm.tjblog.model.entity.UserEntity;
import com.baijiazm.tjblog.service.ICommentService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("admin/comment")
public class CommentController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

    @Resource
    private ICommentService commentsService;

    @GetMapping(value = "")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit, HttpServletRequest request) {
        UserEntity users = this.user(request);
        PageInfo<CommentEntity> commentsPaginator = commentsService.getCommentsWithPage(users.getId().toString(), page, limit);
        request.setAttribute("comments", commentsPaginator);
        return "admin/commentList";
    }

    /**
     * 删除一条评论
     *
     * @param id
     * @return
     */
    @GetMapping(value = {"delete/{id}", "delete/{id}.html"})
    public String delete(@PathVariable Integer id, ModelMap modelMap) {
        try {
            CommentEntity comments = commentsService.getCommentById(id);
            if (null == comments) {
                return errorHint(modelMap, "不存在该评论", "/admin/comment");
            }
            commentsService.delete(id, comments.getContentId());
        } catch (Exception e) {
            String msg = "评论删除失败";
            LOGGER.error(msg, e);
            return errorHint(modelMap, msg, "/admin/comment");
        }
        return errorHint(modelMap, "删除评论成功", "/admin/comment");
    }

    /**
     * 更新评论状态
     *
     * @param id
     * @return
     */
    @GetMapping(value = {"status/{id}", "status/{id}.html"})
    public String status(@PathVariable Integer id, ModelMap modelMap) {
        try {
            CommentEntity comments = commentsService.getCommentById(id);
            if (comments != null) {
                comments.setId(id);
                comments.setStatus("approved");
                commentsService.update(comments);
            } else {
                return errorHint(modelMap, "操作失败", "/admin/comment");
            }
        } catch (Exception e) {
            String msg = "操作失败";
            return errorHint(modelMap, "操作失败", "/admin/comment");
        }
        return errorHint(modelMap, "操作成功", "/admin/comment");
    }

}
