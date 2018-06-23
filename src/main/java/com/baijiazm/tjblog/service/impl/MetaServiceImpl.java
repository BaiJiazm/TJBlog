package com.baijiazm.tjblog.service.impl;

import com.baijiazm.tjblog.dto.Types;
import com.baijiazm.tjblog.exception.TipException;
import com.baijiazm.tjblog.mapper.MetaMapper;
import com.baijiazm.tjblog.model.entity.ContentEntity;
import com.baijiazm.tjblog.model.entity.MetaEntity;
import com.baijiazm.tjblog.model.entity.RelationshipEntity;
import com.baijiazm.tjblog.service.IMetaService;
import com.baijiazm.tjblog.service.IRelationshipService;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MetaServiceImpl implements IMetaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetaServiceImpl.class);

    @Autowired
    MetaMapper metaMapper;

    @Autowired
    IRelationshipService relationshipService;

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

//    @Override
//    public List<MetaDto> getMetaList(String type, String orderby, int limit) {
//        if (StringUtils.isNotBlank(type)) {
//            if (StringUtils.isBlank(orderby)) {
//                orderby = "count desc, a.mid desc";
//            }
//            if (limit < 1 || limit > WebConst.MAX_POSTS) {
//                limit = 10;
//            }
//            Map<String, Object> paraMap = new HashMap<>();
//            paraMap.put("type", type);
//            paraMap.put("order", orderby);
//            paraMap.put("limit", limit);
//            return metaMapper.selectFromSql(paraMap);
//        }
//        return null;
//    }
//
//    @Override
//    @Transactional
//    public void delete(int mid) {
//        MetaEntity metas = metaMapper.selectByPrimaryKey(mid);
//        if (null != metas) {
//            String type = metas.getType();
//            String name = metas.getName();
//
//            metaMapper.deleteByPrimaryKey(mid);
//
//            List<RelationshipVoKey> rlist = relationshipService.getRelationshipById(null, mid);
//            if (null != rlist) {
//                for (RelationshipVoKey r : rlist) {
//                    ContentEntity contents = contentService.getContents(String.valueOf(r.getCid()));
//                    if (null != contents) {
//                        ContentEntity temp = new ContentEntity();
//                        temp.setCid(r.getCid());
//                        if (type.equals(Types.CATEGORY.getType())) {
//                            temp.setCategories(reMeta(name, contents.getCategories()));
//                        }
//                        if (type.equals(Types.TAG.getType())) {
//                            temp.setTags(reMeta(name, contents.getTags()));
//                        }
//                        contentService.updateContentByCid(temp);
//                    }
//                }
//            }
//            relationshipService.deleteById(null, mid);
//        }
//    }
//
//    @Override
//    @Transactional
//    public void saveMeta(String type, String name, Integer mid) {
//        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(name)) {
//            MetaEntity MetaEntity = new MetaEntity();
//            MetaEntity.createCriteria().andTypeEqualTo(type).andNameEqualTo(name);
//            List<MetaEntity> MetaEntitys = metaMapper.selectByExample(MetaEntity);
//            MetaEntity metas;
//            if (MetaEntitys.size() != 0) {
//                throw new TipException("已经存在该项");
//            } else {
//                metas = new MetaEntity();
//                metas.setName(name);
//                if (null != mid) {
//                    MetaEntity original = metaMapper.selectByPrimaryKey(mid);
//                    metas.setMid(mid);
//                    metaMapper.updateByPrimaryKeySelective(metas);
////                    更新原有文章的categories
//                    contentService.updateCategory(original.getName(), name);
//                } else {
//                    metas.setType(type);
//                    metaMapper.insertSelective(metas);
//                }
//            }
//        }
//    }
//
//    @Override
//    @Transactional
//    public void saveMetas(Integer cid, String names, String type) {
//        if (null == cid) {
//            throw new TipException("项目关联id不能为空");
//        }
//        if (StringUtils.isNotBlank(names) && StringUtils.isNotBlank(type)) {
//            String[] nameArr = StringUtils.split(names, ",");
//            for (String name : nameArr) {
//                this.saveOrUpdate(cid, name, type);
//            }
//        }
//    }
//
//    private String reMeta(String name, String metas) {
//        String[] ms = StringUtils.split(metas, ",");
//        StringBuilder sbuf = new StringBuilder();
//        for (String m : ms) {
//            if (!name.equals(m)) {
//                sbuf.append(",").append(m);
//            }
//        }
//        if (sbuf.length() > 0) {
//            return sbuf.substring(1);
//        }
//        return "";
//    }
//
//    @Override
//    @Transactional
//    public void saveMeta(MetaEntity metas) {
//        if (null != metas) {
//            metaMapper.insertSelective(metas);
//        }
//    }
//
//    @Override
//    @Transactional
//    public void update(MetaEntity metas) {
//        if (null != metas && null != metas.getMid()) {
//            metaMapper.updateByPrimaryKeySelective(metas);
//        }
//    }
}
