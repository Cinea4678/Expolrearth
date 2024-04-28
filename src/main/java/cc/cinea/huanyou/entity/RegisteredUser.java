package cc.cinea.huanyou.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 用户
 *
 * @author cinea
 */
@Data
@Entity
public class RegisteredUser {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 密码哈希
     */
    private String passwordHash;

    /**
     * 性别
     */
    private String gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 签名档
     */
    private String signature;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 关注的用户
     */
    @ManyToMany
    @JoinTable(
            name = "user_relationship", // 关联表的名称
            joinColumns = @JoinColumn(name = "follower_id"), // 当前用户作为关注者的外键列
            inverseJoinColumns = @JoinColumn(name = "following_id") // 对方用户作为被关注者的外键列
    )
    private List<RegisteredUser> followingRegisteredUser;

    /**
     * 关注用户数量
     */
    private Long following;

    /**
     * 粉丝
     */
    @ManyToMany(mappedBy = "followingRegisteredUser")
    private List<RegisteredUser> fansList;

    /**
     * 粉丝数量
     */
    private Long fans;

    /**
     * 拥有的收藏夹
     */
    @OneToMany
    @JsonBackReference(value = "fav-list")
    private List<Favorites> favoritesList;

    /**
     * 关注的收藏夹
     */
    @OneToMany
    @JsonBackReference(value = "following-fav-list")
    private List<Favorites> favoritesFollowingList;

    /**
     * 是否被封禁
     */
    private boolean isBanned;

    /**
     * 当前持有的封禁
     */
    @OneToMany
    private List<Ban> banList;
}
