package top.tangtian.mysqlexercise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import top.tangtian.mysqlexercise.pojo.Comment;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment>{
    List<Comment> findAll();
}
