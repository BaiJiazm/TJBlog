package com.baijiazm.tjblog.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

//-- 附件表
//        DROP TABLE IF EXISTS `t_attach`;
//
//        CREATE TABLE `t_attach` (
//        `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
//        `file_name` varchar(100) NOT NULL DEFAULT '',
//        `file_type` varchar(50) DEFAULT '',
//        `file_key` varchar(100) NOT NULL DEFAULT '',
//        `author_id` int(10) DEFAULT NULL,
//        `created` int(10) NOT NULL,
//        PRIMARY KEY (`id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

@Component
public interface AttachMapper {

    String getCountByAuthor="select count(*) from t_attach where #{authorId}=author_id ";
    @Select(getCountByAuthor)
    long getCountByAuthor(int authorId);
}
