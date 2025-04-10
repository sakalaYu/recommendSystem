package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.dto.FileContentDTO;
import org.example.dto.LoginFormDTO;
import org.example.dto.RegisterDTO;
import org.example.dto.Result;
import org.example.entity.FileContent;
import org.example.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author：wzm
 * @Package：org.example.service.impl
 * @Project：tuijianserver
 * @name：UserService
 * @Date：2024/1/31 18:22
 * @Filename：UserService
 */
public interface IUserService extends IService<User> {
    Result login(LoginFormDTO loginForm, HttpSession session);
    Result info(FileContentDTO fileContentDTO);
    Result logout(HttpServletRequest request);
    Result register(RegisterDTO registerDTO);
}
