package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import lombok.Data;

import java.util.Date;

/**
 * @Author：wzm
 * @Package：org.example.entity
 * @Project：tuijianserver
 * @name：FileContent
 * @Date：2024/2/1 13:45
 * @Filename：FileContent
 */
@Data
@TableName("filecontent")
public class FileContent {
    private Integer id;
    private Integer user_id;
    private String file_name;
    private Date upload;
    private Long collect;
    private String title;
    private String file_path;
    private String file_image;
    private Integer type;
    private Long comment;
    private String user_name;
    private Long down_count;
    private String user_image;
    private String price;
    // 新增字段：是否收藏
    private Integer isCollected;
    private Double userRating;
}
