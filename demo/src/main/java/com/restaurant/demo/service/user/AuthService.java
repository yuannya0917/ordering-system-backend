package com.restaurant.demo.service.user;

import com.restaurant.demo.dto.user.RegisterDto;
import com.restaurant.demo.vo.ResultVo;

public interface AuthService {
    ResultVo<String> register(RegisterDto registerDto);
}