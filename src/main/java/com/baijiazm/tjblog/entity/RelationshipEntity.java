package com.baijiazm.tjblog.entity;

import java.io.Serializable;

public class RelationshipEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 内容主键
     */
    private Integer contentId;

    /**
     * 元信息主键
     */
    private Integer metaId;


    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public Integer getMetaId() {
        return metaId;
    }

    public void setMetaId(Integer metaId) {
        this.metaId = metaId;
    }
}