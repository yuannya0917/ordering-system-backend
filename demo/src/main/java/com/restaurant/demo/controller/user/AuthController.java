package com.restaurant.demo.controller.user;

import com.restaurant.demo.dto.user.RegisterDto;
import com.restaurant.demo.service.user.AuthService;
import com.restaurant.demo.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResultVo<String> register(@RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }
}