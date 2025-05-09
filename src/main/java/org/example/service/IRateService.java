package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.dto.Result;
import org.example.entity.Rate;

public interface IRateService extends IService<Rate> {
    Result rate(Integer fileId, Integer ahId, Double rating);
}
