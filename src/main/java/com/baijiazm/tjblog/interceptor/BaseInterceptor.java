package com.baijiazm.tjblog.interceptor;

import com.baijiazm.tjblog.mapper.OptionMapper;
import com.baijiazm.tjblog.mapper.UserMapper;
import com.baijiazm.tjblog.model.entity.OptionEntity;
import com.baijiazm.tjblog.model.entity.UserEntity;
import com.baijiazm.tjblog.utils.Commons;
import com.baijiazm.tjblog.utils.MyUtils;
import com.baijiazm.tjblog.utils.UUID;
import com.baijiazm.tjblog.webConst.WebConst;
import com.baijiazm.tjblog.utils.MapCache;
import com.baijiazm.tjblog.dto.Types;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {

    private MapCache cache = MapCache.single();

    @Resource
    private UserMapper userMapper;

    @Resource
    private OptionMapper optionMapper;

    @Resource
    private Commons commons;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String uri = request.getRequestURI();

//        LOGGE.info("UserAgent: {}", request.getHeader(USER_AGENT));
//        LOGGE.info("用户访问地址: {}, 来路地址: {}", uri, IPKit.getIpAddrByRequest(request));

        //请求拦截处理
        UserEntity user = MyUtils.getLoginUser(request);
        if (null == user) {
            Integer uid = MyUtils.getCookieUid(request);
            if (null != uid) {
                //这里还是有安全隐患,cookie是可以伪造的
                user = userMapper.selectUserById(uid);
                request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
            }
        }

        if (uri.startsWith("/admin") && !uri.startsWith("/admin/login") && null == user) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }

        //设置get请求的token
        if (request.getMethod().equals("GET")) {
            String csrf_token = UUID.UU64();
            // 默认存储30分钟
            cache.hset(Types.CSRF_TOKEN.getType(), csrf_token, uri, 30 * 60);
            request.setAttribute("_csrf_token", csrf_token);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        OptionEntity optionEntity = optionMapper.selectOptionByName("site_record");
        httpServletRequest.setAttribute("commons", commons);//一些工具类和公共方法
        httpServletRequest.setAttribute("option", optionEntity);
//        httpServletRequest.setAttribute("adminCommons", adminCommons);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
