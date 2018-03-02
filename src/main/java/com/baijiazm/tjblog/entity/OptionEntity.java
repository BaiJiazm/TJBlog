package com.baijiazm.tjblog.entity;

import java.io.Serializable;

public class OptionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 选项名称
     */
    private String name;

    /**
     * 选项值
     */
    private String value;

    /**
     * 选项描述
     */
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}