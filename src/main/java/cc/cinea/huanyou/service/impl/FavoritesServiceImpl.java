package cc.cinea.huanyou.service.impl;

import cc.cinea.huanyou.entity.Favorites;
import cc.cinea.huanyou.repository.FavoritesRepository;
import cc.cinea.huanyou.repository.RegistedUserRepository;
import cc.cinea.huanyou.service.FavoritesService;
import com.spencerwi.either.Either;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LevisT
 */
@Service
public class FavoritesServiceImpl implements FavoritesService {
    RegistedUserRepository registedUserRepository;
    FavoritesRepository favoritesRepository;

    public FavoritesServiceImpl(RegistedUserRepository registedUserRepository, FavoritesRepository favoritesRepository) {
        this.registedUserRepository = registedUserRepository;
        this.favoritesRepository = favoritesRepository;
    }

    @Override
    @Transactional
    public Either<Void, String> createFavorites(String name, Long userId) {
        var ownerOptional = registedUserRepository.findById(userId);
        if(ownerOptional.isEmpty()){
            return Either.right("用户不存在");
        }
        var owner = ownerOptional.get();

        var favorites = new Favorites();
        favorites.setName(name);
        favorites.setOwner(owner);
        favorites.setCreationTime(LocalDateTime.now());

        var newFavorites = favoritesRepository.save(favorites);

        owner.getFavoritesList().add(newFavorites);
        registedUserRepository.save(owner);

        return Either.left(null);
    }

    @Override
    public Either<Void, String> setFavoritesName(Long favoritesId, String name, Long userId) {
        var favoritesOptional = favoritesRepository.findById(favoritesId);
        if (favoritesOptional.isEmpty()){
            return Either.right("收藏夹不存在");
        }
        var favorites = favoritesOptional.get();

        if(!favorites.getOwner().getId().equals(userId)){
            return Either.right("您不能编辑这个收藏夹");
        }

        favorites.setName(name);
        favoritesRepository.save(favorites);
        return Either.left(null);
    }

    @Override
    public Either<List<Favorites>, String> getFavoritesByUserId(Long userId) {
        var userOptional = registedUserRepository.findById(userId);
        return userOptional.<Either<List<Favorites>, String>>map(registeredUser -> Either.left(registeredUser.getFavoritesList())).orElseGet(() -> Either.right("查询的用户不存在"));
    }

    @Override
    public Either<List<Favorites>, String> getFollowingFavoritesByUserId(Long userId) {
        var userOptional = registedUserRepository.findById(userId);
        return userOptional.<Either<List<Favorites>, String>>map(registeredUser -> Either.left(registeredUser.getFavoritesFollowingList())).orElseGet(() -> Either.right("查询的用户不存在"));
    }

    @Override
    public Either<Favorites, String> getFavoritesByFavoritesId(Long favoritesId) {
        var favoritesOptional = favoritesRepository.findById(favoritesId);
        return favoritesOptional.<Either<Favorites, String>>map(Either::left).orElseGet(() -> Either.right("查询的收藏夹不存在"));
    }

    @Override
    public Either<Void, String> followFavorites(Long favoritesId, Long userId) {
        var followerOptional = registedUserRepository.findById(userId);
        if (followerOptional.isEmpty()){
            return Either.right("指定用户不存在");
        }
        var follower = followerOptional.get();

        var followeeOptional = favoritesRepository.findById(favoritesId);
        if(followeeOptional.isEmpty()){
            return Either.right("被关注的收藏夹不存在");
        }
        var followee = followeeOptional.get();

        if(followee.getOwner().getId().equals(userId)){
            return Either.right("不能关注自己的收藏夹");
        }
        if(follower.getFavoritesFollowingList().contains(followee)){
            return Either.right("不能重复关注已关注的收藏夹");
        }

        follower.getFavoritesFollowingList().add(followee);
        registedUserRepository.save(follower);
        return Either.left(null);
    }

    @Override
    public Either<Void, String> stopFollowFavorites(Long favoritesId, Long userId) {
        var followerOptional = registedUserRepository.findById(userId);
        if (followerOptional.isEmpty()){
            return Either.right("用户不存在");
        }
        var follower = followerOptional.get();

        var followeeOptional = favoritesRepository.findById(favoritesId);
        if(followeeOptional.isEmpty()){
            return Either.right("被取消关注的收藏夹不存在");
        }
        var followee = followeeOptional.get();
        if(!follower.getFavoritesFollowingList().contains(followee)){
            return Either.right("您未关注该收藏夹");
        }

        follower.getFavoritesFollowingList().remove(followee);
        registedUserRepository.save(follower);
        return Either.left(null);
    }
}
