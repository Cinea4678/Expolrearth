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

}
