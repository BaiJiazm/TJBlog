package com.baijiazm.tjblog.service;

import com.baijiazm.tjblog.entity.UserEntity;

public interface IUserService {

    public UserEntity login(String userName, String password);

}
