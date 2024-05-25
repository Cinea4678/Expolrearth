package cc.cinea.huanyou.service;

import cc.cinea.huanyou.entity.Appeal;
import cc.cinea.huanyou.enums.AppealState;
import com.spencerwi.either.Either;

import java.util.List;

/**
 * @author cinea
 */
public interface AppealService {
    Either<Void, String> submitAppeal(Appeal appeal, Long operatorId);

    Either<Void, String> setAppealState(Long appealId, AppealState newState);

    Either<Void, String> withdrawAppeal(Long appealId, Long operatorId);

    Either<Appeal, String> getAppealById(Long appealId);

    Either<List<Appeal>, String> getAppealByUserId(Long userId);

    Either<List<Appeal>, String> getAllAppeals();
}
