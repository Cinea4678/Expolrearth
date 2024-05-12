package cc.cinea.huanyou.service;

import cc.cinea.huanyou.entity.TravelRecord;
import cc.cinea.huanyou.projection.TravelRecordPreviewProjection;
import com.spencerwi.either.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author cinea
 */
public interface TravelRecordService {
    Either<TravelRecord, String> publish(TravelRecord travelRecord, Long authorId);

    boolean delete(Long recordId, Long operatorId);

    TravelRecord getInfoById(Long recordId);

    TravelRecord getInfoWithInteractionById(Long recordId, Long userId);

    boolean update(TravelRecord travelRecord, Long operatorId);

    Either<Void, String> like(Long recordId, Long operatorId);

    Either<Void, String> editFavorites(Long recordId, Long favoritesId, Long operatorId, boolean isAdd);

    Either<Void, String> cancelLike(Long recordId, Long operatorId);

    Page<TravelRecordPreviewProjection> getTravelRecords(Pageable pageable, boolean isPreview);

    List<TravelRecordPreviewProjection> getTravelRecordsByAuthorId(Long authorId);
}
