package com.restaurant.demo.service.comment.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.demo.entity.comment.Comment;
import com.restaurant.demo.mapper.comment.CommentMapper;
import com.restaurant.demo.service.comment.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addComment(String orderId, String userId, String content) {
        if (orderId == null || userId == null || content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("订单ID、用户ID和评论内容不能为空");
        }
        Comment comment = new Comment();
        // commentId 由 Controller 生成并设置，此处不生成
        comment.setOrderId(orderId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setPublishTime(LocalDateTime.now());
        return save(comment);
    }

    @Override
    public boolean deleteComment(String commentId, String userId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getCommentId, commentId)
               .eq(Comment::getUserId, userId);
        return remove(wrapper);
    }

    @Override
    public List<Comment> getCommentsByOrderId(String orderId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getOrderId, orderId)
               .orderByDesc(Comment::getPublishTime);
        return list(wrapper);
    }

    @Override
    public List<Comment> getCommentsByUserId(String userId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getUserId, userId)
               .orderByDesc(Comment::getPublishTime);
        return list(wrapper);
    }
}