package com.restaurant.demo.service.like.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.restaurant.demo.entity.comment.Comment;
import com.restaurant.demo.entity.comment.Commentlike;
import com.restaurant.demo.mapper.comment.CommentMapper;
import com.restaurant.demo.mapper.comment.CommentlikeMapper;
import com.restaurant.demo.service.like.Likeservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class LikeserviceImpl implements Likeservice {

    @Autowired
    private CommentlikeMapper commentlikeMapper;
    
    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Transactional
    public boolean like(String commentid, String userid) {
        Comment comment = commentMapper.selectById(commentid);
        if (comment == null) {
            return false;
        }
        
        LambdaQueryWrapper<Commentlike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Commentlike::getCommentid, commentid)
               .eq(Commentlike::getUserid, userid);
        if (commentlikeMapper.selectCount(wrapper) > 0) {
            return false;
        }
        
        Commentlike like = new Commentlike();
        like.setCommentid(commentid);
        like.setUserid(userid);
        like.setCreatetime(LocalDateTime.now());
        commentlikeMapper.insert(like);
        
        Integer current = comment.getLikes();
        comment.setLikes(current == null ? 1 : current + 1);
        commentMapper.updateById(comment);
        
        return true;
    }

    @Override
    @Transactional
    public boolean unlike(String commentid, String userid) {
        LambdaQueryWrapper<Commentlike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Commentlike::getCommentid, commentid)
               .eq(Commentlike::getUserid, userid);
        int deleted = commentlikeMapper.delete(wrapper);
        
        if (deleted > 0) {
            Comment comment = commentMapper.selectById(commentid);
            if (comment != null && comment.getLikes() != null && comment.getLikes() > 0) {
                comment.setLikes(comment.getLikes() - 1);
                commentMapper.updateById(comment);
            }
        }
        
        return deleted > 0;
    }

    @Override
    public boolean isLiked(String commentid, String userid) {
        LambdaQueryWrapper<Commentlike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Commentlike::getCommentid, commentid)
               .eq(Commentlike::getUserid, userid);
        return commentlikeMapper.selectCount(wrapper) > 0;
    }

    @Override
    public int getLikeCount(String commentid) {
        return commentlikeMapper.countByCommentid(commentid);
    }
}