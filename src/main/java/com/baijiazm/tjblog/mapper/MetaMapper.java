package com.baijiazm.tjblog.mapper;

import com.baijiazm.tjblog.model.entity.MetaEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

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

    String selectCountByType = "select count(*) from t_meta where type=#{type}";

    @Select(selectCountByType)
    Long selectCountByType(@Param("type") String type);

    String getMetasByType = "select * from t_meta where type=#{type}";

    @Select(getMetasByType)
    List<MetaEntity> getMetasByType(@Param("type") String type);


    String getMetasByTypeOrder = "select * from t_meta where type=#{type} order by #{order}";

    @Select(getMetasByTypeOrder)
    List<MetaEntity> getMetasByTypeOrder(@Param("type") String type,
                                         @Param("order") String order);


    String selectMetasByNameType = "select * from t_meta where name=#{name} and type=#{type}";

    @Select(selectMetasByNameType)
    List<MetaEntity> selectMetasByNameType(@Param("name") String name,
                                           @Param("type") String type);


    String selectByTypeOrderLimit = "select * from t_meta where type=#{type} order by #{order}, limit #{limit}";

    @Select(selectByTypeOrderLimit)
    List<MetaEntity> selectByTypeOrderLimit(@Param("type") String type,
                                            @Param("order") String order,
                                            @Param("limit") int limit);


    String selectByPrimaryKey = "select * from t_meta where id=#{id}";

    @Select(selectByPrimaryKey)
    MetaEntity selectByPrimaryKey(@Param("id") Integer id);


    String insertOneMeta = "insert into t_meta ( " +
            " `id`, `name`, `slug`, `type`, `description`, `sort`, `parent`) " +
            " values (" +
            " #{meta.id}, #{meta.name}, #{meta.slug}, #{meta.type}, #{meta.description}, #{meta.sort}, #{meta.parent} " +
            " ) ";

    @Insert(insertOneMeta)
    @Options(useGeneratedKeys = true, keyProperty = "meta.id")
    void insertOneMeta(@Param("meta") MetaEntity meta);


    String deleteById = "delete from t_meta where id=#{id}";

    @Delete(deleteById)
    void deleteById(@Param("id") Integer id);
}
