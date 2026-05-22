package com.restaurant.demo.dto.collect;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CollectVO {
    private String collectId;
    private String dishId;
    private String linkUrl;
    private String userId;
    private LocalDateTime collectTime;
}