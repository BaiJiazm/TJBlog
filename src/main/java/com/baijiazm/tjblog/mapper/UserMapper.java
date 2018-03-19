package com.baijiazm.tjblog.mapper;

import com.baijiazm.tjblog.model.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

//CREATE TABLE `t_user` (
//        `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
//        `name` varchar(32) DEFAULT NULL,
//        `password` varchar(64) DEFAULT NULL,
//        `email` varchar(200) DEFAULT NULL,
//        `home_url` varchar(200) DEFAULT NULL,
//        `screen_name` varchar(32) DEFAULT NULL,
//        `created` int(10) unsigned DEFAULT '0',
//        `activated` int(10) unsigned DEFAULT '0' ,
//        `logged` int(10) unsigned DEFAULT '0',
//        `group_name` varchar(16) DEFAULT 'visitor' ,
//        PRIMARY KEY (`id`),
//        UNIQUE KEY `name` (`name`),
//        UNIQUE KEY `mail` (`email`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

@Component
public interface UserMapper {

    String selectUserById = "select * from t_user where id=#{id}";

    @Select(selectUserById)
    @Results({
            @Result(property = "homeUrl", column = "home_url"),
            @Result(property = "screenName", column = "screen_name"),
            @Result(property = "groupName", column = "group_name")
    })
    UserEntity selectUserById(@Param("id") int id);

    String getSelectUserById = "select count(*) from t_user where name=#{userName}";

    @Select(getSelectUserById)
    int getUserCountByName(@Param("userName") String userName);

    String getAllUsersByNamePassword = "select * from t_user where name=#{userName} and password=#{password}";

    @Select(getAllUsersByNamePassword)
    @Results({
            @Result(property = "homeUrl", column = "home_url"),
            @Result(property = "screenName", column = "screen_name"),
            @Result(property = "groupName", column = "group_name")
    })
    List<UserEntity> getAllUsersByNamePassword(@Param("userName") String userName,
                                               @Param("password") String password);

}
