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

    
}
