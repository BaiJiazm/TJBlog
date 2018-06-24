package com.baijiazm.tjblog.mapper;

import com.baijiazm.tjblog.model.entity.CommentEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

//CREATE TABLE `t_comment` (
//        `id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
//        `content_id` int(10) unsigned DEFAULT '0' ,
//        `created` int(10) unsigned DEFAULT '0' ,
//        `author` varchar(200) DEFAULT NULL ,
//        `author_id` int(10) unsigned DEFAULT '0' ,
//        `owner_id` int(10) unsigned DEFAULT '0' ,
//        `mail` varchar(200) DEFAULT NULL ,
//        `url` varchar(200) DEFAULT NULL ,
//        `ip` varchar(64) DEFAULT NULL  ,
//        `agent` varchar(200) DEFAULT NULL  ,
//        `content` text ,
//        `type` varchar(16) DEFAULT 'comment' ,
//        `status` varchar(16) DEFAULT 'approved' ,
//        `parent` int(10) unsigned DEFAULT '0',
//        PRIMARY KEY (`id`),
//        KEY `content_id` (`content_id`),
//        KEY `created` (`created`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

@Component
public interface CommentMapper {

    String selectCommentsOrderByCreatedDesc = "select * from t_comment " +
            " order by created desc limit #{limit} offset #{begin}";

    @Select(selectCommentsOrderByCreatedDesc)
    @Results(value = {
            @Result(property = "contentId", column = "content_id"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "ownerId", column = "owner_id"),
    })
    List<CommentEntity> selectCommentsOrderByCreatedDesc(@Param("begin") int begin,
                                                         @Param("limit") int limit);


    String countByOwnerId = "select count(*) from t_comment where owner_id=#{ownerId} ";

    @Select(countByOwnerId)
    Long countByOwnerId(int ownerId);


    String selectByUidOrderById = "select * from t_comment where owner_id=#{uid} order by id desc";

    @Select(selectByUidOrderById)
    @Results(value = {
            @Result(property = "contentId", column = "content_id"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "ownerId", column = "owner_id"),
    })
    List<CommentEntity> selectByUidOrderById(@Param("uid") String uid);


    String selectApprovedByCidOrderById = "select * from t_comment where content_id=#{contentId} " +
            " and status='approved' order by id desc";

    @Select(selectApprovedByCidOrderById)
    @Results(value = {
            @Result(property = "contentId", column = "content_id"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "ownerId", column = "owner_id"),
    })
    List<CommentEntity> selectApprovedByCidOrderById(@Param("contentId") String contentId);


    String selectByPrimaryKey = "select * from t_comment where id=#{id}";

    @Select(selectByPrimaryKey)
    @Results(value = {
            @Result(property = "contentId", column = "content_id"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "ownerId", column = "owner_id"),
    })
    CommentEntity selectByPrimaryKey(@Param("id") Integer id);


    String deleteByPrimaryKey = "delete from t_comment where id=#{id}";

    @Delete(deleteByPrimaryKey)
    void deleteByPrimaryKey(@Param("id") Integer id);


    String insertOneComment = " insert into `t_comment` (`id`,`content_id`, `created`,`author` ," +
            " `author_id`,`owner_id`,`mail` ,`url`," +
            " `ip`,`agent` ,`content`,`type`,`status` ,`parent`) " +
            " values( " +
            " #{comment.id}, #{comment.contentId}, #{comment.created}, #{comment.author}, " +
            " #{comment.authorId}, #{comment.ownerId}, #{comment.mail}, #{comment.url}, " +
            " #{comment.ip}, #{comment.agent}, #{comment.content}, #{comment.type}, " +
            " #{comment.status}, #{comment.parent} )";

    @Insert(insertOneComment)
    @Options(useGeneratedKeys = true, keyProperty = "comment.id")
    void insertOneComment(@Param("comment") CommentEntity comment);


    String updateStatus = "update t_comment set `status`=#{comment.status} " +
            " where id=#{comment.id}";

    @Update(updateStatus)
    int updateStatus(@Param("comment") CommentEntity comment);
}
