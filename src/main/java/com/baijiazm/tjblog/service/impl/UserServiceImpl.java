package com.baijiazm.tjblog.service.impl;

import com.baijiazm.tjblog.model.entity.UserEntity;
import com.baijiazm.tjblog.exception.TipException;
import com.baijiazm.tjblog.mapper.UserMapper;
import com.baijiazm.tjblog.service.IUserService;
import com.baijiazm.tjblog.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import sun.rmi.server.InactiveGroupException;

import java.util.List;

/**
 * 管理员相关业务逻辑
 */
@Service("userService")
public class UserServiceImpl implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserEntity login(String userName, String password) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            throw new TipException("用户名或密码不能为空");
        }
        int userCount = userMapper.getUserCountByName(userName);
        if (userCount <= 0) {
            throw new TipException("不存在的用户");
        }
        String enPassword = MyUtils.MD5encode(userName + password);
        List<UserEntity> userList = userMapper.getAllUsersByNamePassword(userName, enPassword);
        if (userList.isEmpty()) {
            throw new TipException("用户名或密码错误");
        }
        return userList.get(0);
    }

    @Override
    public void updateScreenNameEmailByUid(String screenName, String email, Integer id) {
        userMapper.updateScreenNameEmailByUid(screenName, email, id);
    }

    @Override
    public void updatePassWordByUid(UserEntity user){
        userMapper.updatePassWordByUid(user);
    }
}
