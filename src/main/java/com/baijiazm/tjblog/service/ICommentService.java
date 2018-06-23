package com.baijiazm.tjblog.service;

import com.baijiazm.tjblog.model.entity.CommentEntity;
import com.github.pagehelper.PageInfo;

public interface ICommentService {

    /**
     * 保存对象
     *
     * @param CommentEntity
     */
    String insertComment(CommentEntity CommentEntity);

    /**
     * 获取文章下的评论
     *
     * @param cid
     * @param page
     * @param limit
     * @return CommentEntity
     */
    PageInfo<CommentEntity> getComments(Integer cid, int page, int limit);

    /**
     * 获取文章下的评论
     *
     * @param commentEntity
     * @param page
     * @param limit
     * @return CommentEntity
     */
//    PageInfo<CommentEntity> getCommentsWithPage(CommentEntity commentEntity, int page, int limit);

    /**
     * 根据主键查询评论
     *
     * @param coid
     * @return
     */
//    CommentEntity getCommentById(Integer coid);

    /**
     * 删除评论，暂时没用
     *
     * @param coid
     * @param cid
     * @throws Exception
     */
//    void delete(Integer coid, Integer cid);

    /**
     * 更新评论状态
     *
     * @param comments
     */
//    void update(CommentEntity comments);

}
