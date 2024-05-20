package cc.cinea.huanyou.service;

import cc.cinea.huanyou.entity.RegisteredUser;
import com.spencerwi.either.Either;

import java.util.List;

public interface FollowFansService {
    Either<Void, String> follow(Long userId, Long operatorId);

    Either<Void, String> stopFollow(Long userId, Long operatorId);
}
