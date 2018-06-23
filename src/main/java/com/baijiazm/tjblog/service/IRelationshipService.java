package com.baijiazm.tjblog.service;

import com.baijiazm.tjblog.model.entity.RelationshipEntity;

public interface IRelationshipService {

    /**
     * 保存对象
     *
     * @param relationshipEntity
     */
    void insertRelationship(RelationshipEntity relationshipEntity);

    /**
     * 根据id搜索
     *
     * @param cid
     * @param mid
     * @return
     */
    Long selectCountById(Integer cid, Integer mid);

    /**
     * 按住键删除
     *
     * @param cid
     * @param mid
     */
    void deleteById(Integer cid, Integer mid);
}
