package com.restaurant.demo.dto.comment;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentVO {
    private String commentId;
    private String orderId;
    private String userId;
    private String content;
    private LocalDateTime publishTime;
}