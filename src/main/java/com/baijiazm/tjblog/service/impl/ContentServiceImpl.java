package com.baijiazm.tjblog.service.impl;

import com.baijiazm.tjblog.dto.Types;
import com.baijiazm.tjblog.exception.TipException;
import com.baijiazm.tjblog.mapper.ContentMapper;
import com.baijiazm.tjblog.model.entity.ContentEntity;
import com.baijiazm.tjblog.service.IContentService;
import com.baijiazm.tjblog.service.IMetaService;
import com.baijiazm.tjblog.service.IRelationshipService;
import com.baijiazm.tjblog.utils.DateKit;
import com.baijiazm.tjblog.utils.MyUtils;
import com.baijiazm.tjblog.utils.Tools;
import com.baijiazm.tjblog.webConst.WebConst;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("contentService")
public class ContentServiceImpl implements IContentService {

    @Autowired
    ContentMapper contentMapper;

    @Autowired
    IMetaService metaService;

    @Autowired
    IRelationshipService relationshipService;

    @Override
    @Transactional
    public String publish(ContentEntity content) {
        if (content == null) {
            return "文章对象为空";
        }
        if (StringUtils.isBlank(content.getTitle())) {
            return "文章标题不能为空";
        }
        if (StringUtils.isBlank(content.getContent())) {
            return "文章内容不能为空";
        }
        int titleLength = content.getTitle().length();
        if (titleLength > WebConst.MAX_TITLE_COUNT) {
            return "文章标题过长";
        }
        int contentLength = content.getContent().length();
        if (contentLength > WebConst.MAX_TEXT_COUNT) {
            return "文章内容过长";
        }
        if (null == content.getAuthorId()) {
            return "请登录后发布文章";
        }
        if (StringUtils.isNotBlank(content.getSlug())) {
            if (content.getSlug().length() < 5) {
                return "路径太短了";
            }
            if (!MyUtils.isPath(content.getSlug())) {
                return "您输入的路径不合法 须匹配^[A-Za-z0-9_-]{5,100}$";
            }
            int count = contentMapper.selectCountBySlug(content.getSlug());
            if (count > 0) {
                return "该路径已经存在，请重新输入";
            }
        } else {
            content.setSlug(null);
        }

        content.setContent(EmojiParser.parseToAliases(content.getContent()));

        int time = DateKit.getCurrentUnixTime();
        content.setCreated(time);
        content.setModified(time);
        content.setHits(0);
        content.setCommentsNumber(0);

        contentMapper.insertOneContent(content);

        String tags = content.getTags();
        String categories = content.getCategories();
        Integer cid = content.getId();
        metaService.saveMeta(cid, tags, Types.TAG.getType());
        metaService.saveMeta(cid, categories, Types.CATEGORY.getType());
        return WebConst.SUCCESS_RESULT;
    }

    @Override
    public PageInfo<ContentEntity> getArticleWithPage(String order, int page, int limit) {
//        PageHelper.startPage(page, limit);
        int offset = (page - 1) * limit;
        List<ContentEntity> ContentEntitys = contentMapper.selectContentsByTypeOrderLimitBegin1(Types.ARTICLE.getType(), order, offset, limit);
//        List<ContentEntity> ContentEntitys = contentMapper.selectContentsByTypeOrderLimitBegin(Types.ARTICLE.getType(), order, offset);
        return new PageInfo<>(ContentEntitys);
    }

    @Override
    public PageInfo<ContentEntity> getContents(Integer p, Integer limit) {
        PageHelper.startPage(p, limit);
        List<ContentEntity> data = contentMapper.selectByTypeStatus(Types.ARTICLE.getType(), Types.PUBLISH.getType());
        PageInfo<ContentEntity> pageInfo = new PageInfo<>(data);
        return pageInfo;
    }

    @Override
    public ContentEntity getContents(String id) {
        if (StringUtils.isNotBlank(id)) {
            if (Tools.isNumber(id)) {
                ContentEntity ContentEntity = contentMapper.selectByPrimaryKey(Integer.valueOf(id));
                if (ContentEntity != null) {
                    ContentEntity.setHits(ContentEntity.getHits() + 1);
                    contentMapper.updateByPrimaryKey(ContentEntity);
                }
                return ContentEntity;
            } else {
                ContentEntity contentEntity = new ContentEntity();
                contentEntity.setSlug(id);
                List<ContentEntity> ContentEntitys = contentMapper.selectBySlug(id);
                if (ContentEntitys.size() != 1) {
                    throw new TipException("query content by id and return is not one");
                }
                return ContentEntitys.get(0);
            }
        }
        return null;
    }

