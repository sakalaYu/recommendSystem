package org.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Result;
import org.example.entity.Browse;
import org.example.entity.Download;
import org.example.entity.FileContent;
import org.example.mapper.DownloadMapper;
import org.example.service.IBrowseService;
import org.example.service.IFileContentService;
import org.example.util.SystemConstants;
import org.example.util.UserHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.example.util.RedisConstants.BLOG_LIKED_KEY;

/**
 * @Author：wzm
 * @Package：org.example.controller
 * @Project：tuijianserver
 * @name：FileController
 * @Date：2024/2/1 13:59
 * @Filename：FileController
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    private DownloadMapper downloadMapper;
    @Resource
    IFileContentService fileContentService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @GetMapping("/show")
    public Result queryHotBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return fileContentService.queryHot(current);
    }
    @GetMapping("/data_show")
    public Result data_show(@RequestParam(value = "current", defaultValue = "1") Integer current){
        return fileContentService.data_show(current);
    }
    @GetMapping("/collect")
    public Result collect(@RequestParam(value = "current", defaultValue = "1") Integer current){
        return fileContentService.collect(current);
    }
    @GetMapping("/download")
    public Result download(@RequestParam("id") Integer id){
        Integer userId=null;
        if (UserHolder.getUser() == null) {
            return Result.fail("用户未登录");
        }else {
            userId = UserHolder.getUser().getId();
            if (userId == null) {
                return Result.fail("用户登录超时");
            }
        }
        System.out.println(userId);
        // 检查 download 表中是否存在记录
        int count = downloadMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Download>()
                        .eq("userId", userId)
                        .eq("fileId", id)
        );

        if (count == 0) {
            // 如果不存在记录，新增下载记录
            Download download = new Download();
            download.setUserId(userId);
            download.setFileId(id);
            download.setDownloadTime(LocalDateTime.now());
            downloadMapper.insert(download);
        }

        // 返回成功结果
        return Result.ok("成功下载");
    }
    @PostMapping("/upFile")
    public Result uploadFile(@RequestParam("file") MultipartFile student_file) {
        try {
            // 获取原始文件名称
            String originalFilename = student_file.getOriginalFilename();
            // 生成新文件名
            String fileName = createNewFileName(originalFilename);
            // 保存文件
            student_file.transferTo(new File(SystemConstants.IMAGE_UPLOAD_DIR, fileName));
            // 返回结果
            log.debug("文件上传成功，{}", fileName);
            log.debug("文件上传初始名称，{}",student_file);
            return Result.ok(fileName);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }
    private String createNewFileName(String originalFilename) {
        // 获取后缀
        String suffix = StrUtil.subAfter(originalFilename, ".", true);
        // 生成目录
        String name = UUID.randomUUID().toString();
        int hash = name.hashCode();
        int d1 = hash & 0xF;
        int d2 = (hash >> 4) & 0xF;
        // 判断目录是否存在
        File dir = new File(SystemConstants.IMAGE_UPLOAD_DIR, StrUtil.format("/blogs/{}/{}", d1, d2));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 生成文件名
        return StrUtil.format("/blogs/{}/{}/{}.{}", d1, d2, name, suffix);
    }
    @GetMapping("/file/delete")
    public Result deleteBlogImg(@RequestParam("name") String filename) {
        File file = new File(SystemConstants.IMAGE_UPLOAD_DIR, filename);
        if (file.isDirectory()) {
            return Result.fail("错误的文件名称");
        }
        FileUtil.del(file);
        return Result.ok();
    }
    @Resource
    private IBrowseService browseService;
    @GetMapping("/show_info")
    public Result queryBlogByUserId(@RequestParam("id") Integer id) {
        // 根据用户查询
        FileContent fileContent = fileContentService.query()
                .eq("id", id).one();

        // 获取当前用户 ID
        Integer userId = UserHolder.getUser() != null ? UserHolder.getUser().getId() : null;
        if (userId == null) {
            return Result.fail("用户登录过期");
        }
        // 记录用户浏览记录
        Browse existingBrowse = browseService.query()
                .eq("user_id", userId)
                .eq("file_id", id)
                .one();

        if (existingBrowse == null) {
            // 如果用户未浏览过，新增记录
            Browse browse = new Browse();
            browse.setUserId(userId);
            browse.setFileId(id);
            browse.setBrowseTime(LocalDateTime.now());
            browseService.save(browse);
        } else {
            // 如果用户已浏览过，更新浏览时间
            existingBrowse.setBrowseTime(LocalDateTime.now());
            browseService.updateById(existingBrowse);
        }

        String key = BLOG_LIKED_KEY + id;

        // 查询 Redis 中是否存在收藏记录
        Double score = stringRedisTemplate.opsForZSet().score(key, String.valueOf(userId));
        int isCollected = (score != null) ? 1 : 0;

        // 将是否收藏的标志添加到返回数据中
        fileContent.setIsCollected(isCollected);

        String rate = "rate:" + id;
        String rating = (String) stringRedisTemplate.opsForHash().get(rate, String.valueOf(userId));
        Double userRating = rating != null ? new Double(rating) : 0.0;

        fileContent.setUserRating(userRating);


        log.debug("文件详情: {}", fileContent);
        return Result.ok(fileContent);
    }
    @GetMapping("/delete")
    public Result delete(@RequestParam("id") Integer id){
        FileContent fileContent = fileContentService.query().eq("id", id).one();
        if(fileContent ==null){
            return Result.fail("没找到此文件呢");
        }
        boolean r = fileContentService.removeById(id);
        if(r){
            System.out.println("删除成功");
            return Result.ok();
        }
        return Result.fail("没删除成功");
    }

}
