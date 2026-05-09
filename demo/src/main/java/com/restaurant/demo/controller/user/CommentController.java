package com.restaurant.demo.controller.user;

import com.restaurant.demo.entity.user.Comment;
import com.restaurant.demo.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 新增评价 → 返回 Map
    @PostMapping
    public Map<String, Object> addComment(@RequestBody Comment comment) {
        comment.setPublishTime(LocalDateTime.now());
        comment.setCommentId("cmt" + System.currentTimeMillis());
        boolean saved = commentService.save(comment);
        Map<String, Object> result = new HashMap<>();
        result.put("success", saved);
        result.put("message", saved ? "评价成功" : "评价失败");
        return result;
    }

    // 查询订单评价 → 直接返回 List
    @GetMapping("/order/{orderId}")
    public List<Comment> getCommentsByOrder(@PathVariable String orderId) {
        return commentService.getCommentsByOrder(orderId);
    }

    // 删除评价 → 返回 Map
    @DeleteMapping("/{commentId}/user/{userId}")
    public Map<String, Object> deleteComment(@PathVariable String commentId, @PathVariable String userId) {
        boolean deleted = commentService.deleteComment(commentId, userId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", deleted);
        result.put("message", deleted ? "删除成功" : "删除失败");
        return result;
    }
}