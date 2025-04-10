package org.example.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.*;
import org.example.entity.FileContent;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.service.IFileContentService;
import org.example.service.IUserService;
import org.example.util.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.example.util.RedisConstants.LOGIN_USER_KEY;
import static org.example.util.RedisConstants.LOGIN_USER_TTL;

/**
 * @Author：wzm
 * @Package：org.example.service.impl
 * @Project：tuijianserver
 * @name：IuserServiceImpl
 * @Date：2024/1/31 18:25
 * @Filename：IuserServiceImpl
 */
@Service
@Slf4j
public class IuserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    IFileContentService fileContentService;
    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();
        System.out.println(password);
        User user = query().eq("username", username).one();
        if(user ==null){
            return Result.fail("用户不存在");
        }
        if(!user.getPassword().trim().equals(password)){
            return  Result.fail("密码错误");
        }
        System.out.println("成功");
        String token = UUID.randomUUID().toString(true);
        UserDTO userDTO =BeanUtil.copyProperties(user,UserDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        String tokenKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("token",token);
        resMap.put("userInfo",user);
        return Result.ok(resMap);
    }
    /**
     * @description:代码信息上传页面
     * @return: org.example.dto.Result
     * @author: wzm
     * @time: 2024/3/4 8:40
     */
    @Override
    public Result info(FileContentDTO fileContentDTO) {
        // 1.获取登录用户
        UserDTO user = UserHolder.getUser();
        FileContent fileContent = new FileContent();
        fileContent.setUser_id(user.getId());
        fileContent.setFile_image(fileContentDTO.getFile_image());
        fileContent.setFile_name(fileContentDTO.getFile_name());
        fileContent.setFile_path(fileContentDTO.getFile_path());
        fileContent.setPrice(fileContent.getPrice());
        fileContent.setUser_name(user.getUsername());
//        System.out.println("fileContentDTO.getFile_path:"+fileContentDTO.getFile_path());
//        System.out.println("fileContentDTO.getFile_image:"+fileContentDTO.getFile_image());
//        System.out.println("fileContent.getUpload():"+fileContentDTO.getUpload());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(fileContentDTO.getUpload());
            fileContent.setUpload(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileContent.setType(fileContentDTO.getType());
        fileContent.setTitle(fileContentDTO.getTitle());
        // 2.保存笔记
        boolean isSuccess = fileContentService.save(fileContent);
        if(!isSuccess){
            return Result.fail("新增笔记失败!");
        }
        return Result.ok();
    }
    @Override
    public Result logout(HttpServletRequest request) {
        if(UserHolder.getUser() == null){
            return Result.fail("你不该看到这个页面");
        }
        String token = request.getHeader("authorization");
        //在Redis把token也删了
        String key  = LOGIN_USER_KEY + token;
        if (stringRedisTemplate.hasKey(key)) {
            // key 存在，执行删除操作
            stringRedisTemplate.delete(key);
        } else {
            return Result.fail("你不该看到这个页面");
        }
        UserHolder.removeUser();
        log.debug("成功退出");
        return Result.ok();
    }

    @Override
    public Result register(RegisterDTO registerDTO) {
        User user = getOne(new QueryWrapper<User>().eq("username", registerDTO.getUsername()));
        User userLKnm = getOne(new QueryWrapper<User>().eq("userLKnm", registerDTO.getUserLKnm()));
        if(userLKnm != null){
            return Result.fail("组名已存在请更换");
        }
        if(user == null){
            if(registerDTO.getPassword().equals(registerDTO.getRes_password())) {
                User user1 = new User();
                user1.setUsername(registerDTO.getUsername());
                user1.setPassword(registerDTO.getPassword());
                user1.setUserLKnm(registerDTO.getUserLKnm());
                save(user1);
                return Result.ok(user1);
            }else {
                return Result.fail("密码不一致");
            }
        }else {
            return Result.fail("用户名已经存在请更换");
        }
    }

}
