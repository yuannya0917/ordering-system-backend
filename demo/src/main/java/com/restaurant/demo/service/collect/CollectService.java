package com.restaurant.demo.service.collect;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.demo.entity.collect.Collect;
import java.util.List;

public interface CollectService extends IService<Collect> {
    boolean addCollect(String userId, String dishId, String linkUrl);
    boolean cancelCollect(String userId, String dishId);
    List<Collect> getCollectByUserId(String userId);
    boolean isCollected(String userId, String dishId);
}