package com.baijiazm.tjblog.mapper;

import com.baijiazm.tjblog.model.entity.ContentEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ContentMapper {

    String selectContentsOrderByCreatedDesc = "select * from t_content " +
            " order by created desc limit #{limit} offset #{begin}";

    @Select(selectContentsOrderByCreatedDesc)
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "allowComment", column = "allow_comment"),
            @Result(property = "allowPing", column = "allow_ping"),
            @Result(property = "allowFeed", column = "allow_feed")
    })
    List<ContentEntity> selectContentsOrderByCreatedDesc(@Param("begin") int begin,
                                                         @Param("limit") int limit);

    String countByType = "select count(*) from t_content where type=#{type}";

    @Select(countByType)
    Long countByType(@Param("type") String type);


}
