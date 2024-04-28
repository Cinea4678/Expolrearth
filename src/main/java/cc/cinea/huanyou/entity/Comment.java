package cc.cinea.huanyou.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论
 *
 * @author cinea
 */
@Data
@Entity
public class Comment {
    /**
     * ID
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 作者
     */
    @ManyToOne
    private RegisteredUser author;

    /**
     * 发表日期
     */
    private LocalDateTime time;

    /**
     * 内容
     */
    private String content;

    /**
     * 点赞数量
     */
    private Long likes;

    /**
     * 点过赞的用户
     */
    @ManyToMany
    private List<RegisteredUser> likedUser;

    /**
     * 回复
     */
    @OneToMany(fetch = FetchType.EAGER)
    private List<Comment> reply;
}
