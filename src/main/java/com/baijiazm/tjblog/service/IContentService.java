package com.baijiazm.tjblog.service;

import com.baijiazm.tjblog.model.entity.ContentEntity;
import com.github.pagehelper.PageInfo;

public interface IContentService {

    PageInfo<ContentEntity> getArticleWithPage(String order, int page, int limit);

    /**
     * 发布文章
     *
     * @param contents
     */
    String publish(ContentEntity contents);

    /**
     * 查询文章返回多条数据
     *
     * @param p     当前页
     * @param limit 每页条数
     * @return ContentEntity
     */
    PageInfo<ContentEntity> getContents(Integer p, Integer limit);

    /**
     * 根据id或slug获取文章
     *
     * @param id id
     * @return ContentEntity
     */
    ContentEntity getContents(String id);

    /**
     * 根据主键更新点击次数
     *
     * @param contentEntity contentEntity
     */
    void updateHitsByCid(ContentEntity contentEntity);

    void updateCommentsNumberById(ContentEntity contentEntity);

//    /**
//     * 查询分类/标签下的文章归档
//     * @param mid mid
//     * @param page page
//     * @param limit limit
//     * @return ContentEntity
//     */
//    PageInfo<ContentEntity> getArticles(Integer mid, int page, int limit);
//
//    /**
//     * 搜索、分页
//     * @param keyword keyword
//     * @param page page
//     * @param limit limit
//     * @return ContentEntity
//     */
//    PageInfo<ContentEntity> getArticles(String keyword,Integer page,Integer limit);
//
//
//    /**
//     * @param commentVoExample
//     * @param page
//     * @param limit
//     * @return
//     */
//    PageInfo<ContentEntity> getArticlesWithpage(ContentEntity commentVoExample, Integer page, Integer limit);

    /**
     * //     * 根据文章id删除
     * //     * @param cid
     * //
     */
    String deleteByCid(Integer cid);

//
//    /**
//     * 编辑文章
//     * @param contents
//     */
//    String updateArticle(ContentEntity contents);
//
//
//    /**
//     * 更新原有文章的category
//     * @param ordinal
//     * @param newCatefory
//     */
//    void updateCategory(String ordinal,String newCatefory);
}
