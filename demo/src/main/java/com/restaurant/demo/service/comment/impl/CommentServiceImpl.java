package com.restaurant.demo.service.comment.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.demo.entity.user.Comment;
import com.restaurant.demo.mapper.user.CommentMapper;
import com.restaurant.demo.service.comment.CommentService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public List<Comment> getCommentsByOrder(String orderId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getOrderId, orderId);
        wrapper.orderByDesc(Comment::getPublishTime);
        return list(wrapper);
    }

    @Override
    public boolean deleteComment(String commentId, String userId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getCommentId, commentId)
               .eq(Comment::getUserId, userId);
        return remove(wrapper);
    }
}