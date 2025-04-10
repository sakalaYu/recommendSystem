package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.*;
import org.example.entity.Collect;
import org.example.entity.FileContent;
import org.example.entity.User;
import org.example.service.ICollectService;
import org.example.service.IFileContentService;
import org.example.service.IUserService;
import org.example.util.UserHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author：wzm
 * @Package：org.example.controller
 * @Project：tuijianserver
 * @name：UserController
 * @Date：2024/1/31 18:30
 * @Filename：UserController
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;
    @Resource
    private ICollectService collectService;
    @Resource
    private IFileContentService fileContentService;
   /**
    * @description:登录
    * @return:
    * @author: wzm
    * @time: 2024/1/31 18:31
    */
   @PostMapping("/login")
   public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session){
       log.info("Attempting to log in with username: {}", loginForm.getUsername());
       Result result = userService.login(loginForm, session);
       if (result.getCode() == 401) {
           log.warn("Login failed for username: {}", loginForm.getUsername());
       }
       log.info("Login result: {}", result);
       return result;
   }
   /**
    * @description:注册
    * @return:
    * @author: wzm
    * @time: 2024/3/8 15:30
    */
   @PostMapping("/register")
   public Result register(@RequestBody RegisterDTO registerDTO){
        return userService.register(registerDTO);
   }
   /**
    * @description:退出登录
    * @return: org.example.dto.Result
    * @author: wzm
    * @time: 2024/3/7 20:49
    */
   @GetMapping("/logout")
   public Result logout(HttpServletRequest request){
        return userService.logout(request);
   }
    @GetMapping("/health")
    public Result healthCheck() {
        return Result.ok("Server is running");
    }
   @PostMapping("/collect")
    public Result collect(@RequestBody CollectDto collect){
       return collectService.collect(collect);
   };
   /**
    * @description:收藏表
    * @return: org.example.dto.Result
    * @author: wzm
    * @time: 2024/3/4 8:42
    */
   @GetMapping("/showcollect")
    public Result showcollect(){
       return collectService.showcollect();
   }
   /**
    * @description:代码信息上传
    * @return: org.example.dto.Result
    * @author: wzm
    * @time: 2024/3/4 8:40
    */
   @PostMapping("/info")
    public Result info(@RequestBody FileContentDTO fileContentDTO){
       return  userService.info(fileContentDTO);
   }
   /**
    * @description:我的上传
    * @return:
    * @author: wzm
    * @time: 2024/3/5 21:07
    */
   @GetMapping("/MyUpload")
    public Result MyUpload(){
       Integer id = UserHolder.getUser().getId();
       List<FileContent> Myfile = fileContentService.query().eq("user_id", id).list();
       return Result.ok(Myfile);
   }
   /**
    * @description:用户图表信息
    * @return:
    * @author: wzm
    * @time: 2024/3/6 20:05
    */
   @GetMapping("/user/echar")
    public Result user_echar(){
       List<Integer> userIdList = userService.list().stream().map(User::getId).collect(Collectors.toList());
       List<List<String>> resList1 = new ArrayList<List<String>>(); // 小组上传了多少file
       List<List<String>> resList2 = new ArrayList<List<String>>(); // 每个小组的评论量
       List<List<String>> resList3 = new ArrayList<List<String>>(); // 每个file的下载量
       for(Integer id : userIdList){
           Integer count = fileContentService.query().eq("user_id", id).count();
           User user = userService.query().eq("id", id).one();
           String name = user.getUserLKnm();
           Long downCount = user.getDown_count();
           Long commentUser = user.getComment_user();
           List<String> temp1 =new ArrayList<>();
           List<String> temp2 =new ArrayList<>();
           List<String> temp3 =new ArrayList<>();
           temp1.add(name);
           temp1.add(String.valueOf(count));

           temp2.add(name);
           temp2.add(String.valueOf(commentUser));
           temp3.add(name);
           temp3.add(String.valueOf(downCount));
           resList1.add(temp1);
           resList2.add(temp2);
           resList3.add(temp3);
       }
       Map<String, List<List<String>>> resMap =new HashMap<>();
       resMap.put("resList1",resList1);
       resMap.put("resList2",resList2);
       resMap.put("resList3",resList3);
       return Result.ok(resMap);
   }
}
