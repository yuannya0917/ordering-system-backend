package com.restaurant.demo.entity.collect;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("collect")
public class Collect {
    @TableId(value = "CollectID")
    private String collectId;

    @TableField("dishID")
    private String dishId;

    @TableField("LinkUrl")
    private String linkUrl;

    @TableField("userID")
    private String userId;

    @TableField("CollectTime")
    private LocalDateTime collectTime;
}