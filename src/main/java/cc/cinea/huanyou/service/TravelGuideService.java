package cc.cinea.huanyou.service;

import cc.cinea.huanyou.entity.TravelGuide;
import cc.cinea.huanyou.entity.TravelRecord;
import cc.cinea.huanyou.projection.TravelGuidePreviewProjection;
import com.spencerwi.either.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author cinea
 */
public interface TravelGuideService {
    Either<TravelGuide, String> publish(TravelGuide travelGuide, Long authorId);

    boolean delete(Long guideId, Long operatorId);

    TravelGuide getInfoById(Long guideId);

    TravelGuide getInfoWithInteractionById(Long recordId, Long userId);

    boolean update(TravelGuide travelGuide, Long operatorId);

    Either<Void, String> like(Long guideId, Long operatorId);

    Either<Void, String> editFavorites(Long guideId, Long favoritesId, Long operatorId, boolean isAdd);

    Either<Void, String> cancelLike(Long guideId, Long operatorId);

    Page<TravelGuidePreviewProjection> getTravelGuides(Pageable pageable, boolean isPreview);

    Page<TravelGuidePreviewProjection> getTravelGuidesByResortId(Long resortId, Pageable pageable);

    List<TravelGuidePreviewProjection> getTravelGuidesByAuthorId(Long authorId);
}
