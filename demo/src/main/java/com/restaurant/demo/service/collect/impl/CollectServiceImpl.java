package com.restaurant.demo.service.collect.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.demo.entity.collect.Collect;
import com.restaurant.demo.mapper.collect.CollectMapper;
import com.restaurant.demo.service.collect.CollectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCollect(String userId, String dishId, String linkUrl) {
        if (isCollected(userId, dishId)) {
            throw new RuntimeException("您已经收藏过这道菜了");
        }
        Collect collect = new Collect();
        // collectId 由 Controller 生成并设置
        collect.setUserId(userId);
        collect.setDishId(dishId);
        collect.setLinkUrl(Objects.requireNonNullElse(linkUrl, ""));
        collect.setCollectTime(LocalDateTime.now());
        return save(collect);
    }

    @Override
    public boolean cancelCollect(String userId, String dishId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .eq(Collect::getDishId, dishId);
        return remove(wrapper);
    }

    @Override
    public List<Collect> getCollectByUserId(String userId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .orderByDesc(Collect::getCollectTime);
        return list(wrapper);
    }

    @Override
    public boolean isCollected(String userId, String dishId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .eq(Collect::getDishId, dishId);
        return count(wrapper) > 0;
    }
}