package com.baijiazm.tjblog.service;

public interface ILogService {

    public void insertLog(String action, String data, String ip, Integer authorId);
}
