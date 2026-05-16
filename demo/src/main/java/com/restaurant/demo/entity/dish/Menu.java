// Menu.java
package com.restaurant.demo.entity.dish;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data  // 确保有这个注解
@TableName("menu")
public class Menu {
    
    @TableId(type = IdType.INPUT)
    @TableField("menuID")
    private String menuId;
    
    @TableField("menuName")
    private String menuName;
    
    @TableField("remark")
    private String remark;
    
    @TableField("createTime")
    private LocalDateTime createTime;
    
}