package com.restaurant.demo.service.dish.impl;

import com.restaurant.demo.entity.dish.DishImage;
import com.restaurant.demo.mapper.dish.DishImageMapper;
import com.restaurant.demo.service.dish.DishImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class DishImageServiceImpl implements DishImageService {

    @Autowired
    private DishImageMapper dishImageMapper;

    @Value("${file.upload-path:D:/upload/}")
    private String uploadPath;

    @Override
    public String uploadOrUpdateImage(String dishId, String dishName, MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        // 1. 获取原文件名后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 2. 生成新文件名（用 dishId）
        String newFileName = dishId + suffix;

        // 3. 创建保存目录
        String saveDir = uploadPath + "dish/";
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 4. 删除旧图片文件（如果存在）
        File oldFile = new File(saveDir + dishId + ".jpg");
        if (oldFile.exists()) {
            oldFile.delete();
        }
        File oldFilePng = new File(saveDir + dishId + ".png");
        if (oldFilePng.exists()) {
            oldFilePng.delete();
        }

        // 5. 保存新图片
        File dest = new File(saveDir + newFileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("文件保存失败");
        }

        // 6. 保存/更新数据库
        String imageUrl = "/images/dish/" + newFileName;
        DishImage existing = dishImageMapper.selectByDishId(dishId);
        
        if (existing == null) {
            // 新增
            DishImage dishImage = new DishImage();
            dishImage.setDishId(dishId);
            dishImage.setDishName(dishName);
            dishImage.setImageUrl(imageUrl);
            dishImage.setCreateTime(LocalDateTime.now());
            dishImage.setUpdateTime(LocalDateTime.now());
            dishImageMapper.insert(dishImage);
        } else {
            // 更新
            existing.setImageUrl(imageUrl);
            existing.setUpdateTime(LocalDateTime.now());
            dishImageMapper.updateById(existing);
        }

        return imageUrl;
    }

    @Override
    public DishImage getImageByDishId(String dishId) {
        return dishImageMapper.selectByDishId(dishId);
    }

    @Override
    public boolean deleteImageByDishId(String dishId) {
        // 删除本地文件
        String saveDir = uploadPath + "dish/";
        File file = new File(saveDir + dishId + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        File filePng = new File(saveDir + dishId + ".png");
        if (filePng.exists()) {
            filePng.delete();
        }
        
        // 删除数据库记录
        return dishImageMapper.deleteByDishId(dishId) > 0;
    }
}