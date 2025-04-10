package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.dto.Result;
import org.example.entity.FileContent;

/**
 * @Author：wzm
 * @Package：org.example.service
 * @Project：tuijianserver
 * @name：IFileContentService
 * @Date：2024/2/1 13:57
 * @Filename：IFileContentService
 */
public interface IFileContentService extends IService<FileContent> {
    Result queryHot(Integer current);
    Result data_show(Integer current);
    Result collect(Integer current);
}
