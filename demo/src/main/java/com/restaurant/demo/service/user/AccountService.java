package com.restaurant.demo.service.user;

import com.restaurant.demo.dto.user.DeleteAccountDto;
import com.restaurant.demo.dto.user.ForgotPasswordDto;
import com.restaurant.demo.vo.ResultVo;

public interface AccountService {
    
    // 注销账号
    ResultVo<String> deleteAccount(DeleteAccountDto dto);
    
    // 找回密码（通过密保验证）
    ResultVo<String> forgotPassword(ForgotPasswordDto dto);
}