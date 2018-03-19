package com.baijiazm.tjblog.mapper;

import com.baijiazm.tjblog.model.entity.CommentEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

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

    String countByOwnerId="select count(*) from t_comment where owner_id=#{ownerId} ";
    @Select(countByOwnerId)
    Long countByOwnerId(int ownerId);
}
