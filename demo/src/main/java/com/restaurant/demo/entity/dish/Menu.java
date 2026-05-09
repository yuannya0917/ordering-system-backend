// Menu.java
package com.restaurant.demo.entity.dish;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data  // 确保有这个注解
@TableName("menu")
public class Menu {
    
    @TableId(type = IdType.ASSIGN_UUID)
    private String menuId;
    
    private String menuName;
    
    private String remark;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}