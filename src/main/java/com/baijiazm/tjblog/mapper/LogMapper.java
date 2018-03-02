package com.baijiazm.tjblog.mapper;

import com.baijiazm.tjblog.entity.LogEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface LogMapper {

    String insertLog = "insert into t_log(id, action, data, author_id, ip, created) " +
            " values(#{log.id}, #{log.action},#{log.data},#{log.authorId},#{log.ip},#{log.created})";

    @Insert(insertLog)
    void insertLog(@Param("log") LogEntity log);
}
