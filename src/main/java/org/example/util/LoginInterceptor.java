package org.example.util;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
//    private StringRedisTemplate stringRedisTemplate;
//    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
//        this.stringRedisTemplate=stringRedisTemplate;
//    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.判断是否需要拦截（ThreadLocal中是否有用户）
        if (UserHolder.getUser() == null) {
            // 没有，需要拦截，设置状态码
            response.setStatus(401);
            // 拦截
            return false;
        }
        // 有用户，则放行
        return true;
////        //1.获取session
////        HttpSession session = request.getSession();
////        //2.获取session用户信息
////        Object user = session.getAttribute("user");
////        //3.判断用户
////        if(user ==null){
////            //4.不存在拦截
////            response.setStatus(401);
////            return false;
////        }
////        // 5. 存在，保存用户信息到ThreadLocal
////        UserHolder.saveUser((UserDTO) user);
////        return true;
//        //1.获取请求体中的token
//        String token = request.getHeader("authorizaton");
//        if (StrUtil.isBlank(token)) {
//            //为空
//            response.setStatus(401);
//            return false;
//        }
//        // 2.基于token获取redis中的用户
//        String key = RedisConstants.LOGIN_USER_KEY + token;
//        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
//        // 3.判断用户是否为空
//        if (userMap.isEmpty()) {
//            //为空
//            response.setStatus(401);
//            return false;
//        }
//        // 4.将查询到的Hash数据转换成UserDTO对象
//        UserDTO userDTO= BeanUtil.fillBeanWithMap(userMap,new UserDTO(), false);
//        // 5.存在保存用户信息到ThreadLocal
//        UserHolder.saveUser(userDTO);
//        // 6.刷新token有效期
//        stringRedisTemplate.expire(key,RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
//        return true;

    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
