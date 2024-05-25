package cc.cinea.huanyou.repository;

import cc.cinea.huanyou.entity.Resort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author cinea
 */
public interface ResortRepository extends JpaRepository<Resort, Long> {
    @Query("select case when count(u)>0 then true else false end from Resort r join r.likedUser u where r.id = :resortId and u.id = :userId")
    boolean isLikedByUser(@Param("resortId") Long resortId, @Param("userId") Long userId);

    List<Resort> findAllByNameLike(String name);
}
