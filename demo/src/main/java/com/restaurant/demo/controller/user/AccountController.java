package com.restaurant.demo.controller.user;

import com.restaurant.demo.dto.user.DeleteAccountDto;
import com.restaurant.demo.dto.user.ForgotPasswordDto;
import com.restaurant.demo.service.user.AccountService;
import com.restaurant.demo.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // 注销账号
    @DeleteMapping("/delete")
    public ResultVo<String> deleteAccount(@RequestBody DeleteAccountDto deleteAccountDto) {
        return accountService.deleteAccount(deleteAccountDto);
    }

    // 找回密码（密保验证）
    @PutMapping("/forgotPassword")
    public ResultVo<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        return accountService.forgotPassword(forgotPasswordDto);
    }
}