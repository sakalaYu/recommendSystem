package org.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Result;
import org.example.entity.FileContent;
import org.example.service.IFileContentService;
import org.example.util.SystemConstants;
import org.example.util.UserHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
    IFileContentService fileContentService;
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
        Integer id1 = UserHolder.getUser().getId();
        System.out.println(id1);
        FileContent fileContent = fileContentService.query().eq("id", id).one();
        String filePath = fileContent.getFile_path();
        return Result.ok(filePath);
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
    @GetMapping("/show_info")
    public Result queryBlogByUserId(@RequestParam("id") Integer id) {
        // 根据用户查询
        FileContent fileContent = fileContentService.query()
                .eq("id", id).one();
        // 获取当前页数据
        log.debug("文件详情",fileContent);
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
