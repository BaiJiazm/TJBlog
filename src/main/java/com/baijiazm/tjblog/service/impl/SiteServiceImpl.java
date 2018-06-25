package com.baijiazm.tjblog.service.impl;

import com.baijiazm.tjblog.dto.Types;
import com.baijiazm.tjblog.mapper.*;
import com.baijiazm.tjblog.model.StatisticsVO;
import com.baijiazm.tjblog.model.entity.CommentEntity;
import com.baijiazm.tjblog.model.entity.ContentEntity;
import com.baijiazm.tjblog.model.entity.LogEntity;
import com.baijiazm.tjblog.model.entity.MetaEntity;
import com.baijiazm.tjblog.service.ISiteService;
import com.baijiazm.tjblog.webConst.WebConst;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("siteService")
public class SiteServiceImpl implements ISiteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

    private CommentMapper commentMapper;

    private ContentMapper contentMapper;

    private AttachMapper attachMapper;

    private MetaMapper metaMapper;

    private LogMapper logMapper;

    @Autowired
    SiteServiceImpl(CommentMapper commentMapper, ContentMapper contentMapper,
                    AttachMapper attachMapper, MetaMapper metaMapper,
                    LogMapper logMapper) {
        this.commentMapper = commentMapper;
        this.contentMapper = contentMapper;
        this.attachMapper = attachMapper;
        this.metaMapper = metaMapper;
        this.logMapper = logMapper;
    }

    @Override
    public List<CommentEntity> getRecentComments(int limit) {
        LOGGER.debug("Enter getRecentContents method");
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        List<CommentEntity> commentEntityList = commentMapper.selectCommentsOrderByCreatedDesc(0, limit);
        LOGGER.debug("Exit getRecentComments method");
        return commentEntityList;
    }

    @Override
    public List<ContentEntity> getRecentContents(int limit) {
        LOGGER.debug("Enter getRecentContents method");
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        List<ContentEntity> contentEntityList = contentMapper.selectContentsByTypeOrderLimitBegin1(
                Types.ARTICLE.getType(), "created desc", 0, limit);
        LOGGER.debug("Exit getRecentContents method");
        return contentEntityList;
    }

    @Override
    public StatisticsVO getStatistics() {
        LOGGER.debug("Enter getStatistics method");
        int authorid = 1;
        int ownerId = 1;
        StatisticsVO statisticsVO = new StatisticsVO();
        Long articles = contentMapper.selectCountByType(Types.ARTICLE.getType());
        Long comments = commentMapper.countByOwnerId(ownerId);
        Long attaches = attachMapper.getCountByAuthor(authorid);
        Long links = metaMapper.selectCountByType(Types.LINK.getType());

        statisticsVO.setArticles(articles);
        statisticsVO.setComments(comments);
        statisticsVO.setAttachs(attaches);
        statisticsVO.setLinks(links);

        return statisticsVO;
    }

    @Override
    public List<LogEntity> getLogs(int page, int limit) {
        if (page <= 0) {
            page = 1;
        }
        if (limit < 1 || limit > WebConst.MAX_POSTS) {
            limit = 10;
        }

//        PageHelper.startPage((page - 1) * limit, limit);
        List<LogEntity> logEntities = logMapper.selectLogsOffsetLimit(0, limit);
        return logEntities;
    }
}
