package com.restaurant.demo.entity.comment;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("commentlike")
public class Commentlike {
    @TableId
    private Integer id;
    private String commentid;
    private String userid;
    private LocalDateTime createtime;
}