package com.restaurant.demo.entity.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {
    @TableId
    private String commentId;      // CommentID, varchar(11) PK
    private String orderId;        // OrderID, 关联 order 表
    private String userId;         // userId, 关联 customer 表
    private String content;        // Content, text
    private LocalDateTime publishTime; // PublishTime, datetime
}