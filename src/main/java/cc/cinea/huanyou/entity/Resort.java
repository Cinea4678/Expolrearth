package cc.cinea.huanyou.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * 景区
 *
 * @author cinea
 */
@Data
@Entity
public class Resort {
    /**
     * ID
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 地点
     */
    private String address;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片列表
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    private List<String> imageList;

    /**
     * 描述
     * <p>
     * 例如：西湖东靠杭州市区，其余三面环山，面积约6.39平方千米………………
     */
    @Column(columnDefinition = "text")
    private String description;

    /**
     * 攻略列表
     */
    @OneToMany
    private List<TravelGuide> guideList;

    /**
     * 简述
     * <p>
     * 例如：中国十大风景名胜之一，全国首批5A级景区，被列入世界遗产名录
     */
    private String summary;

    /**
     * 点赞
     */
    private Long likes;

    /**
     * 点过赞的用户
     */
    @ManyToMany
    private List<RegisteredUser> likedUser;
}
