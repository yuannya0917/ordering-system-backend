package com.restaurant.demo.dto.comment;

import lombok.Data;

@Data
public class CommentAddDTO {
    private String orderId;
    private String userId;
    private String content;
}