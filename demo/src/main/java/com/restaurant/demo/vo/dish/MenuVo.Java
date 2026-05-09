package com.restaurant.demo.vo.dish;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MenuVo {
    private String menuId;
    private String menuName;
    private String remark;
    private LocalDateTime createTime;
}