// UpdateMenuDto.java
package com.restaurant.demo.dto.dish;

import lombok.Data;

@Data
public class UpdateMenuDto {
    private String menuId;
    private String menuName;
    private String remark;
}