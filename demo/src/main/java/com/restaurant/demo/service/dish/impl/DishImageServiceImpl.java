package com.restaurant.demo.service.dish.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = dishId + suffix;

        String saveDir = uploadPath + "dish/";
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 删除旧文件
        File oldFile = new File(saveDir + dishId + ".jpg");
        if (oldFile.exists()) oldFile.delete();
        File oldFilePng = new File(saveDir + dishId + ".png");
        if (oldFilePng.exists()) oldFilePng.delete();

        // 保存新文件
        File dest = new File(saveDir + newFileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("文件保存失败");
        }

        String imageUrl = "/images/dish/" + newFileName;
        
        // 查询是否存在
        LambdaQueryWrapper<DishImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishImage::getDish_id, dishId);
        DishImage existing = dishImageMapper.selectOne(wrapper);

        if (existing == null) {
            // 新增
            DishImage dishImage = new DishImage();
            dishImage.setDish_id(dishId);
            dishImage.setDish_name(dishName);
            dishImage.setImage_url(imageUrl);
            dishImage.setCreate_time(LocalDateTime.now());
            dishImage.setUpdate_time(LocalDateTime.now());
            dishImageMapper.insert(dishImage);
        } else {
            // 更新
            existing.setDish_name(dishName);
            existing.setImage_url(imageUrl);
            existing.setUpdate_time(LocalDateTime.now());
            dishImageMapper.updateById(existing);
        }

        return imageUrl;
    }

    @Override
    public DishImage getImageByDishId(String dishId) {
        LambdaQueryWrapper<DishImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishImage::getDish_id, dishId);
        return dishImageMapper.selectOne(wrapper);
    }

    @Override
    public boolean deleteImageByDishId(String dishId) {
        String saveDir = uploadPath + "dish/";
        File file = new File(saveDir + dishId + ".jpg");
        if (file.exists()) file.delete();
        File filePng = new File(saveDir + dishId + ".png");
        if (filePng.exists()) filePng.delete();

        LambdaQueryWrapper<DishImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishImage::getDish_id, dishId);
        return dishImageMapper.delete(wrapper) > 0;
    }
}