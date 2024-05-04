package cc.cinea.huanyou.repository;

import cc.cinea.huanyou.entity.Appeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppealRepository extends JpaRepository<Appeal, Long> {
    @Query("select a from Appeal a where a.ban.userId = :userId")
    List<Appeal> findAllOfUserId(@Param("userId") Long userId);
}
