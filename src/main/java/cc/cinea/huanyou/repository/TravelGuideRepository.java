package cc.cinea.huanyou.repository;

import cc.cinea.huanyou.entity.RegisteredUser;
import cc.cinea.huanyou.entity.Resort;
import cc.cinea.huanyou.entity.TravelGuide;
import cc.cinea.huanyou.projection.TravelGuideAuthorProjection;
import cc.cinea.huanyou.projection.TravelGuidePreviewProjection;
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
public interface TravelGuideRepository extends JpaRepository<TravelGuide, Long> {
    @Query("select author as author from TravelGuide where id =:id")
    TravelGuideAuthorProjection findAuthorById(@Param("id") Long id);

    @Query("select case when count(u)>0 then true else false end from TravelGuide g join g.likedUser u where g.id = :guideId and u.id = :userId")
    boolean isLikedByUser(@Param("guideId") Long guideId, @Param("userId") Long userId);
    Page<TravelGuidePreviewProjection> findAllBy(Pageable pageable);

    Page<TravelGuidePreviewProjection> findAllByResort(Resort resortId, Pageable pageable);

    List<TravelGuidePreviewProjection> findAllByAuthorOrderByPublishTimeDesc(RegisteredUser author);
}
