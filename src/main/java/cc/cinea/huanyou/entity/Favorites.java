package cc.cinea.huanyou.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 收藏夹
 * @author cinea
 */
@Data
@Entity
public class Favorites {
    /**
     * 收藏夹
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 收藏夹名称
     */
    private String name;

    /**
     * 创建日期
     */
    private LocalDateTime creationTime;

    /**
     * 拥有者
     */
    @OneToOne
    @JsonBackReference
    private RegisteredUser owner;

    /**
     * 旅行攻略
     */
    @ManyToMany
    private List<TravelGuide> itemGuides;

    /**
     * 旅行记录
     */
    @ManyToMany
    private List<TravelRecord> itemRecords;
}
