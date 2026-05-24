package com.restaurant.demo.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.restaurant.demo.dto.user.*;
import com.restaurant.demo.entity.user.Administrator;
import com.restaurant.demo.entity.user.Customer;
import com.restaurant.demo.entity.user.User;
import com.restaurant.demo.entity.order.Order;
import com.restaurant.demo.mapper.collect.CollectMapper;
import com.restaurant.demo.mapper.comment.CommentMapper;
import com.restaurant.demo.mapper.order.OrderDetailMapper;
import com.restaurant.demo.mapper.order.OrderMapper;
import com.restaurant.demo.mapper.user.AdministratorMapper;
import com.restaurant.demo.mapper.user.CustomerMapper;
import com.restaurant.demo.mapper.user.UserMapper;
import com.restaurant.demo.mapper.user.OrderExtMapper;
import com.restaurant.demo.service.user.AuthService;
import com.restaurant.demo.vo.ResultVo;
import com.restaurant.demo.vo.user.LoginRespVo;
import com.restaurant.demo.vo.user.UserInfoVo;
import com.restaurant.demo.vo.user.UserListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.restaurant.demo.entity.order.Order;
import com.restaurant.demo.entity.order.OrderDetail;
import com.restaurant.demo.entity.comment.Comment;
import com.restaurant.demo.entity.collect.Collect;
import com.restaurant.demo.mapper.order.OrderMapper;
import com.restaurant.demo.mapper.order.OrderDetailMapper;
import com.restaurant.demo.mapper.comment.CommentMapper;
import com.restaurant.demo.mapper.collect.CollectMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.List;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private AdministratorMapper administratorMapper;

    @Autowired
    private OrderExtMapper orderExtMapper;

    @Autowired
private OrderMapper orderMapper;

@Autowired
private OrderDetailMapper orderDetailMapper;

@Autowired
private CommentMapper commentMapper;

