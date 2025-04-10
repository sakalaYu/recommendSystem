package org.example.dto;

import lombok.Data;

/**
 * @Author：wzm
 * @Package：org.example.dto
 * @Project：tuijianserver
 * @name：RegisterDTO
 * @Date：2024/3/8 15:32
 * @Filename：RegisterDTO
 */
@Data
public class RegisterDTO {
    private String username;
    private String res_password;
    private String password;
    private String userLKnm;
}
