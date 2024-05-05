package cc.cinea.huanyou.repository;

import cc.cinea.huanyou.entity.RegisteredUser;
import cc.cinea.huanyou.entity.TravelRecord;
import cc.cinea.huanyou.projection.TravelGuidePreviewProjection;
import cc.cinea.huanyou.projection.TravelRecordAuthorProjection;
import cc.cinea.huanyou.projection.TravelRecordPreviewProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author cinea
 */
public interface TravelRecordRepository extends JpaRepository<TravelRecord, Long> {
    @Query("select author as author from TravelRecord where id =:id")
    TravelRecordAuthorProjection findAuthorById(@Param("id") Long id);

    @Query("select case when count(u)>0 then true else false end from TravelRecord r join r.likedUser u where r.id = :recordId and u.id = :userId")
    boolean isLikedByUser(@Param("recordId") Long recordId, @Param("userId") Long userId);

    Page<TravelRecordPreviewProjection> findAllBy(Pageable pageable);

    List<TravelRecordPreviewProjection> findAllByAuthorOrderByPublishTimeDesc(RegisteredUser author);
}
