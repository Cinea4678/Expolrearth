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

}
