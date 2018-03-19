package com.baijiazm.tjblog.service.impl;

import com.baijiazm.tjblog.model.entity.LogEntity;
import com.baijiazm.tjblog.mapper.LogMapper;
import com.baijiazm.tjblog.service.ILogService;
import com.baijiazm.tjblog.utils.DateKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("logService")
public class LogServiceImpl implements ILogService {

    private LogEntity logEntity;

    private LogMapper logMapper;

    @Autowired
    LogServiceImpl(LogMapper logMapper) {
        this.logEntity = new LogEntity();
        this.logMapper = logMapper;
    }

    @Override
    public void insertLog(String action, String data, String ip, Integer authorId) {
        logEntity = new LogEntity();
        logEntity.setAction(action);
        logEntity.setData(data);
        logEntity.setIp(ip);
        logEntity.setAuthorId(authorId);
        logEntity.setCreated(DateKit.getCurrentUnixTime());
        logMapper.insertLog(logEntity);
    }

}
