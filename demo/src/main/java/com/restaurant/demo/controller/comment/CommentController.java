package com.restaurant.demo.controller.comment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 添加评论
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addComment(@RequestBody CommentAddDTO dto) {
        Map<String, Object> response = new HashMap<>();
        
        System.out.println("===== 收到添加评论请求 =====");
        System.out.println("orderId: " + dto.getOrderId());
        System.out.println("userId: " + dto.getUserId());
        System.out.println("content: " + dto.getContent());
        
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

        String commentId = generateCommentId();
        System.out.println("准备插入的 CommentID: " + commentId);
        System.out.println("CommentID 长度: " + commentId.length());
        
        try {
            Comment comment = new Comment();
            comment.setCommentId(commentId);
            comment.setOrderId(dto.getOrderId());
            comment.setUserId(dto.getUserId());
            comment.setContent(dto.getContent());
            comment.setPublishTime(LocalDateTime.now());
            
            boolean saved = commentService.save(comment);
            if (saved) {
                response.put("success", true);
                response.put("commentId", commentId);
                System.out.println("评论插入成功！");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "评论失败，请稍后重试");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            System.err.println("插入评论时出错：");
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "系统错误：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 普通用户删除自己的评论
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
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getCommentId, commentId)
               .eq(Comment::getUserId, userId);
        boolean deleted = commentService.remove(wrapper);
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
     * 商家删除任意评论（不需要 userId）
     */
    @DeleteMapping("/admin/delete/{commentId}")
    public ResponseEntity<Map<String, Object>> adminDeleteComment(@PathVariable String commentId) {
        Map<String, Object> response = new HashMap<>();
        
        if (commentId == null || commentId.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "评论ID不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        
        boolean deleted = commentService.removeById(commentId);
        if (deleted) {
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "删除失败，评论不存在");
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

    /**
     * 管理员查询所有评论（按时间倒序）
     */
    @GetMapping("/admin/list")
    public ResponseEntity<List<CommentVO>> getAllCommentsForAdmin() {
        List<Comment> comments = commentService.list(
            new LambdaQueryWrapper<Comment>().orderByDesc(Comment::getPublishTime)
        );
        List<CommentVO> voList = comments.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(voList);
    }

    // ---------- 私有辅助方法 ----------
    private String generateCommentId() {
        // 使用UUID的前11位，确保长度固定为11
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 11);
    }

    private CommentVO convertToVO(Comment comment) {
        CommentVO vo = new CommentVO();
        BeanUtils.copyProperties(comment, vo);
        return vo;
    }
}