package cc.cinea.huanyou.service.impl;

import cc.cinea.huanyou.entity.Appeal;
import cc.cinea.huanyou.enums.AppealState;
import cc.cinea.huanyou.repository.AppealRepository;
import cc.cinea.huanyou.repository.BanRepository;
import cc.cinea.huanyou.service.AppealService;
import com.spencerwi.either.Either;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author cinea
 */
@Service
public class AppealServiceImpl implements AppealService {
    AppealRepository appealRepository;
    BanRepository banRepository;

    public AppealServiceImpl(AppealRepository appealRepository, BanRepository banRepository) {
        this.appealRepository = appealRepository;
        this.banRepository = banRepository;
    }

    @Override
    public Either<Void, String> submitAppeal(Appeal appeal, Long operatorId) {
        var banOptional = banRepository.findById(appeal.getBan().getId());
        if (banOptional.isEmpty()) {
            return Either.right("对应的封禁不存在");
        }
        var ban = banOptional.get();

        if (!ban.getUserId().equals(operatorId)) {
            return Either.right("您不可申诉该封禁");
        }

        appeal.setAppealTime(LocalDateTime.now());
        appeal.setId(null);
        appeal.setState(AppealState.NOT_REVIEWED);

        appealRepository.save(appeal);

        return Either.left(null);
    }

    @Override
    public Either<Void, String> setAppealState(Long appealId, AppealState newState) {
        var appealOptional = appealRepository.findById(appealId);
        if (appealOptional.isEmpty()) {
            return Either.right("未找到该申诉");
        }
        var appeal = appealOptional.get();

        appeal.setState(newState);
        appealRepository.save(appeal);

        return Either.left(null);
    }

    @Override
    public Either<Void, String> withdrawAppeal(Long appealId, Long operatorId) {
        var appealOptional = appealRepository.findById(appealId);
        if (appealOptional.isEmpty()) {
            return Either.right("未找到该申诉");
        }
        var appeal = appealOptional.get();

        if (!appeal.getBan().getUserId().equals(operatorId)) {
            return Either.right("您不可撤回该申诉");
        }

        appealRepository.deleteById(appealId);
        return Either.left(null);
    }

    @Override
    public Either<Appeal, String> getAppealById(Long appealId) {
        var appealOptional = appealRepository.findById(appealId);
        return appealOptional.<Either<Appeal, String>>map(Either::left).orElseGet(() -> Either.right("未找到该申诉"));
    }

    @Override
    public Either<List<Appeal>, String> getAppealByUserId(Long userId) {
        return Either.left(appealRepository.findAllOfUserId(userId));
    }

    @Override
    public Either<List<Appeal>, String> getAllAppeals() {
        return Either.left(appealRepository.findAll());
    }

}
