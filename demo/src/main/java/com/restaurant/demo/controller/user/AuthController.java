package com.restaurant.demo.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.restaurant.demo.dto.user.*;
import com.restaurant.demo.service.user.AuthService;
import com.restaurant.demo.vo.ResultVo;
import com.restaurant.demo.vo.user.LoginRespVo;
import com.restaurant.demo.vo.user.UserInfoVo;
import com.restaurant.demo.vo.user.UserListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResultVo<String> register(@RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }

    @PostMapping("/login")
    public ResultVo<LoginRespVo> login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PutMapping("/update")
    public ResultVo<String> updateUser(@RequestBody UpdateUserDto updateUserDto) {
        return authService.updateUser(updateUserDto);
    }

    @GetMapping("/info")
    public ResultVo<UserInfoVo> getUserInfo(
            @RequestParam String userId,
            @RequestParam String currentUserId) {
        return authService.getUserInfo(userId, currentUserId);
    }

    @PutMapping("/updatePassword")
    public ResultVo<String> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) {
        return authService.updatePassword(updatePasswordDto);
    }

    @PutMapping("/forgotPassword")
    public ResultVo<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        return authService.forgotPassword(forgotPasswordDto);
    }

    @DeleteMapping("/delete")
    public ResultVo<String> deleteAccount(@RequestBody DeleteAccountDto deleteAccountDto) {
        return authService.deleteAccount(deleteAccountDto);
    }

    @PostMapping("/queryUsers")
    public ResultVo<Page<UserListVo>> queryUserList(@RequestBody UserQueryDto userQueryDto) {
        return authService.queryUserList(userQueryDto);
    }

    @GetMapping("/getAllUsers")
    public ResultVo<List<UserListVo>> getAllUsers(@RequestParam String currentUserId) {
        return authService.getAllUsers(currentUserId);
    }
}