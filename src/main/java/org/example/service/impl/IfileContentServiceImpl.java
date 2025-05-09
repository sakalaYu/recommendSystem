package org.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.dto.Result;
import org.example.entity.FileContent;
import org.example.entity.Rate;
import org.example.entity.Collect;
import org.example.entity.Download;
import org.example.entity.Browse;
import org.example.mapper.FileContentMapper;
import org.example.mapper.RateMapper;
import org.example.mapper.BrowseMapper;
import org.example.mapper.CollectMapper;
import org.example.mapper.DownloadMapper;
import org.example.service.IFileContentService;
import org.example.util.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import org.example.service.RecommendationService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.example.util.RedisConstants.BLOG_LIKED_KEY;
import static org.example.util.RedisConstants.LOGIN_USER_KEY;

/**
 * @Author：wzm
 * @Package：org.example.service.impl
 * @Project：tuijianserver
 * @name：IfileContentServiceImpl
 * @Date：2024/2/1 13:58
 * @Filename：IfileContentServiceImpl
 */
@Service
public class IfileContentServiceImpl extends ServiceImpl<FileContentMapper, FileContent> implements IFileContentService {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    HttpServletRequest request;
    @Resource
    RecommendationService recommendationService;
    @Resource
    private RateMapper rateMapper;

    @Resource
    private CollectMapper collectMapper;

    @Resource
    private DownloadMapper downloadMapper;

    @Resource
    private BrowseMapper browseMapper;
    @Override
    public Result queryHot(Integer current) {
        // 根据用户查询
        Page<FileContent> page = query()
                .page(new Page<>(current, 20));
        // 获取当前页数据
        List<FileContent> records = page.getRecords();
        // 查询用户
//        records.forEach(blog -> {
//            this.queryBlogUser(blog);
//            this.isBlogLiked(blog);
//        });
        // 获取当前用户 ID
     if (UserHolder.getUser() == null) {
         // 检查是否为登录超时
         String token = request.getHeader("authorization");
         if (token != null && isTokenExpired(token)) {
             System.out.println("---------------用户登录超时---------------");
             return Result.fail( "登录超时，请重新登录");
         }
         System.out.println("---------------UserHolder.getUser() 为 null，用户未登录---------------");
         // 用户未登录，返回文章列表
         return Result.ok(records);
     }
        Integer userId = UserHolder.getUser().getId();

        System.out.println(userId+"userIduserIduserId");
        // 遍历文章列表，查询是否收藏
        for (FileContent fileContent : records) {
            String key = BLOG_LIKED_KEY + fileContent.getId();
            Double score = stringRedisTemplate.opsForZSet().score(key,  String.valueOf(userId));
            fileContent.setIsCollected((score != null) ? 1 : 0);
        }

        System.out.println(records);


        // 模拟用户评分数据（实际项目中应从数据库或缓存中获取）
        Map<Integer, Map<Integer, Integer>> ratings = fetchRatings();

        // 调用推荐算法
        List<Integer> recommendedIds = recommendationService.getRecommendations(ratings, userId, 10);

        // 根据推荐的文章 ID 查询文章内容
        List<FileContent> recommendedArticles = query()
                .in("id", recommendedIds)
                .list();



        return Result.ok(records);

    }

    /**
     * 模拟获取用户评分数据
     * @return 用户评分数据
     */

    /**
     * 模拟获取用户评分数据
     * @return 用户评分数据
     */
    private Map<Integer, Map<Integer, Integer>> fetchRatings() {
        Map<Integer, Map<Integer, Integer>> ratings = new HashMap<>();

        // 获取当前时间和一个月前的时间
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        // 1. 获取评分数据
        List<Rate> rateList = rateMapper.selectList(
                new QueryWrapper<Rate>().ge("updated_at", oneMonthAgo)
        );
        for (Rate rate : rateList) {
            ratings.computeIfAbsent(rate.getUser_id(), k -> new HashMap<>())
                    .merge(rate.getFile_id(), (int) (rate.getRating() * 1.0), Integer::sum);
        }

        // 2. 获取收藏数据
        List<Collect> collectList = collectMapper.selectList(
                new QueryWrapper<Collect>().ge("createTime", oneMonthAgo)
        );
        for (Collect collect : collectList) {
            ratings.computeIfAbsent(collect.getPerson_id(), k -> new HashMap<>())
                    .merge(collect.getFile_id(), (int) (1 * 0.8), Integer::sum);
        }

        // 3. 获取下载数据
        List<Download> downloadList = downloadMapper.selectList(
                new QueryWrapper<Download>().ge("downloadTime", oneMonthAgo)
        );
        for (Download download : downloadList) {
            ratings.computeIfAbsent(download.getUserId(), k -> new HashMap<>())
                    .merge(download.getFileId(), (int) (1 * 0.6), Integer::sum);
        }

        // 4. 获取浏览数据
        List<Browse> browseList = browseMapper.selectList(
                new QueryWrapper<Browse>().ge("browse_time", oneMonthAgo)
        );
        for (Browse browse : browseList) {
            ratings.computeIfAbsent(browse.getUserId(), k -> new HashMap<>())
                    .merge(browse.getFileId(), (int) (1 * 0.4), Integer::sum);
        }
        System.out.println(ratings);
        return ratings;
    }

    @Override
    public Result collect(Integer current) {
        Page<FileContent> page = query()
                .orderByDesc("collect")
                .page(new Page<>(current, 20));
        // 获取当前页数据
        List<FileContent> records = page.getRecords();
        return Result.ok(records);
    }

    @Override
    public Result data_show(Integer current) {
            Page<FileContent> page = query()
                    .orderByDesc("upload")
                    .page(new Page<>(current, 20));
            // 获取当前页数据
            List<FileContent> records = page.getRecords();
            return Result.ok(records);
    }
    /**
     * 判断 token 是否过期
     * @param token 用户的 token
     * @return 如果 token 已过期，返回 true；否则返回 false
     */
    private boolean isTokenExpired(String token) {
        // 根据业务逻辑拼接 Redis 中存储 token 的 key
        String key = LOGIN_USER_KEY + token;
        // 检查 Redis 中是否存在该 key
        return stringRedisTemplate.opsForHash().entries(key).isEmpty();
    }
}
