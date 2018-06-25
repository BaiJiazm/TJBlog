package com.baijiazm.tjblog.mapper;

import com.baijiazm.tjblog.model.entity.ContentEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ContentMapper {

    String selectContentsByTypeOrderLimitBegin1 = "select * from t_content where type=#{type}" +
            " order by #{order} limit #{limit} offset #{offset}";

    @Select(selectContentsByTypeOrderLimitBegin1)
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "allowComment", column = "allow_comment"),
            @Result(property = "allowPing", column = "allow_ping"),
            @Result(property = "allowFeed", column = "allow_feed")
    })
    List<ContentEntity> selectContentsByTypeOrderLimitBegin1(@Param("type") String type,
                                                             @Param("order") String order,
                                                             @Param("offset") int offset,
                                                             @Param("limit") int limit);


    String selectCountByType = "select count(*) from t_content where type=#{type}";

    @Select(selectCountByType)
    Long selectCountByType(@Param("type") String type);


    String selectByTypeStatus = "select * from t_content where type=#{type} and status=#{status} order by created desc";

    @Select(selectByTypeStatus)
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "allowComment", column = "allow_comment"),
            @Result(property = "allowPing", column = "allow_ping"),
            @Result(property = "allowFeed", column = "allow_feed")
    })
    List<ContentEntity> selectByTypeStatus(@Param("type") String type,
                                           @Param("status") String status);


    String selectCountBySlug = "select count(*) from t_content where slug=#{slug}";

    @Select(selectCountBySlug)
    int selectCountBySlug(@Param("slug") String slug);


    String insertOneContent = "insert into t_content(" +
            "`id`, `title`, `slug`, `created`," +
            " `modified`, `content`, `author_id`, `type`," +
            " `status`, `tags`, `categories`, `hits`, " +
            "`comments_number`, `allow_comment`, `allow_ping`, `allow_feed`) " +
            " values( " +
            " #{content.id}, #{content.title}, #{content.slug}, #{content.created}, " +
            " #{content.modified}, #{content.content}, #{content.authorId}, #{content.type}, " +
            " #{content.status}, #{content.tags}, #{content.categories}, #{content.hits}, " +
            " #{content.commentsNumber}, #{content.allowComment}, #{content.allowPing}, #{content.allowFeed} " +
            " )";

    @Insert(insertOneContent)
    @Options(useGeneratedKeys = true, keyProperty = "content.id")
    void insertOneContent(@Param("content") ContentEntity content);


    String selectByPrimaryKey = "select * from t_content where id=#{cid}";

    @Select(selectByPrimaryKey)
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "allowComment", column = "allow_comment"),
            @Result(property = "allowPing", column = "allow_ping"),
            @Result(property = "allowFeed", column = "allow_feed")
    })
    ContentEntity selectByPrimaryKey(@Param("cid") Integer cid);


    String updateByPrimaryKey = "update t_content set `id`=#{content.id}, " +
            "`title`= #{content.title}, `slug`=#{content.slug}, `created`=#{content.created}," +
            " `modified`=#{content.modified},`content`= #{content.content}, `author_id`=#{content.authorId}, `type`=#{content.type}, " +
            " `status`=#{content.status}, `tags`=#{content.tags}, `categories`=#{content.categories}, `hits`=#{content.hits}, " +
            " `comments_number`=#{content.commentsNumber}, `allow_comment`=#{content.allowComment}, `allow_ping`=#{content.allowPing}, `allow_feed`=#{content.allowFeed} " +
            " where id=#{content.id}";

    @Update(updateByPrimaryKey)
    int updateByPrimaryKey(@Param("content") ContentEntity content);

    String selectBySlug = "select * from t_content where slug=#{slug}";

    @Select(selectBySlug)
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "allowComment", column = "allow_comment"),
            @Result(property = "allowPing", column = "allow_ping"),
            @Result(property = "allowFeed", column = "allow_feed")
    })
    List<ContentEntity> selectBySlug(@Param("slug") String slug);


    String deleteByPrimaryKey = "delete from t_content where id=#{cid}";

    @Delete(deleteByPrimaryKey)
    void deleteByPrimaryKey(@Param("cid") Integer cid);


    String updateCommentsNumberById = "update t_content set `comments_number`=#{content.commentsNumber} " +
            " where id=#{content.id}";

    @Update(updateCommentsNumberById)
    int updateCommentsNumberById(@Param("content") ContentEntity content);


    String updateHitsById = "update t_content set `hits`=#{content.hits} " +
            " where id=#{content.id}";

    @Update(updateHitsById)
    int updateHitsById(@Param("content") ContentEntity content);


    String updateCategoriesByCid = "update t_content set `categories`=#{content.categories} " +
            " where id=#{content.id}";

    @Update(updateCategoriesByCid)
    int updateCategoriesByCid(@Param("content") ContentEntity content);

}
