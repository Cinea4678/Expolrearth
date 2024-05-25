package cc.cinea.huanyou.service.impl;

import cc.cinea.huanyou.entity.RegisteredUser;
import cc.cinea.huanyou.entity.Resort;
import cc.cinea.huanyou.entity.TravelGuide;
import cc.cinea.huanyou.entity.TravelRecord;
import cc.cinea.huanyou.projection.TravelGuidePreviewProjection;
import cc.cinea.huanyou.repository.FavoritesRepository;
import cc.cinea.huanyou.repository.RegistedUserRepository;
import cc.cinea.huanyou.repository.TravelGuideRepository;
import cc.cinea.huanyou.service.TravelGuideService;
import com.spencerwi.either.Either;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author cinea
 */
@Service
public class TravelGuideServiceImpl implements TravelGuideService {

    TravelGuideRepository travelGuideRepository;
    RegistedUserRepository registedUserRepository;
    FavoritesRepository favoritesRepository;

    public TravelGuideServiceImpl(TravelGuideRepository travelGuideRepository, RegistedUserRepository registedUserRepository, FavoritesRepository favoritesRepository) {
        this.travelGuideRepository = travelGuideRepository;
        this.registedUserRepository = registedUserRepository;
        this.favoritesRepository = favoritesRepository;
    }

    public Either<TravelGuide, String> publish(TravelGuide travelGuide, Long authorId) {
        var authorOptional = registedUserRepository.findById(authorId);
        if (authorOptional.isEmpty()) {
            return Either.right("用户不存在");
        }
        var author = authorOptional.get();

        if (author.isBanned()) {
            return Either.right("用户被封禁，无法发布");
        }

        // 去除不应该由客户端插入的数据
        travelGuide.setId(null);

        // 插入初始数据
        travelGuide.setComments(new ArrayList<>());
        travelGuide.setAuthor(author);
        travelGuide.setFavorites(0L);
        travelGuide.setLikes(0L);
        travelGuide.setPublishTime(LocalDateTime.now());

        return Either.left(travelGuideRepository.save(travelGuide));
    }

    @Override
    public boolean delete(Long guideId, Long operatorId) {
        var author = travelGuideRepository.findAuthorById(guideId);

        if (!operatorId.equals(author.getAuthor().getId())) {
            return false;
        }

        travelGuideRepository.deleteById(guideId);
        return true;
    }

    @Override
    public TravelGuide getInfoById(Long guideId) {
        return travelGuideRepository.findById(guideId).orElse(null);
    }

    @Override
    public TravelGuide getInfoWithInteractionById(Long guideId, Long userId) {
        var guideOptional = travelGuideRepository.findById(guideId);
        if (guideOptional.isEmpty()) {
            return null;
        }
        var guide = guideOptional.get();

        guide.setLikedUser(new ArrayList<>());
        if (travelGuideRepository.isLikedByUser(guideId, userId)) {
            var user = new RegisteredUser();
            user.setId(userId);
            guide.getLikedUser().add(user);
        }

        return guide;
    }

    @Override
    public boolean update(TravelGuide travelGuide, Long operatorId) {
        var oldGuide = travelGuideRepository.findById(travelGuide.getId()).orElse(null);

        if (oldGuide == null) {
            return false;
        }

        if (oldGuide.getAuthor().getId().equals(operatorId)) {
            return false;
        }

        oldGuide.setContent(travelGuide.getContent());
        oldGuide.setTitle(travelGuide.getTitle());
        oldGuide.setImages(travelGuide.getImages());

        travelGuideRepository.save(oldGuide);
        return true;
    }

    @Override
    @Transactional
    public Either<Void, String> like(Long guideId, Long operatorId) {
        var guide = travelGuideRepository.findById(guideId).orElse(null);
        if (guide == null) {
            return Either.right("旅行攻略不存在");
        }

        if (travelGuideRepository.isLikedByUser(guideId, operatorId)) {
            return Either.right("您已经点过赞了");
        }

        var user = new RegisteredUser();
        user.setId(operatorId);
        guide.getLikedUser().add(user);
        guide.setLikes(guide.getLikes() + 1);

        travelGuideRepository.save(guide);
        return Either.left(null);
    }

    @Override
    public Either<Void, String> editFavorites(Long guideId, Long favoritesId, Long operatorId, boolean isAdd) {
        var guide = travelGuideRepository.findById(guideId).orElse(null);
        if (guide == null) {
            return Either.right("旅行攻略不存在");
        }

        var favorites = favoritesRepository.findById(favoritesId).orElse(null);
        if (favorites == null) {
            return Either.right("收藏夹不存在");
        }

        if (!favoritesRepository.isOwnedBy(favoritesId, operatorId)) {
            return Either.right("您不具有收藏夹编辑权限");
        }

        boolean presence = favorites.getItemGuides().contains(guide);

        if (presence && isAdd) {
            return Either.right("已经收藏过了");
        } else if (!presence && !isAdd) {
            return Either.right("您未收藏该攻略");
        }

        if (isAdd) {
            favorites.getItemGuides().add(guide);
            guide.setFavorites(guide.getFavorites() + 1);
        } else {
            favorites.getItemGuides().remove(guide);
            guide.setFavorites(guide.getFavorites() - 1);
        }
        favoritesRepository.save(favorites);
        travelGuideRepository.save(guide);

        return Either.left(null);
    }

    @Override
    public Either<Void, String> cancelLike(Long guideId, Long operatorId) {
        var guide = travelGuideRepository.findById(guideId).orElse(null);
        if (guide == null) {
            return Either.right("旅行攻略不存在");
        }

        if (!travelGuideRepository.isLikedByUser(guideId, operatorId)) {
            return Either.right("您还没有点过赞");
        }

        var user = registedUserRepository.findById(operatorId).orElse(null);

        guide.getLikedUser().remove(user);
        guide.setLikes(guide.getLikes() - 1);

        travelGuideRepository.save(guide);
        return Either.left(null);
    }

    @Override
    public Page<TravelGuidePreviewProjection> getTravelGuides(Pageable pageable, boolean isPreview) {
        return travelGuideRepository.findAllBy(pageable);
    }

    @Override
    public Page<TravelGuidePreviewProjection> getTravelGuidesByResortId(Long resortId, Pageable pageable) {
        var resort = new Resort();
        resort.setId(resortId);
        return travelGuideRepository.findAllByResort(resort, pageable);
    }

    @Override
    public List<TravelGuidePreviewProjection> getTravelGuidesByAuthorId(Long authorId) {
        var author = new RegisteredUser();
        author.setId(authorId);
        return travelGuideRepository.findAllByAuthorOrderByPublishTimeDesc(author);
    }
}
