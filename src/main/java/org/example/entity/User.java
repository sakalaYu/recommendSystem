package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author：wzm
 * @Package：org.example.entity
 * @Project：tuijianserver
 * @name：User
 * @Date：2024/1/31 18:20
 * @Filename：User
 */
@Data
@TableName("user")
public class User {
    private Integer id;
    private String username;
    private String password;
    private String userLKnm;
    private Long comment_user;
    private String user_icon;
    private Long down_count;

}
