package com.restaurant.demo.mapper.comment;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restaurant.demo.entity.comment.Commentlike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommentlikeMapper extends BaseMapper<Commentlike> {
    
    @Select("select count(*) from commentlike where commentid = #{commentid}")
    Integer countByCommentid(String commentid);
}