package com.restaurant.demo.controller.user;

import com.restaurant.demo.common.Result;
import com.restaurant.demo.entity.user.Comment;
import com.restaurant.demo.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 新增评价
    @PostMapping
    public Result<?> addComment(@RequestBody Comment comment) {
        comment.setPublishTime(LocalDateTime.now());
        // 简单生成 ID，实际可用雪花算法或 UUID
        comment.setCommentId("cmt" + System.currentTimeMillis());
        boolean saved = commentService.save(comment);
        return saved ? Result.success() : Result.error("评价失败");
    }

    // 根据订单查询评价
    @GetMapping("/order/{orderId}")
    public Result<List<Comment>> getCommentsByOrder(@PathVariable String orderId) {
        return Result.success(commentService.getCommentsByOrder(orderId));
    }

    // 删除自己的评价
    @DeleteMapping("/{commentId}/user/{userId}")
    public Result<?> deleteComment(@PathVariable String commentId, @PathVariable String userId) {
        return commentService.deleteComment(commentId, userId) ? Result.success() : Result.error("删除失败");
    }
}