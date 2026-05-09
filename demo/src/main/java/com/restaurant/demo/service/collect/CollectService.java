package com.restaurant.demo.service.collect;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.demo.entity.user.Collect; // 重点：导入 entity.user.Collect！
import java.util.List;

public interface CollectService extends IService<Collect> {
    List<Collect> getCollectByUserId(String userId);
    boolean cancelCollect(String userId, String dishId);
    boolean isCollected(String userId, String dishId);
}