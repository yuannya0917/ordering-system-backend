package com.restaurant.demo.entity.comment;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {

    @TableId(value = "CommentID")
    private String commentId;

    @TableField("OrderID")
    private String orderId;

    @TableField("userID")
    private String userId;

    @TableField("Content")
    private String content;

    @TableField("PublishTime")
    private LocalDateTime publishTime;
}