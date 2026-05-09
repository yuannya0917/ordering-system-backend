package com.restaurant.demo.service.comment;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.demo.entity.user.Comment;
import java.util.List;

public interface CommentService extends IService<Comment> {
    List<Comment> getCommentsByOrder(String orderId);
    boolean deleteComment(String commentId, String userId);
}