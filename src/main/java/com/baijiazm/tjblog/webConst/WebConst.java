package com.baijiazm.tjblog.webConst;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebConst {

    public static Map<String, String> initConfig = new HashMap<>();

    public static final String LOGIN_SESSION_KEY = "login_user";

    /**
     * aes加密
     */
    public static String AES_SALT = "0123456789abcdef";

    public static final String USER_IN_COOKIE = "S_L_ID";

    /**
     * 最大获取文章条数
     */
    public static final int MAX_POSTS = 9999;
}
