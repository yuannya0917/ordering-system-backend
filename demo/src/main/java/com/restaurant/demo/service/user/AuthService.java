package com.restaurant.demo.service.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.restaurant.demo.dto.user.*;
import com.restaurant.demo.vo.ResultVo;
import com.restaurant.demo.vo.user.LoginRespVo;
import com.restaurant.demo.vo.user.UserInfoVo;
import com.restaurant.demo.vo.user.UserListVo;
import java.util.List;

public interface AuthService {
    ResultVo<String> register(RegisterDto registerDto);
    ResultVo<LoginRespVo> login(LoginDto loginDto);
    ResultVo<String> updateUser(UpdateUserDto updateUserDto);
    ResultVo<UserInfoVo> getUserInfo(String userId, String currentUserId);
    ResultVo<String> updatePassword(UpdatePasswordDto updatePasswordDto);
    ResultVo<String> forgotPassword(ForgotPasswordDto forgotPasswordDto);
    ResultVo<String> deleteAccount(DeleteAccountDto deleteAccountDto);
    ResultVo<Page<UserListVo>> queryUserList(UserQueryDto userQueryDto);
    ResultVo<List<UserListVo>> getAllUsers(String currentUserId);

    // 获取密保问题
    ResultVo<String> getSecurityQuestion(String userId);
}