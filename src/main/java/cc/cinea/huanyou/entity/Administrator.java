package cc.cinea.huanyou.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * 管理员
 * @author cinea
 */
@Data
@Entity
public class Administrator {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 密码哈希
     */
    private String passwordHash;

    /**
     * 手机号
     */
    private String phoneNumber;
}
