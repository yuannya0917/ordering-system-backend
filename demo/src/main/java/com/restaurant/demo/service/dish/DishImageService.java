package com.restaurant.demo.service.dish;

import com.restaurant.demo.entity.dish.DishImage;
import org.springframework.web.multipart.MultipartFile;

public interface DishImageService {
    
    // 上传/更新菜品图片（一菜一图，新图覆盖旧图）
    String uploadOrUpdateImage(String dishId, String dishName, MultipartFile file);
    
    // 根据菜品ID查询图片
    DishImage getImageByDishId(String dishId);
    
    // 根据菜品ID删除图片
    boolean deleteImageByDishId(String dishId);
}