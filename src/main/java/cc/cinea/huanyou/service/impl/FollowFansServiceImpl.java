package cc.cinea.huanyou.service.impl;

import cc.cinea.huanyou.entity.RegisteredUser;
import cc.cinea.huanyou.repository.RegistedUserRepository;
import cc.cinea.huanyou.service.FollowFansService;
import com.spencerwi.either.Either;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FollowFansServiceImpl implements FollowFansService {
    RegistedUserRepository registedUserRepository;

    public FollowFansServiceImpl(RegistedUserRepository registedUserRepository) {
        this.registedUserRepository = registedUserRepository;
    }

    @Override
    @Transactional
    public Either<Void, String> follow(Long userId, Long operatorId) {
        var followerOption = registedUserRepository.findById(operatorId);
        var followeeOption = registedUserRepository.findById(userId);

        if (followerOption.isEmpty() || followeeOption.isEmpty()) {
            return Either.right("正在关注的用户不存在");
        }

        var follower = followerOption.get();
        var followee = followeeOption.get();

        if (follower.getFollowingRegisteredUser().contains(followee)) {
            return Either.right("您已经关注该用户");
        }

        follower.getFollowingRegisteredUser().add(followee);
        follower.setFollowing(follower.getFollowing() + 1);
        followee.getFansList().add(follower);
        followee.setFans(followee.getFans() + 1);

        registedUserRepository.save(follower);
        registedUserRepository.save(followee);

        return Either.left(null);
    }

    @Override
    public Either<Void, String> stopFollow(Long userId, Long operatorId) {
        var followerOption = registedUserRepository.findById(operatorId);
        var followeeOption = registedUserRepository.findById(userId);

        if (followerOption.isEmpty() || followeeOption.isEmpty()) {
            return Either.right("正在取消关注的用户不存在");
        }

        var follower = followerOption.get();
        var followee = followeeOption.get();

        if (!follower.getFollowingRegisteredUser().contains(followee)) {
            return Either.right("您未关注该用户");
        }

        follower.getFollowingRegisteredUser().remove(followee);
        follower.setFollowing(follower.getFollowing() - 1);
        followee.getFansList().remove(follower);
        followee.setFans(followee.getFans() - 1);

        return Either.left(null);
    }
}
