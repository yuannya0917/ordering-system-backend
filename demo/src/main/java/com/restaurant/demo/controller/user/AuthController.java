package com.restaurant.demo.controller.user;

import com.restaurant.demo.dto.user.LoginDto;
import com.restaurant.demo.vo.user.LoginRespVo;
import com.restaurant.demo.vo.user.UserInfoVo;
import com.restaurant.demo.dto.user.RegisterDto;
import com.restaurant.demo.dto.user.UpdatePasswordDto;
import com.restaurant.demo.dto.user.UpdateUserDto;
import com.restaurant.demo.service.user.AuthService;
import com.restaurant.demo.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    ///注册
    @PostMapping("/register")
    public ResultVo<String> register(@RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }


    // 新增登录接口
    @PostMapping("/login")
    public ResultVo<LoginRespVo> login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }


    // 修改用户信息（个人资料）
    @PutMapping("/update")
    public ResultVo<String> updateUser(@RequestBody UpdateUserDto updateUserDto) {
        return authService.updateUser(updateUserDto);
    }

    // 查询用户信息
    @GetMapping("/info")
    public ResultVo<UserInfoVo> getUserInfo(
            @RequestParam String userId,
            @RequestParam String currentUserId) {
        return authService.getUserInfo(userId, currentUserId);
    }

    // 修改密码
    @PutMapping("/updatePassword")
    public ResultVo<String> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) {
        return authService.updatePassword(updatePasswordDto);
    }
}