package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.dto.Result;
import org.example.entity.Rate;
import org.example.mapper.RateMapper;
import org.example.service.IRateService;
import org.example.util.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static org.example.util.RedisConstants.BLOG_LIKED_KEY;

@Service
public class RateServiceImpl extends ServiceImpl<RateMapper, Rate> implements IRateService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result rate(Integer fileId, Integer ahId, Double rating) {
        Integer userId = UserHolder.getUser() != null ? UserHolder.getUser().getId() : null;
        if (userId == null) {
            return Result.fail("用户登录过期");
        }

        String key = "rate:" + fileId;
        // 保存评分到 Redis
        stringRedisTemplate.opsForHash().put(key, String.valueOf(userId), String.valueOf(rating));

        // 保存评分到数据库
        Rate rate = query().eq("file_id", fileId).eq("user_id", userId).one();
        if (rate == null) {
            rate = new Rate();
            rate.setFile_id(fileId);
            rate.setUser_id(userId);
            rate.setRating(rating);
            save(rate);
        } else {
            rate.setRating(rating);
            updateById(rate);
        }

        return Result.ok();
    }
}
