package com.restaurant.demo.controller.comment;

import com.restaurant.demo.dto.comment.CommentAddDTO;
import com.restaurant.demo.dto.comment.CommentVO;
import com.restaurant.demo.entity.comment.Comment;
import com.restaurant.demo.service.comment.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 添加评论（修复 commentId 未设置的问题）
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addComment(@RequestBody CommentAddDTO dto) {
        Map<String, Object> response = new HashMap<>();
        // 参数校验
        if (dto.getOrderId() == null || dto.getOrderId().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "订单ID不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        if (dto.getUserId() == null || dto.getUserId().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "用户ID不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "评论内容不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        // 生成评论ID
        String commentId = generateCommentId();

        // 构建评论实体
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setOrderId(dto.getOrderId());
        comment.setUserId(dto.getUserId());
        comment.setContent(dto.getContent());
        comment.setPublishTime(LocalDateTime.now());

        try {
            boolean saved = commentService.save(comment);
            if (saved) {
                response.put("success", true);
                response.put("commentId", commentId);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "评论失败，请稍后重试");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 删除评论（仅评论作者本人可删）
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteComment(@RequestParam String commentId,
                                                              @RequestParam String userId) {
        Map<String, Object> response = new HashMap<>();
        if (commentId == null || commentId.trim().isEmpty() ||
            userId == null || userId.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "参数不完整");
            return ResponseEntity.badRequest().body(response);
        }
        boolean deleted = commentService.deleteComment(commentId, userId);
        response.put("success", deleted);
        if (deleted) {
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "删除失败，评论不存在或无权删除");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * 查询某个订单的所有评论
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<CommentVO>> getCommentsByOrderId(@PathVariable String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Comment> comments = commentService.getCommentsByOrderId(orderId);
        List<CommentVO> voList = comments.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(voList);
    }

    /**
     * 查询某个用户的所有评论
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentVO>> getCommentsByUserId(@PathVariable String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Comment> comments = commentService.getCommentsByUserId(userId);
        List<CommentVO> voList = comments.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(voList);
    }

    // ---------- 私有辅助方法 ----------
    private String generateCommentId() {
        long millis = System.currentTimeMillis();
        String timePart = Long.toString(millis % 10000000); // 后7位
        int random = ThreadLocalRandom.current().nextInt(10);
        return "cmt" + timePart + random;
    }

    private CommentVO convertToVO(Comment comment) {
        CommentVO vo = new CommentVO();
        BeanUtils.copyProperties(comment, vo);
        return vo;
    }
}