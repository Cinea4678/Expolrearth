package cc.cinea.huanyou.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 旅行攻略
 *
 * @author cinea
 */
@Data
@Entity
public class TravelGuide {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 作者
     */
    @ManyToOne
    private RegisteredUser author;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容（HTML富文本）
     */
    @Column(columnDefinition = "text")
    private String content;

    /**
     * 图片
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    private List<String> images;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 点赞数量
     */
    private Long likes;

    /**
     * 点赞的用户
     */
    @ManyToMany(fetch = FetchType.LAZY)
    private List<RegisteredUser> likedUser;

    /**
     * 收藏数量
     */
    private Long favorites;

    /**
     * 评论
     */
    @OneToMany(fetch = FetchType.EAGER)
    private List<Comment> comments;

    /**
     * 景区
     */
    @ManyToOne
    private Resort resort;
}
