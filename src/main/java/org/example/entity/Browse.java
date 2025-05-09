package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("browse")
public class Browse {
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("file_id")
    private Integer fileId;

    @TableField("browse_time")
    private LocalDateTime browseTime;
}