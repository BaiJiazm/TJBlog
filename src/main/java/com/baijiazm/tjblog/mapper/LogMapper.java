package com.baijiazm.tjblog.mapper;

import com.baijiazm.tjblog.model.entity.LogEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LogMapper {

    String insertLog = "insert into t_log(id, action, data, author_id, ip, created) " +
            " values(#{log.id}, #{log.action},#{log.data},#{log.authorId},#{log.ip},#{log.created})";

    @Insert(insertLog)
    void insertLog(@Param("log") LogEntity log);

    String selectLogsOffsetLimit = "select * from t_log order by created desc limit #{limit} offset #{offset}";

    @Select(selectLogsOffsetLimit)
    @Results({
            @Result(property = "authorId", column = "author_id")
    })
    List<LogEntity> selectLogsOffsetLimit(@Param("offset") int offset,
                                          @Param("limit") int limit);
}
