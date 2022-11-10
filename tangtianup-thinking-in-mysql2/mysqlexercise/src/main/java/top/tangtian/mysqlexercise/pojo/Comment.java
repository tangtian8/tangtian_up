package top.tangtian.mysqlexercise.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author tangtian
 * @description
 * @date 2022/11/5 22:14
 */
@TableName("comment")
public class Comment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer  id;
    private Integer  userId;
    private Integer  contentId;
    private String content;

    public Integer  getId() {
        return id;
    }

    public void setId(Integer  id) {
        this.id = id;
    }

    public Integer  getUserId() {
        return userId;
    }

    public void setUserId(Integer  userId) {
        this.userId = userId;
    }

    public Integer  getContentId() {
        return contentId;
    }

    public void setContentId(Integer  contentId) {
        this.contentId = contentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
