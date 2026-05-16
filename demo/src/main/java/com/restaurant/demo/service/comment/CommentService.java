package com.restaurant.demo.service.comment;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.demo.entity.comment.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {

    boolean addComment(String orderId, String userId, String content);

    boolean deleteComment(String commentId, String userId);

    List<Comment> getCommentsByOrderId(String orderId);

    List<Comment> getCommentsByUserId(String userId);
}