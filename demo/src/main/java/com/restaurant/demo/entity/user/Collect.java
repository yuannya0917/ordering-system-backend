package com.restaurant.demo.entity.user; // 重点：包名是 entity.user！

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("collect") // 对应数据库collect表
public class Collect {
    private String collectId;
    private String dishId;
    private String linkUrl;
    private String userId;
    private LocalDateTime collectTime;
}