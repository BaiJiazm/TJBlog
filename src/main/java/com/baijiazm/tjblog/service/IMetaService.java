package com.baijiazm.tjblog.service;

import com.baijiazm.tjblog.model.entity.MetaEntity;

import java.util.List;

public interface IMetaService {

    List<MetaEntity> getMetasByType(String type);

    void saveMeta(Integer cid, String names, String types);

    /**
     * 根据类型查询项目列表
     *
     * @param types
     * @return
     */
    List<MetaEntity> getMetas(String types);


    /**
     * 保存多个项目
     *
     * @param cid
     * @param names
     * @param type
     */
    void saveMetas(Integer cid, String names, String type);

    /**
     * 保存项目
     *
     * @param type
     * @param name
     * @param mid
     */
    void saveMeta(String type, String name, Integer mid);


    /**
     * 根据类型查询项目列表，带项目下面的文章数
     *
     * @return
     */
    List<MetaEntity> getMetaList(String type, String orderby, int limit);


    /**
     * 删除项目
     *
     * @param mid
     */
    void delete(int mid);

}