    @Override
    public void updateCommentsNumberById(ContentEntity contentEntity) {
        if (null != contentEntity && null != contentEntity.getId()) {
            contentMapper.updateCommentsNumberById(contentEntity);
        }
    }

    @Override
    public void updateHitsByCid(ContentEntity contentEntity) {
        if (null != contentEntity && null != contentEntity.getId()) {
            contentMapper.updateHitsById(contentEntity);
        }
    }

    //    @Override
//    public PageInfo<ContentEntity> getArticles(Integer mid, int page, int limit) {
//        int total = metaService.countWithSql(mid);
//        PageHelper.startPage(page, limit);
//        List<ContentEntity> list = contentMapper.findByCatalog(mid);
//        PageInfo<ContentEntity> paginator = new PageInfo<>(list);
//        paginator.setTotal(total);
//        return paginator;
//    }
//
//    @Override
//    public PageInfo<ContentEntity> getArticles(String keyword, Integer page, Integer limit) {
//        PageHelper.startPage(page, limit);
//        ContentEntity ContentEntity = new ContentEntity();
//        ContentEntity.Criteria criteria = ContentEntity.createCriteria();
//        criteria.andTypeEqualTo(Types.ARTICLE.getType());
//        criteria.andStatusEqualTo(Types.PUBLISH.getType());
//        criteria.andTitleLike("%" + keyword + "%");
//        ContentEntity.setOrderByClause("created desc");
//        List<ContentEntity> ContentEntitys = contentMapper.selectByExampleWithBLOBs(ContentEntity);
//        return new PageInfo<>(ContentEntitys);
//    }
//
//    @Override
//    public PageInfo<ContentEntity> getArticlesWithpage(ContentEntity commentVoExample, Integer page, Integer limit) {
//        PageHelper.startPage(page, limit);
//        List<ContentEntity> ContentEntitys = contentMapper.selectByExampleWithBLOBs(commentVoExample);
//        return new PageInfo<>(ContentEntitys);
//    }
//
    @Override
    @Transactional
    public String deleteByCid(Integer cid) {
        ContentEntity contents = this.getContents(cid + "");
        if (null != contents) {
            contentMapper.deleteByPrimaryKey(cid);
            relationshipService.deleteById(cid, null);
            return WebConst.SUCCESS_RESULT;
        }
        return "数据为空";
    }
//
//    @Override
//    public void updateCategory(String ordinal, String newCatefory) {
//        ContentEntity ContentEntity = new ContentEntity();
//        ContentEntity.setCategories(newCatefory);
//        ContentEntity example = new ContentEntity();
//        example.createCriteria().andCategoriesEqualTo(ordinal);
//        contentMapper.updateByExampleSelective(ContentEntity, example);
//    }
//
//    @Override
//    @Transactional
//    public String updateArticle(ContentEntity contents) {
//        if (null == contents) {
//            return "文章对象为空";
//        }
//        if (StringUtils.isBlank(contents.getTitle())) {
//            return "文章标题不能为空";
//        }
//        if (StringUtils.isBlank(contents.getContent())) {
//            return "文章内容不能为空";
//        }
//        int titleLength = contents.getTitle().length();
//        if (titleLength > WebConst.MAX_TITLE_COUNT) {
//            return "文章标题过长";
//        }
//        int contentLength = contents.getContent().length();
//        if (contentLength > WebConst.MAX_TEXT_COUNT) {
//            return "文章内容过长";
//        }
//        if (null == contents.getAuthorId()) {
//            return "请登录后发布文章";
//        }
//        if (StringUtils.isBlank(contents.getSlug())) {
//            contents.setSlug(null);
//        }
//        int time = DateKit.getCurrentUnixTime();
//        contents.setModified(time);
//        Integer cid = contents.getId();
//        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));
//
//        contentMapper.updateByPrimaryKeySelective(contents);
//        relationshipService.deleteById(cid, null);
//        metasService.saveMetas(cid, contents.getTags(), Types.TAG.getType());
//        metasService.saveMetas(cid, contents.getCategories(), Types.CATEGORY.getType());
//        return WebConst.SUCCESS_RESULT;
//    }
}
