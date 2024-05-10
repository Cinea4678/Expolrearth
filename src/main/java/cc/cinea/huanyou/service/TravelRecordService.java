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
}
