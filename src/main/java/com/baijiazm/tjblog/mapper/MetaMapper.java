package com.baijiazm.tjblog.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

//-- 元信息表
//        DROP TABLE IF EXISTS `t_meta`;
//
//        CREATE TABLE `t_meta` (
//        `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
//        `name` varchar(200) DEFAULT NULL,
//        `slug` varchar(200) DEFAULT NULL,
//        `type` varchar(32) NOT NULL DEFAULT '' ,
//        `description` varchar(200) DEFAULT NULL ,
//        `sort` int(10) unsigned DEFAULT '0',
//        `parent` int(10) unsigned DEFAULT '0',
//        PRIMARY KEY (`id`),
//        KEY `slug` (`slug`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

@Component
public interface MetaMapper {
    String countByType = "select count(*) from t_meta where type=#{type}";

    @Select(countByType)
    Long countByType(@Param("type") String type);
}