@Autowired
private CollectMapper collectMapper;

    // ========== 注册 ==========
    @Override
    @Transactional
    public ResultVo<String> register(RegisterDto dto) {
        String userId = dto.getUserId();
        String userType = dto.getUserType();
        String userPassword = dto.getUserPassword();

        if (userId == null || userId.trim().isEmpty()) {
            return ResultVo.error("手机号不能为空");
        }
        if (!userId.matches("\\d{11}")) {
            return ResultVo.error("手机号必须为11位数字");
        }
        if (userPassword == null || userPassword.trim().isEmpty()) {
            return ResultVo.error("密码不能为空");
        }
        if (userType == null || (!"customer".equals(userType) && !"admin".equals(userType))) {
            return ResultVo.error("用户类型错误");
        }

        if ("customer".equals(userType)) {
            if (dto.getSecurityQuestion() == null || dto.getSecurityQuestion().trim().isEmpty()) {
                return ResultVo.error("安全问题不能为空");
            }
            if (dto.getSecurityAnswer() == null || dto.getSecurityAnswer().trim().isEmpty()) {
                return ResultVo.error("安全答案不能为空");
            }
        }

        if ("admin".equals(userType)) {
            if (dto.getMerchantName() == null || dto.getMerchantName().trim().isEmpty()) {
                return ResultVo.error("商家名称不能为空");
            }
        }

        if (userMapper.selectById(userId) != null) {
            return ResultVo.error("手机号已注册");
        }

        User user = new User();
        user.setUserId(userId);
        user.setUserPassword(userPassword);
        userMapper.insert(user);

        if ("customer".equals(userType)) {
            Customer customer = new Customer();
            customer.setUserId(userId);
            customer.setUsername(userId);
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

    // ========== 修改用户信息 ==========
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
            if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
                customer.setUsername(dto.getUsername());
            }
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

    // ========== 查询用户信息（含消费账单和订单ID列表） ==========
@Override
public ResultVo<UserInfoVo> getUserInfo(String userId, String currentUserId) {
    String currentUserType = getUserType(currentUserId);
    if (currentUserType == null) {
        return ResultVo.error("当前用户不存在");
    }
    if (!"admin".equals(currentUserType) && !currentUserId.equals(userId)) {
        return ResultVo.error("无权限查看他人信息");
    }

    String userType = getUserType(userId);
    if (userType == null) {
        return ResultVo.error("要查询的用户不存在");
    }

    UserInfoVo vo = new UserInfoVo();
    vo.setUserId(userId);
    vo.setUserType(userType);

    if ("customer".equals(userType)) {
        Customer customer = customerMapper.selectById(userId);
        vo.setUsername(customer.getUsername());
        vo.setSecurityQuestion(customer.getSecurityQuestion());
        vo.setSecurityAnswer(customer.getSecurityAnswer());

        // 账单信息
        BigDecimal totalAmount = orderExtMapper.selectTotalAmountByUserId(userId);
        Integer orderCount = orderExtMapper.selectCountByUserId(userId);
        vo.setTotalAmount(totalAmount != null ? totalAmount : BigDecimal.ZERO);
        vo.setOrderCount(orderCount != null ? orderCount : 0);
        
        // 新增：订单ID列表
        List<String> orderIds = orderExtMapper.selectOrderIdsByUserId(userId);
        vo.setOrderIds(orderIds);
        
    } else if ("admin".equals(userType)) {
        Administrator admin = administratorMapper.selectById(userId);
        vo.setMerchantName(admin.getMerchantName());
        vo.setTotalAmount(BigDecimal.ZERO);
        vo.setOrderCount(0);
        vo.setOrderIds(null);
    }

    return ResultVo.success(vo);
}

    // ========== 商家查询用户列表（分页） ==========
    @Override
    public ResultVo<Page<UserListVo>> queryUserList(UserQueryDto dto) {
        String currentUserType = getUserType(dto.getCurrentUserId());
        if (!"admin".equals(currentUserType)) {
            return ResultVo.error("无权限查询用户列表");
        }

        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        if (dto.getUserId() != null && !dto.getUserId().isEmpty()) {
            wrapper.like(Customer::getUserId, dto.getUserId());
        }
        if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
            wrapper.like(Customer::getUsername, dto.getUsername());
        }

        Page<Customer> page = new Page<>(dto.getPage(), dto.getPageSize());
        Page<Customer> customerPage = customerMapper.selectPage(page, wrapper);

        Page<UserListVo> resultPage = new Page<>();
        resultPage.setCurrent(customerPage.getCurrent());
        resultPage.setSize(customerPage.getSize());
        resultPage.setTotal(customerPage.getTotal());
        resultPage.setPages(customerPage.getPages());

        List<UserListVo> list = customerPage.getRecords().stream().map(customer -> {
            UserListVo vo = new UserListVo();
            vo.setUserId(customer.getUserId());
            vo.setUsername(customer.getUsername());
            vo.setSecurityQuestion(customer.getSecurityQuestion());

            BigDecimal totalAmount = orderExtMapper.selectTotalAmountByUserId(customer.getUserId());
            Integer orderCount = orderExtMapper.selectCountByUserId(customer.getUserId());
            vo.setTotalAmount(totalAmount != null ? totalAmount : BigDecimal.ZERO);
            vo.setOrderCount(orderCount != null ? orderCount : 0);

            return vo;
        }).collect(Collectors.toList());

        resultPage.setRecords(list);
        return ResultVo.success(resultPage);
    }

    // ========== 商家查询所有用户（不分页） ==========
    @Override
    public ResultVo<List<UserListVo>> getAllUsers(String currentUserId) {
        String currentUserType = getUserType(currentUserId);
        if (!"admin".equals(currentUserType)) {
            return ResultVo.error("无权限查询用户列表");
        }

        List<Customer> customers = customerMapper.selectList(null);

        List<UserListVo> list = customers.stream().map(customer -> {
            UserListVo vo = new UserListVo();
            vo.setUserId(customer.getUserId());
            vo.setUsername(customer.getUsername());
            vo.setSecurityQuestion(customer.getSecurityQuestion());

            BigDecimal totalAmount = orderExtMapper.selectTotalAmountByUserId(customer.getUserId());
            Integer orderCount = orderExtMapper.selectCountByUserId(customer.getUserId());
            vo.setTotalAmount(totalAmount != null ? totalAmount : BigDecimal.ZERO);
            vo.setOrderCount(orderCount != null ? orderCount : 0);

            return vo;
        }).collect(Collectors.toList());

        return ResultVo.success(list);
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

    // ========== 找回密码 ==========
    @Override
    public ResultVo<String> forgotPassword(ForgotPasswordDto dto) {
        String userId = dto.getUserId();
        String securityAnswer = dto.getSecurityAnswer();
        String newPassword = dto.getNewPassword();

        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResultVo.error("账号不存在");
        }

        if (newPassword == null || newPassword.length() < 6) {
            return ResultVo.error("新密码长度至少6位");
        }

        String userType = getUserType(userId);
        if (!"customer".equals(userType)) {
            return ResultVo.error("商家账号请联系管理员找回密码");
        }

        Customer customer = customerMapper.selectById(userId);
        if (customer == null || !customer.getSecurityAnswer().equals(securityAnswer)) {
            return ResultVo.error("密保答案错误");
        }

        user.setUserPassword(newPassword);
        userMapper.updateById(user);

        return ResultVo.success("密码找回成功", null);
    }

    // ========== 注销账号 ==========
