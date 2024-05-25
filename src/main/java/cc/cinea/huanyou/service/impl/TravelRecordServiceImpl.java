package cc.cinea.huanyou.service.impl;

import cc.cinea.huanyou.entity.RegisteredUser;
import cc.cinea.huanyou.entity.TravelRecord;
import cc.cinea.huanyou.projection.TravelRecordPreviewProjection;
import cc.cinea.huanyou.repository.FavoritesRepository;
import cc.cinea.huanyou.repository.RegistedUserRepository;
import cc.cinea.huanyou.repository.TravelRecordRepository;
import cc.cinea.huanyou.service.TravelRecordService;
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
public class TravelRecordServiceImpl implements TravelRecordService {

    TravelRecordRepository travelRecordRepository;
    RegistedUserRepository registedUserRepository;
    FavoritesRepository favoritesRepository;

    public TravelRecordServiceImpl(TravelRecordRepository travelRecordRepository, RegistedUserRepository registedUserRepository, FavoritesRepository favoritesRepository) {
        this.travelRecordRepository = travelRecordRepository;
        this.registedUserRepository = registedUserRepository;
        this.favoritesRepository = favoritesRepository;
    }

    @Override
    public Either<TravelRecord, String> publish(TravelRecord travelRecord, Long authorId) {
        var authorOptional = registedUserRepository.findById(authorId);
        if (authorOptional.isEmpty()) {
            return Either.right("用户不存在");
        }
        var author = authorOptional.get();

        if (author.isBanned()) {
            return Either.right("用户被封禁，无法发布");
        }

        // 去除不应该由客户端插入的数据
        travelRecord.setId(null);

        // 插入初始数据
        travelRecord.setComments(new ArrayList<>());
        travelRecord.setAuthor(author);
        travelRecord.setFavorites(0L);
        travelRecord.setLikes(0L);
        travelRecord.setPublishTime(LocalDateTime.now());

        return Either.left(travelRecordRepository.save(travelRecord));
    }

    @Override
    public boolean delete(Long recordId, Long operatorId) {
        var author = travelRecordRepository.findAuthorById(recordId);

        if (!operatorId.equals(author.getAuthor().getId())) {
            return false;
        }

        travelRecordRepository.deleteById(recordId);
        return true;
    }

    @Override
    public TravelRecord getInfoById(Long recordId) {
        return travelRecordRepository.findById(recordId).orElse(null);
    }

    @Override
    public TravelRecord getInfoWithInteractionById(Long recordId, Long userId) {
        var recordOptional = travelRecordRepository.findById(recordId);
        if (recordOptional.isEmpty()) {
            return null;
        }
        var record = recordOptional.get();

        record.setLikedUser(new ArrayList<>());
        if (travelRecordRepository.isLikedByUser(recordId, userId)) {
            var user = new RegisteredUser();
            user.setId(userId);
            record.getLikedUser().add(user);
        }

        return record;
    }

    @Override
    public boolean update(TravelRecord travelRecord, Long operatorId) {
        var oldRecord = travelRecordRepository.findById(travelRecord.getId()).orElse(null);

        if (oldRecord == null) {
            return false;
        }

        if (oldRecord.getAuthor().getId().equals(operatorId)) {
            return false;
        }

        oldRecord.setContent(travelRecord.getContent());
        oldRecord.setTitle(travelRecord.getTitle());
        oldRecord.setImages(travelRecord.getImages());

        travelRecordRepository.save(oldRecord);
        return true;
    }

    @Override
    @Transactional
    public Either<Void, String> like(Long recordId, Long operatorId) {
        var record = travelRecordRepository.findById(recordId).orElse(null);
        if (record == null) {
            return Either.right("旅行记录不存在");
        }

        if (travelRecordRepository.isLikedByUser(recordId, operatorId)) {
            return Either.right("您已经为这篇记录点过赞了");
        }

        var user = new RegisteredUser();
        user.setId(operatorId);
        record.getLikedUser().add(user);
        record.setLikes(record.getLikes() + 1);

        travelRecordRepository.save(record);
        return Either.left(null);
    }

    @Override
    @Transactional
    public Either<Void, String> editFavorites(Long recordId, Long favoritesId, Long operatorId, boolean isAdd) {
        var record = travelRecordRepository.findById(recordId).orElse(null);
        if (record == null) {
            return Either.right("旅行记录不存在");
        }

        var favorites = favoritesRepository.findById(favoritesId).orElse(null);
        if (favorites == null) {
            return Either.right("收藏夹不存在");
        }

        if (!favoritesRepository.isOwnedBy(favoritesId, operatorId)) {
            return Either.right("您不具有收藏夹编辑权限");
        }

        boolean presence = favorites.getItemRecords().contains(record);

        if (presence && isAdd) {
            return Either.right("已经收藏过了");
        } else if (!presence && !isAdd) {
            return Either.right("您未收藏该记录");
        }

        if (isAdd) {
            favorites.getItemRecords().add(record);
            record.setFavorites(record.getFavorites() + 1);
        } else {
            favorites.getItemRecords().remove(record);
            record.setFavorites(record.getFavorites() - 1);
        }
        favoritesRepository.save(favorites);
        travelRecordRepository.save(record);

        return Either.left(null);
    }

    @Override
    @Transactional
    public Either<Void, String> cancelLike(Long recordId, Long operatorId) {
        var record = travelRecordRepository.findById(recordId).orElse(null);
        if (record == null) {
            return Either.right("旅行记录不存在");
        }

        if (!travelRecordRepository.isLikedByUser(recordId, operatorId)) {
            return Either.right("您还没有为这篇记录点过赞");
        }

        var user = registedUserRepository.findById(operatorId).orElse(null);

        record.getLikedUser().remove(user);
        record.setLikes(record.getLikes() - 1);

        travelRecordRepository.save(record);
        return Either.left(null);
    }

    @Override
    public Page<TravelRecordPreviewProjection> getTravelRecords(Pageable pageable, boolean isPreview) {
        return travelRecordRepository.findAllBy(pageable);
    }

    @Override
    public List<TravelRecordPreviewProjection> getTravelRecordsByAuthorId(Long authorId) {
        var author = new RegisteredUser();
        author.setId(authorId);
        return travelRecordRepository.findAllByAuthorOrderByPublishTimeDesc(author);
    }
}
