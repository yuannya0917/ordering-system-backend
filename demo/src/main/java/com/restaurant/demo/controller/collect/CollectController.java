package com.restaurant.demo.controller.collect;

import com.restaurant.demo.dto.collect.CollectAddDTO;
import com.restaurant.demo.dto.collect.CollectVO;
import com.restaurant.demo.entity.collect.Collect;
import com.restaurant.demo.service.collect.CollectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addCollect(@RequestBody CollectAddDTO dto) {
        Map<String, Object> response = new HashMap<>();
        // 手动校验
        if (dto.getUserId() == null || dto.getUserId().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "用户ID不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        if (dto.getDishId() == null || dto.getDishId().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "菜品ID不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        // 重复检查
        if (collectService.isCollected(dto.getUserId(), dto.getDishId())) {
            response.put("success", false);
            response.put("message", "您已经收藏过这道菜了");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        String collectId = generateCollectId();
        Collect collect = new Collect();
        collect.setCollectId(collectId);
        collect.setUserId(dto.getUserId());
        collect.setDishId(dto.getDishId());
        collect.setLinkUrl(dto.getLinkUrl() == null ? "" : dto.getLinkUrl());
        collect.setCollectTime(LocalDateTime.now());

        boolean saved = collectService.save(collect);
        if (saved) {
            response.put("success", true);
            response.put("collectId", collectId);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "收藏失败，请稍后重试");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<Map<String, Object>> cancelCollect(@RequestParam String userId,
                                                              @RequestParam String dishId) {
        Map<String, Object> response = new HashMap<>();
        if (userId == null || userId.trim().isEmpty() ||
            dishId == null || dishId.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "参数不完整");
            return ResponseEntity.badRequest().body(response);
        }
        boolean removed = collectService.cancelCollect(userId, dishId);
        response.put("success", removed);
        if (removed) {
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "取消收藏失败，可能该收藏不存在");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<CollectVO>> listCollect(@RequestParam String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Collect> collects = collectService.getCollectByUserId(userId);
        List<CollectVO> voList = collects.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(voList);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkCollected(@RequestParam String userId,
                                                   @RequestParam String dishId) {
        if (userId == null || userId.trim().isEmpty() ||
            dishId == null || dishId.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        boolean collected = collectService.isCollected(userId, dishId);
        return ResponseEntity.ok(collected);
    }

    // ---------- 私有辅助方法 ----------
    private String generateCollectId() {
        // 使用时间戳后11位，确保长度固定为11
        String timestamp = String.valueOf(System.currentTimeMillis());
        if (timestamp.length() > 11) {
            timestamp = timestamp.substring(timestamp.length() - 11);
        }
        return timestamp;
    }

    private CollectVO convertToVO(Collect collect) {
        CollectVO vo = new CollectVO();
        BeanUtils.copyProperties(collect, vo);
        return vo;
    }
}