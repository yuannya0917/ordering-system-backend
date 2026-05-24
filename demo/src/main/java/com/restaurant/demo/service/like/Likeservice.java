package com.restaurant.demo.service.like;

public interface Likeservice {
    boolean like(String commentid, String userid);
    boolean unlike(String commentid, String userid);
    boolean isLiked(String commentid, String userid);
    int getLikeCount(String commentid);
}