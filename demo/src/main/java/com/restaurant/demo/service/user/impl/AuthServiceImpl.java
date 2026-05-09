package com.restaurant.demo.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.restaurant.demo.dto.user.RegisterDto;
import com.restaurant.demo.entity.user.Administrator;
import com.restaurant.demo.entity.user.Customer;
import com.restaurant.demo.entity.user.User;
import com.restaurant.demo.mapper.user.AdministratorMapper;
import com.restaurant.demo.mapper.user.CustomerMapper;
import com.restaurant.demo.mapper.user.UserMapper;
import com.restaurant.demo.service.user.AuthService;
import com.restaurant.demo.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private AdministratorMapper administratorMapper;

    @Override
    @Transactional
    public ResultVo<String> register(RegisterDto dto) {
        String userId = dto.getUserId();
        String userType = dto.getUserType();

        // 1. 检查是否已注册
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserId, userId);
        if (userMapper.selectCount(wrapper) > 0) {
            return ResultVo.error("手机号已注册");
        }

        // 2. 保存 User 表
        User user = new User();
        user.setUserId(userId);
        user.setUserPassword(dto.getUserPassword());
        user.setUserType(userType);
        userMapper.insert(user);

        // 3. 保存子表
        if ("customer".equals(userType)) {
            Customer customer = new Customer();
            customer.setUserId(userId);
            customer.setUserPassword(dto.getUserPassword());
            customer.setSecurityQuestion(dto.getSecurityQuestion());
            customer.setSecurityAnswer(dto.getSecurityAnswer());
            customerMapper.insert(customer);
            return ResultVo.success("顾客注册成功", userId);

        } else if ("admin".equals(userType)) {
            Administrator admin = new Administrator();
            admin.setUserId(userId);
            admin.setUserPassword(dto.getUserPassword());
            admin.setMerchantName(dto.getMerchantName());
            administratorMapper.insert(admin);
            return ResultVo.success("商家注册成功", userId);
        }

        return ResultVo.error("用户类型错误");
    }
}