@Override
@Transactional
public ResultVo<String> deleteAccount(DeleteAccountDto dto) {
    String userId = dto.getUserId();
    String currentUserId = dto.getCurrentUserId();

    if (!userId.equals(currentUserId)) {
        return ResultVo.error("只能注销自己的账号");
    }

    User user = userMapper.selectById(userId);
    if (user == null) {
        return ResultVo.error("用户不存在");
    }

    // 1. 查询用户的订单ID列表
    LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
    orderWrapper.eq(Order::getUserId, userId);
    List<Order> orders = orderMapper.selectList(orderWrapper);
    
    // 2. 先删除评论（因为 comment 有外键引用 order）
    LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
    commentWrapper.eq(Comment::getUserId, userId);
    commentMapper.delete(commentWrapper);
    
    // 3. 删除订单详情
    for (Order order : orders) {
        LambdaQueryWrapper<OrderDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(OrderDetail::getOrderId, order.getOrderId());
        orderDetailMapper.delete(detailWrapper);
    }
    
    // 4. 删除订单
    orderMapper.delete(orderWrapper);
    
    // 5. 删除收藏
    LambdaQueryWrapper<Collect> collectWrapper = new LambdaQueryWrapper<>();
    collectWrapper.eq(Collect::getUserId, userId);
    collectMapper.delete(collectWrapper);
    
    // 6. 删除 customer 或 administrator
    customerMapper.deleteById(userId);
    administratorMapper.deleteById(userId);
    
    // 7. 最后删除 user
    userMapper.deleteById(userId);

    return ResultVo.success("账号注销成功", null);
}

// ========== 获取密保问题 ==========
@Override
public ResultVo<String> getSecurityQuestion(String userId) {
    // 1. 检查用户是否存在
    User user = userMapper.selectById(userId);
    if (user == null) {
        return ResultVo.error("账号不存在");
    }
    
    // 2. 判断用户类型（只有顾客有密保问题）
    String userType = getUserType(userId);
    if (!"customer".equals(userType)) {
        return ResultVo.error("商家账号不支持密保找回密码");
    }
    
    // 3. 获取密保问题
    Customer customer = customerMapper.selectById(userId);
    if (customer == null || customer.getSecurityQuestion() == null) {
        return ResultVo.error("未设置密保问题");
    }
    
    return ResultVo.success(customer.getSecurityQuestion());
}
    // ========== 辅助方法 ==========
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