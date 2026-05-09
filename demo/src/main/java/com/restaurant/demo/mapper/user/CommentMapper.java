package com.restaurant.demo.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restaurant.demo.entity.user.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}