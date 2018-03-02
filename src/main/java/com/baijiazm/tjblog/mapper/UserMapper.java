package com.baijiazm.tjblog.mapper;

import com.baijiazm.tjblog.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper {

    @Select("select count(*) from t_user where name=#{userName}")
    int getUserCountByName(@Param("userName") String userName);

    @Select("select * from t_user where name=#{userName} and password=#{password}")
    List<UserEntity> getAllUsersByNamePassword(@Param("userName") String userName,
                                               @Param("password") String password);

}
