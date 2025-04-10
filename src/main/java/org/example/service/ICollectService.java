package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.dto.CollectDto;
import org.example.dto.Result;
import org.example.entity.Collect;
import org.example.mapper.CollectMapper;

/**
 * @Author：wzm
 * @Package：org.example.service
 * @Project：tuijianserver
 * @name：CollectService
 * @Date：2024/2/1 15:10
 * @Filename：CollectService
 */
public interface ICollectService extends IService<Collect> {
    Result collect(CollectDto collect);
    Result showcollect();
}
