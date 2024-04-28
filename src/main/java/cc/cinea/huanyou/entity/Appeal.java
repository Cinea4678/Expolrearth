package cc.cinea.huanyou.entity;

import cc.cinea.huanyou.enums.AppealState;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 申诉
 *
 * @author cinea
 */
@Data
@Entity
public class Appeal {
    /**
     * ID
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 状态
     */
    private AppealState state;

    /**
     * 内容
     */
    private String content;

    /**
     * 申诉日期
     */
    private LocalDateTime appealTime;

    /**
     * 对应的封禁
     */
    @ManyToOne
    private Ban ban;
}
