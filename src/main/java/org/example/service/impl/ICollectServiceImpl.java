package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.dto.CollectDto;
import org.example.dto.Result;
import org.example.dto.UserDTO;
import org.example.entity.Collect;
import org.example.entity.FileContent;
import org.example.mapper.CollectMapper;
import org.example.service.ICollectService;
import org.example.service.IFileContentService;
import org.example.util.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.example.util.RedisConstants.BLOG_LIKED_KEY;

/**
 * @Author：wzm
 * @Package：org.example.service.impl
 * @Project：tuijianserver
 * @name：ICollectServiceImpl
 * @Date：2024/2/1 15:11
 * @Filename：ICollectServiceImpl
 */
@Slf4j
@Service
public class ICollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements ICollectService {
    @Resource
    IFileContentService fileContentService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Override
    public Result collect(CollectDto collect) {
        System.out.println(collect);
        int ahId = collect.getAh_id();
        System.out.println("-------------------------ahId"+ahId);
        int file_id = collect.getFile_id();
        System.out.println("-------------------------"+file_id);
        int user_id = UserHolder.getUser().getId();
        System.out.println("-------------------------user_id"+user_id);

        String key = BLOG_LIKED_KEY +file_id ;
        Double score = stringRedisTemplate.opsForZSet().score(key,  String.valueOf(user_id));
        if (score == null) {
            // 3.如果未收藏，可以收藏
            // 3.1.数据库收藏数 + 1
            Collect cl = new Collect();
            cl.setFile_id(file_id);
            cl.setPerson_id(user_id);
            cl.setAnther_id(ahId);
            boolean isSuccess = save(cl);
            // 3.2.保存用户到Redis的set集合  zadd key value score
            if (isSuccess) {
                stringRedisTemplate.opsForZSet().add(key, String.valueOf(user_id), System.currentTimeMillis());
                System.out.println("-------------------------"+file_id);
                FileContent fileContent = fileContentService.query().eq("id", file_id).one();
                Long count = fileContent.getCollect();
                fileContent.setCollect(count +1);
                fileContentService.updateById(fileContent);
            }
        } else {
            // 4.如果已点赞，取消点赞
            // 4.1.数据库点赞数 -1
            FileContent fileContent = fileContentService.query().eq("id", file_id).one();
            System.out.println(fileContent);
            Long count = fileContent.getCollect();
            System.out.println(count);
            fileContent.setCollect(count-1);
            fileContentService.updateById(fileContent);
            System.out.println("-------------------------"+file_id);
            boolean isSuccess = remove(new QueryWrapper<Collect>().eq("person_id",user_id).eq("file_id",file_id));
            // 4.2.把用户从Redis的set集合移除
            if (isSuccess) {
                stringRedisTemplate.opsForZSet().remove(key, String.valueOf(user_id));
            }
        }
//        System.out.println(collectId);
//        FileContent fileContent = fileContentService.query().eq("id", collectId).one();
//        fileContent.setCollect(fileContent.getCollect()+1);
//        fileContentService.updateById(fileContent);
//

        return Result.ok();
    }

    @Override
    public Result showcollect() {
        Integer id = UserHolder.getUser().getId();
        List<Collect> collectList = query().eq("person_id", id).list();
        List<FileContent> fileContentList = new ArrayList<>();
        if (collectList != null && !collectList.isEmpty()) {
            for (Collect collect : collectList) {
                FileContent fileContent = fileContentService.query().eq("id",collect.getFile_id()).one();
                if(fileContent !=null){
                    fileContentList.add(fileContent);
                }else{
                    return Result.fail("没有收藏内容");
                }
            }

            return Result.ok(fileContentList);
        } else {
            return Result.fail("没有");
        }
    }
}
