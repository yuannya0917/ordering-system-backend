package com.restaurant.demo.controller.dish;

import com.restaurant.demo.entity.dish.DishImage;
import com.restaurant.demo.service.dish.DishImageService;
import com.restaurant.demo.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/dish-image")
public class DishImageController {

    @Autowired
    private DishImageService dishImageService;

    @PostMapping("/upload")
    public ResultVo<String> uploadImage(
            @RequestParam("dishId") String dishId,
            @RequestParam("dishName") String dishName,
            @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = dishImageService.uploadOrUpdateImage(dishId, dishName, file);
            return ResultVo.success("上传成功", imageUrl);
        } catch (Exception e) {
            return ResultVo.error(e.getMessage());
        }
    }

    @GetMapping("/{dishId}")
    public ResultVo<DishImage> getImageByDishId(@PathVariable String dishId) {
        DishImage image = dishImageService.getImageByDishId(dishId);
        if (image != null) {
            return ResultVo.success(image);
        } else {
            return ResultVo.error("暂无图片");
        }
    }

    @DeleteMapping("/{dishId}")
    public ResultVo<String> deleteImageByDishId(@PathVariable String dishId) {
        boolean result = dishImageService.deleteImageByDishId(dishId);
        if (result) {
            return ResultVo.success("删除成功", null);
        } else {
            return ResultVo.error("删除失败");
        }
    }
}