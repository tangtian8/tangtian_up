package top.tangtian.mysqlexercise.controller;

import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangtian.mysqlexercise.mapper.CommentMapper;
import top.tangtian.mysqlexercise.pojo.Comment;

import java.util.List;
import java.util.UUID;

/**
 * @author tangtian
 * @description
 * @date 2022/11/5 22:23
 */
@RestController()
@RequestMapping("comment")
public class CommentController {
    @Autowired
    private CommentMapper commentMapper;


    @GetMapping("/findAll")
    public List<Comment> findALlComment(){
        return commentMapper.findAll();
    }
    @GetMapping("/insert")
    public Comment insertComment(){
        Comment comment = new Comment();
        comment.setUserId(2);
        comment.setContentId(2);
        comment.setContent(UUID.randomUUID().toString());
        commentMapper.insert(comment);
        return commentMapper.selectById(comment.getId());
    }
}
