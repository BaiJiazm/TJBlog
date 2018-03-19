package com.baijiazm.tjblog.mapper;

import com.baijiazm.tjblog.model.entity.OptionEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface OptionMapper {

    String selectOptionByName = "select * from t_option where name=#{name}";

    @Select(selectOptionByName)
    OptionEntity selectOptionByName(@Param("name") String name);
}
