package com.baijiazm.tjblog.service.impl;

import com.baijiazm.tjblog.exception.TipException;
import com.baijiazm.tjblog.mapper.CommentMapper;
import com.baijiazm.tjblog.model.entity.CommentEntity;
import com.baijiazm.tjblog.model.entity.ContentEntity;
import com.baijiazm.tjblog.service.ICommentService;
import com.baijiazm.tjblog.service.IContentService;
import com.baijiazm.tjblog.utils.DateKit;
import com.baijiazm.tjblog.utils.MyUtils;
import com.baijiazm.tjblog.webConst.WebConst;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private IContentService contentService;

    @Override
    @Transactional
    public String insertComment(CommentEntity comments) {
        if (null == comments) {
            return "评论对象为空";
        }
        if (StringUtils.isBlank(comments.getAuthor())) {
            comments.setAuthor("热心网友");
        }
        if (StringUtils.isNotBlank(comments.getMail()) && !MyUtils.isEmail(comments.getMail())) {
            return "请输入正确的邮箱格式";
        }
        if (StringUtils.isBlank(comments.getContent())) {
            return "评论内容不能为空";
        }
        if (comments.getContent().length() < 5 || comments.getContent().length() > 2000) {
            return "评论字数在5-2000个字符";
        }
        if (null == comments.getContentId()) {
            return "评论文章不能为空";
        }
        ContentEntity contents = contentService.getContents(comments.getContentId().toString());
        if (null == contents) {
            return "不存在的文章";
        }
        comments.setOwnerId(contents.getAuthorId());
        comments.setStatus("not_audit");
        comments.setCreated(DateKit.getCurrentUnixTime());
        commentMapper.insertOneComment(comments);

        ContentEntity temp = new ContentEntity();
        temp.setId(contents.getId());
        Integer commentsNumber = contents.getCommentsNumber();
        temp.setCommentsNumber(commentsNumber == null ? 1 : commentsNumber + 1);
        contentService.updateCommentsNumberById(temp);

        return WebConst.SUCCESS_RESULT;
    }

    @Override
    public PageInfo<CommentEntity> getComments(Integer cid, int page, int limit) {
        if (null != cid) {
            PageHelper.startPage(page, limit);
            List<CommentEntity> parents = commentMapper.selectApprovedByCidOrderById(cid.toString());
            PageInfo<CommentEntity> commentPaginator = new PageInfo<>(parents);
//            PageInfo<CommentEntity> returnBo = copyPageInfo(commentPaginator);
//            if (parents.size() != 0) {
//                List<CommentEntity> comments = new ArrayList<>(parents.size());
//                parents.forEach(parent -> {
//                    CommentEntity comment = new CommentEntity();
//                    comments.add(comment);
//                });
//                returnBo.setList(comments);
//            }
//            return returnBo;
            return commentPaginator;
        }
        return null;
    }

    @Override
    public PageInfo<CommentEntity> getCommentsWithPage(String uid, int page, int limit) {
        PageHelper.startPage(page, limit);
        List<CommentEntity> commentEntitys = commentMapper.selectByUidOrderById(uid);
        PageInfo<CommentEntity> pageInfo = new PageInfo<>(commentEntitys);
        return pageInfo;
    }

    @Override
    @Transactional
    public void update(CommentEntity comments) {
        if (null != comments && null != comments.getId()) {
            commentMapper.updateStatus(comments);
        }
    }

    @Override
    @Transactional
    public void delete(Integer coid, Integer cid) {
        if (null == coid) {
            throw new TipException("主键为空");
        }
        commentMapper.deleteByPrimaryKey(coid);
        ContentEntity contents = contentService.getContents(cid + "");
        if (null != contents) {
            ContentEntity temp = new ContentEntity();
            temp.setId(cid);
            Integer commentsNumber = contents.getCommentsNumber();
            temp.setCommentsNumber(commentsNumber == null || commentsNumber <= 0 ? 0 : commentsNumber - 1);
            contentService.updateCommentsNumberById(temp);
        }
    }

    @Override
    public CommentEntity getCommentById(Integer coid) {
        if (null != coid) {
            return commentMapper.selectByPrimaryKey(coid);
        }
        return null;
    }

    /**
     * copy原有的分页信息，除数据
     *
     * @param ordinal
     * @param <T>
     * @return
     */
    private <T> PageInfo<T> copyPageInfo(PageInfo ordinal) {
        PageInfo<T> returnBo = new PageInfo<T>();
        returnBo.setPageSize(ordinal.getPageSize());
        returnBo.setPageNum(ordinal.getPageNum());
        returnBo.setEndRow(ordinal.getEndRow());
        returnBo.setTotal(ordinal.getTotal());
        returnBo.setHasNextPage(ordinal.isHasNextPage());
        returnBo.setHasPreviousPage(ordinal.isHasPreviousPage());
        returnBo.setIsFirstPage(ordinal.isIsFirstPage());
        returnBo.setIsLastPage(ordinal.isIsLastPage());
        returnBo.setNavigateFirstPage(ordinal.getNavigateFirstPage());
        returnBo.setNavigateLastPage(ordinal.getNavigateLastPage());
        returnBo.setNavigatepageNums(ordinal.getNavigatepageNums());
        returnBo.setSize(ordinal.getSize());
        returnBo.setPrePage(ordinal.getPrePage());
        returnBo.setNextPage(ordinal.getNextPage());
        return returnBo;
    }
}
