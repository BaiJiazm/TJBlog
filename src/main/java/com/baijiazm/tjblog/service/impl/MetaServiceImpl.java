package com.baijiazm.tjblog.service.impl;

import com.baijiazm.tjblog.dto.Types;
import com.baijiazm.tjblog.exception.TipException;
import com.baijiazm.tjblog.mapper.MetaMapper;
import com.baijiazm.tjblog.model.entity.ContentEntity;
import com.baijiazm.tjblog.model.entity.MetaEntity;
import com.baijiazm.tjblog.model.entity.RelationshipEntity;
import com.baijiazm.tjblog.service.IContentService;
import com.baijiazm.tjblog.service.IMetaService;
import com.baijiazm.tjblog.service.IRelationshipService;
import com.baijiazm.tjblog.webConst.WebConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MetaServiceImpl implements IMetaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetaServiceImpl.class);

    @Autowired
    MetaMapper metaMapper;

    @Autowired
    IRelationshipService relationshipService;

    @Autowired
    IContentService contentService;

    @Override
    public List<MetaEntity> getMetasByType(String type) {
        if (StringUtils.isNotBlank(type)) {
            return metaMapper.getMetasByType(type);
        }
        return null;
    }

    @Override
    @Transactional
    public void saveMeta(Integer cid, String names, String type) {
        if (null == cid) {
            throw new TipException("Meta 关联id不能为空");
        }
        if (StringUtils.isNotBlank(names) && StringUtils.isNotBlank(type)) {
            String[] nameArray = StringUtils.split(names, ",");
            for (String name : nameArray) {
                LOGGER.info(name);
                this.saveOrUpdate(cid, name, type);
            }
        }
    }

    private void saveOrUpdate(Integer cid, String name, String type) {
        List<MetaEntity> metas = metaMapper.selectMetasByNameType(name, type);
        MetaEntity meta;
        if (metas.size() > 1) {
            throw new TipException("查询到多条数据");
        } else if (metas.size() == 0) {
            meta = new MetaEntity();
            meta.setSlug(name);
            meta.setName(name);
            meta.setType(type);
            metaMapper.insertOneMeta(meta);
//            metas = metaMapper.selectMetasByNameType(name, type);
            LOGGER.info(meta.getName());
        } else {
            meta = metas.get(0);
            LOGGER.info(meta.getName());
        }
        int mid = meta.getId();
        if (mid != 0) {
            Long count = relationshipService.selectCountById(cid, mid);
            if (count == 0) {
                RelationshipEntity relationshipEntity = new RelationshipEntity();
                relationshipEntity.setContentId(cid);
                relationshipEntity.setMetaId(mid);
                relationshipService.insertRelationship(relationshipEntity);
            }
        }
    }

    @Override
    public List<MetaEntity> getMetas(String types) {
        if (StringUtils.isNotBlank(types)) {
            return metaMapper.getMetasByTypeOrder(types, "sort desc, id desc");
        }
        return null;
    }

    @Override
    public List<MetaEntity> getMetaList(String type, String orderby, int limit) {
        if (StringUtils.isNotBlank(type)) {
            if (StringUtils.isBlank(orderby)) {
                orderby = "count desc, id desc";
            }
            if (limit < 1 || limit > WebConst.MAX_POSTS) {
                limit = 10;
            }
            return metaMapper.selectByTypeOrderLimit(type, orderby, limit);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(int mid) {
        MetaEntity metas = metaMapper.selectByPrimaryKey(mid);
        if (null != metas) {
            String type = metas.getType();
            String name = metas.getName();

            metaMapper.deleteById(mid);

            List<RelationshipEntity> rlist = relationshipService.getRelationshipById(null, mid);
            if (null != rlist) {
                for (RelationshipEntity r : rlist) {
                    ContentEntity contents = contentService.getContents(String.valueOf(r.getContentId()));
                    if (null != contents) {
                        ContentEntity temp = new ContentEntity();
                        temp.setId(r.getContentId());
                        if (type.equals(Types.CATEGORY.getType())) {
                            temp.setCategories(reMeta(name, contents.getCategories()));
                            contentService.updateCategoriesByCid(temp);
                        }
                    }
                }
            }
            relationshipService.deleteById(null, mid);
        }
    }

    @Override
    @Transactional
    public void saveMeta(String type, String name, Integer mid) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(name)) {
            List<MetaEntity> MetaEntitys = metaMapper.selectMetasByNameType(name, type);
            if (MetaEntitys.size() != 0) {
                throw new TipException("已经存在该项");
            } else {
                MetaEntity metas;
                metas = new MetaEntity();
                metas.setName(name);
                metas.setType(type);
                metaMapper.insertOneMeta(metas);
            }
        }
    }


    @Override
    @Transactional
    public void saveMetas(Integer cid, String names, String type) {
        if (null == cid) {
            throw new TipException("项目关联id不能为空");
        }
        if (StringUtils.isNotBlank(names) && StringUtils.isNotBlank(type)) {
            String[] nameArr = StringUtils.split(names, ",");
            for (String name : nameArr) {
                this.saveOrUpdate(cid, name, type);
            }
        }
    }

    private String reMeta(String name, String metas) {
        String[] ms = StringUtils.split(metas, ",");
        StringBuilder sbuf = new StringBuilder();
        for (String m : ms) {
            if (!name.equals(m)) {
                sbuf.append(",").append(m);
            }
        }
        if (sbuf.length() > 0) {
            return sbuf.substring(1);
        }
        return "";
    }

}
