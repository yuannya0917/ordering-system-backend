package com.restaurant.demo.service.user.impl;

import com.restaurant.demo.dto.user.LoginDto;
import com.restaurant.demo.dto.user.RegisterDto;
import com.restaurant.demo.dto.user.UpdatePasswordDto;
import com.restaurant.demo.dto.user.UpdateUserDto;
import com.restaurant.demo.entity.user.Administrator;
import com.restaurant.demo.entity.user.Customer;
import com.restaurant.demo.entity.user.User;
import com.restaurant.demo.mapper.user.AdministratorMapper;
import com.restaurant.demo.mapper.user.CustomerMapper;
import com.restaurant.demo.mapper.user.UserMapper;
import com.restaurant.demo.service.user.AuthService;
import com.restaurant.demo.vo.ResultVo;
import com.restaurant.demo.vo.user.LoginRespVo;
import com.restaurant.demo.vo.user.UserInfoVo;
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

    // ========== 注册 ==========
    @Override
    @Transactional
    public ResultVo<String> register(RegisterDto dto) {
        String userId = dto.getUserId();
        String userType = dto.getUserType();

        if (userMapper.selectById(userId) != null) {
            return ResultVo.error("手机号已注册");
        }

        User user = new User();
        user.setUserId(userId);
        user.setUserPassword(dto.getUserPassword());
        userMapper.insert(user);

        if ("customer".equals(userType)) {
            Customer customer = new Customer();
            customer.setUserId(userId);
            customer.setSecurityQuestion(dto.getSecurityQuestion());
            customer.setSecurityAnswer(dto.getSecurityAnswer());
            customerMapper.insert(customer);
            return ResultVo.success("顾客注册成功", userId);
        } else if ("admin".equals(userType)) {
            Administrator admin = new Administrator();
            admin.setUserId(userId);
            admin.setMerchantName(dto.getMerchantName());
            administratorMapper.insert(admin);
            return ResultVo.success("商家注册成功", userId);
        }

        return ResultVo.error("用户类型错误");
    }

    // ========== 登录 ==========
    @Override
    public ResultVo<LoginRespVo> login(LoginDto dto) {
        String userId = dto.getUserId();
        String password = dto.getUserPassword();

        Administrator admin = administratorMapper.selectById(userId);
        if (admin != null) {
            User user = userMapper.selectById(userId);
            if (user != null && user.getUserPassword().equals(password)) {
                LoginRespVo respVo = new LoginRespVo();
                respVo.setUserId(userId);
                respVo.setUserType("admin");
                return ResultVo.success(respVo);
            }
            return ResultVo.error("密码错误");
        }

        Customer customer = customerMapper.selectById(userId);
        if (customer != null) {
            User user = userMapper.selectById(userId);
            if (user != null && user.getUserPassword().equals(password)) {
                LoginRespVo respVo = new LoginRespVo();
                respVo.setUserId(userId);
                respVo.setUserType("customer");
                return ResultVo.success(respVo);
            }
            return ResultVo.error("密码错误");
        }

        return ResultVo.error("账号未注册");
    }

    // ========== 修改用户信息（个人资料） ==========
    @Override
    @Transactional
    public ResultVo<String> updateUser(UpdateUserDto dto) {
        String userId = dto.getUserId();
        
        String userType = getUserType(userId);
        if (userType == null) {
            return ResultVo.error("用户不存在");
        }
        
        if ("customer".equals(userType)) {
            Customer customer = customerMapper.selectById(userId);
            if (dto.getSecurityQuestion() != null) {
                customer.setSecurityQuestion(dto.getSecurityQuestion());
            }
            if (dto.getSecurityAnswer() != null) {
                customer.setSecurityAnswer(dto.getSecurityAnswer());
            }
            customerMapper.updateById(customer);
            return ResultVo.success("顾客信息修改成功", null);
            
        } else if ("admin".equals(userType)) {
            Administrator admin = administratorMapper.selectById(userId);
            if (dto.getMerchantName() != null) {
                admin.setMerchantName(dto.getMerchantName());
            }
            administratorMapper.updateById(admin);
            return ResultVo.success("商家信息修改成功", null);
        }
        
        return ResultVo.error("修改失败");
    }

    // ========== 查询用户信息 ==========
    // 商家可以查看任意用户，顾客只能查看自己的信息
    @Override
    public ResultVo<UserInfoVo> getUserInfo(String userId, String currentUserId) {
        // 1. 权限判断
        String currentUserType = getUserType(currentUserId);
        if (currentUserType == null) {
            return ResultVo.error("当前用户不存在");
        }
        
        // 如果不是商家，且查询的不是自己，则拒绝
        if (!"admin".equals(currentUserType) && !currentUserId.equals(userId)) {
            return ResultVo.error("无权限查看他人信息");
        }
        
        // 2. 查询用户信息
        String userType = getUserType(userId);
        if (userType == null) {
            return ResultVo.error("要查询的用户不存在");
        }
        
        UserInfoVo vo = new UserInfoVo();
        vo.setUserId(userId);
        vo.setUserType(userType);
        
        if ("customer".equals(userType)) {
            Customer customer = customerMapper.selectById(userId);
            vo.setSecurityQuestion(customer.getSecurityQuestion());
            vo.setSecurityAnswer(customer.getSecurityAnswer());
        } else if ("admin".equals(userType)) {
            Administrator admin = administratorMapper.selectById(userId);
            vo.setMerchantName(admin.getMerchantName());
        }
        
        return ResultVo.success(vo);
    }

    // ========== 修改密码 ==========
    @Override
    public ResultVo<String> updatePassword(UpdatePasswordDto dto) {
        String userId = dto.getUserId();
        String verifyType = dto.getVerifyType();
        String newPassword = dto.getNewPassword();
        
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResultVo.error("用户不存在");
        }
        
        if (newPassword == null || newPassword.length() < 6) {
            return ResultVo.error("新密码长度至少6位");
        }
        
        if ("password".equals(verifyType)) {
            String oldPassword = dto.getOldPassword();
            if (oldPassword == null || !user.getUserPassword().equals(oldPassword)) {
                return ResultVo.error("原密码错误");
            }
        } else if ("security".equals(verifyType)) {
            String userType = getUserType(userId);
            if (!"customer".equals(userType)) {
                return ResultVo.error("商家账号不支持密保验证，请使用密码验证");
            }
            
            Customer customer = customerMapper.selectById(userId);
            if (customer == null || !customer.getSecurityAnswer().equals(dto.getSecurityAnswer())) {
                return ResultVo.error("密保答案错误");
            }
        } else {
            return ResultVo.error("验证方式错误");
        }
        
        user.setUserPassword(newPassword);
        userMapper.updateById(user);
        
        return ResultVo.success("密码修改成功", null);
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