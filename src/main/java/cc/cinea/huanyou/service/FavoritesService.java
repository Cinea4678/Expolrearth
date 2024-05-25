package cc.cinea.huanyou.service;

import cc.cinea.huanyou.entity.Favorites;
import com.spencerwi.either.Either;

import java.util.List;

/**
 * @author LevisT
 */
public interface FavoritesService {
    Either<Void, String> createFavorites(String name, Long userId);

    Either<Void, String> setFavoritesName(Long favoritesId, String name, Long userId);

    Either<List<Favorites>, String> getFavoritesByUserId(Long userId);

    Either<List<Favorites>, String> getFollowingFavoritesByUserId(Long userId);

    Either<Favorites, String> getFavoritesByFavoritesId(Long favoritesId);

    Either<Void, String> followFavorites(Long favoritesId, Long userId);

    Either<Void, String> stopFollowFavorites(Long favoritesId, Long userId);
}
