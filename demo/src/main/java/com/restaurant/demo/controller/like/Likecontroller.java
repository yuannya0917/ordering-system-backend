package com.restaurant.demo.controller.like;

import com.restaurant.demo.service.like.Likeservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/like")
public class Likecontroller {

    @Autowired
    private Likeservice likeservice;

    @PostMapping("/add")
    public Map<String, Object> like(@RequestParam String commentid, @RequestParam String userid) {
        Map<String, Object> result = new HashMap<>();
        boolean success = likeservice.like(commentid, userid);
        result.put("success", success);
        result.put("message", success ? "点赞成功" : "已点过赞或评论不存在");
        return result;
    }

    @DeleteMapping("/cancel")
    public Map<String, Object> unlike(@RequestParam String commentid, @RequestParam String userid) {
        Map<String, Object> result = new HashMap<>();
        boolean success = likeservice.unlike(commentid, userid);
        result.put("success", success);
        result.put("message", success ? "取消点赞成功" : "取消点赞失败");
        return result;
    }

    @GetMapping("/check")
    public Map<String, Object> isLiked(@RequestParam String commentid, @RequestParam String userid) {
        Map<String, Object> result = new HashMap<>();
        boolean liked = likeservice.isLiked(commentid, userid);
        result.put("liked", liked);
        return result;
    }

    @GetMapping("/count/{commentid}")
    public Map<String, Object> getLikeCount(@PathVariable String commentid) {
        Map<String, Object> result = new HashMap<>();
        int count = likeservice.getLikeCount(commentid);
        result.put("count", count);
        return result;
    }
}