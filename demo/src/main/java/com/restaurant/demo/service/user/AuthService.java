package com.restaurant.demo.service.user;

import com.restaurant.demo.dto.user.RegisterDto;
import com.restaurant.demo.vo.ResultVo;
import com.restaurant.demo.dto.user.LoginDto; 
import com.restaurant.demo.vo.user.LoginRespVo;


import com.restaurant.demo.dto.user.UpdatePasswordDto;
import com.restaurant.demo.dto.user.UpdateUserDto;
import com.restaurant.demo.vo.user.UserInfoVo;


public interface AuthService {
    //注册
    ResultVo<String> register(RegisterDto registerDto);

    //登录
    ResultVo<LoginRespVo> login(LoginDto loginDto);

    // 修改用户信息（个人资料）
    ResultVo<String> updateUser(UpdateUserDto updateUserDto);
    
    // 查询用户信息
    ResultVo<UserInfoVo> getUserInfo(String userId, String currentUserId);
    
    // 修改密码
    ResultVo<String> updatePassword(UpdatePasswordDto updatePasswordDto);
}