package org.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.dto.Result;
import org.example.entity.FileContent;
import org.example.mapper.FileContentMapper;
import org.example.service.IFileContentService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        System.out.println(records);
        return Result.ok(records);
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
}
