package com.restaurant.demo.dto.user;

import lombok.Data;

@Data
public class DeleteAccountDto {
    private String userId;
    private String currentUserId;
}