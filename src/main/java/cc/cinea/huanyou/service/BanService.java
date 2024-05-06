package cc.cinea.huanyou.service;

import cc.cinea.huanyou.entity.Ban;
import com.spencerwi.either.Either;

import java.util.List;

/**
 * @author LevisT
 */
public interface BanService {
    Either<Void, String> addBan(Ban ban, Long userId);

    Either<Void, String> editBan(Ban ban);

    Either<Void, String> deleteBan(Long banId);

    Either<Ban, String> getBanById(Long banId);

    Either<List<Ban>, String> getBanByUserId(Long userId);
}
