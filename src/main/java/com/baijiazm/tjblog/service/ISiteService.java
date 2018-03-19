package com.baijiazm.tjblog.service;

import com.baijiazm.tjblog.model.StatisticsVO;
import com.baijiazm.tjblog.model.entity.CommentEntity;
import com.baijiazm.tjblog.model.entity.ContentEntity;
import com.baijiazm.tjblog.model.entity.LogEntity;

import java.util.List;

public interface ISiteService {

    List<CommentEntity> getRecentComments(int limit);

    List<ContentEntity> getRecentContents(int limit);

    StatisticsVO getStatistics();

    List<LogEntity> getLogs(int page, int limit);
}
