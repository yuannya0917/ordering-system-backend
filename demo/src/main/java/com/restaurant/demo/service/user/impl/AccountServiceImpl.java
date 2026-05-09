package com.restaurant.demo.service.user.impl;

import com.restaurant.demo.dto.user.DeleteAccountDto;
import com.restaurant.demo.dto.user.ForgotPasswordDto;
import com.restaurant.demo.entity.user.Administrator;
import com.restaurant.demo.entity.user.Customer;
import com.restaurant.demo.entity.user.User;
import com.restaurant.demo.mapper.user.AdministratorMapper;
import com.restaurant.demo.mapper.user.CustomerMapper;
import com.restaurant.demo.mapper.user.UserMapper;
import com.restaurant.demo.service.user.AccountService;
import com.restaurant.demo.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private AdministratorMapper administratorMapper;

    // ========== 注销账号 ==========
    @Override
    @Transactional
    public ResultVo<String> deleteAccount(DeleteAccountDto dto) {
        String userId = dto.getUserId();
        String currentUserId = dto.getCurrentUserId();

        // 1. 只能注销自己的账号
        if (!userId.equals(currentUserId)) {
            return ResultVo.error("只能注销自己的账号");
        }

        // 2. 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResultVo.error("用户不存在");
        }

        // 3. 删除 user 表（由于外键级联，customer/administrator 会自动删除）
        userMapper.deleteById(userId);

        return ResultVo.success("账号注销成功", null);
    }

    // ========== 找回密码（密保验证） ==========
    @Override
    public ResultVo<String> forgotPassword(ForgotPasswordDto dto) {
        String userId = dto.getUserId();
        String securityAnswer = dto.getSecurityAnswer();
        String newPassword = dto.getNewPassword();

        // 1. 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResultVo.error("账号不存在");
        }

        // 2. 检查新密码格式
        if (newPassword == null || newPassword.length() < 6) {
            return ResultVo.error("新密码长度至少6位");
        }

        // 3. 判断用户类型，获取密保答案
        String userType = getUserType(userId);
        if (userType == null) {
            return ResultVo.error("用户信息异常");
        }

        // 只有顾客支持密保找回密码  商家不用找回密码
        if (!"customer".equals(userType)) {
            return ResultVo.error("商家账号请联系管理员找回密码");
        }

        Customer customer = customerMapper.selectById(userId);
        if (customer == null) {
            return ResultVo.error("用户信息异常");
        }

        // 4. 验证密保答案
        if (securityAnswer == null || !securityAnswer.equals(customer.getSecurityAnswer())) {
            return ResultVo.error("密保答案错误");
        }

        // 5. 修改密码
        user.setUserPassword(newPassword);
        userMapper.updateById(user);

        return ResultVo.success("密码找回成功", null);
    }

    // ========== 辅助方法：判断用户类型 ==========
    private String getUserType(String userId) {
        if (customerMapper.selectById(userId) != null) {
            return "customer";
        }
        if (administratorMapper.selectById(userId) != null) {
            return "admin";
        }
        return null;
    }
}