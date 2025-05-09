package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author：wzm
 * @Package：org.example.entity
 * @Project：tuijianserver
 * @name：Collect
 * @Date：2024/2/1 15:06
 * @Filename：Collect
 */
@Data
@TableName("collect")
public class Collect {
    private Integer id;
    private Integer anther_id;
    private Integer person_id;
    private Integer file_id;
    private LocalDateTime createTime;
}
