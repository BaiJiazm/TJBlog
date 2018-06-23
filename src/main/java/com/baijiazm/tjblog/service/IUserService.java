package com.baijiazm.tjblog.service;

import com.baijiazm.tjblog.model.entity.UserEntity;

public interface IUserService {

    public UserEntity login(String userName, String password);

    public void updateScreenNameEmailByUid(String screenName, String email, Integer id);

    public void updatePassWordByUid(UserEntity user);
}
