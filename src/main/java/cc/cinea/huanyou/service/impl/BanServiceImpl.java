package cc.cinea.huanyou.service.impl;

import cc.cinea.huanyou.entity.Ban;
import cc.cinea.huanyou.repository.BanRepository;
import cc.cinea.huanyou.repository.RegistedUserRepository;
import cc.cinea.huanyou.service.BanService;
import com.spencerwi.either.Either;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LevisT
 */
@Service
public class BanServiceImpl implements BanService{
    BanRepository banRepository;
    RegistedUserRepository registedUserRepository;

    public BanServiceImpl(BanRepository banRepository, RegistedUserRepository registedUserRepository) {
        this.banRepository = banRepository;
        this.registedUserRepository = registedUserRepository;
    }

    @Override
    @Transactional
    public Either<Void, String> addBan(Ban ban, Long userId) {
        var userOptional = registedUserRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return Either.right("用户不存在");
        }
        var user = userOptional.get();

        ban.setId(null);
        ban.setUserId(userId);
        var newBan = banRepository.save(ban);

        user.setBanned(true);
        user.getBanList().add(newBan);
        registedUserRepository.save(user);
        return Either.left(null);
    }

    @Override
    public Either<Void, String> editBan(Ban ban) {
        if (banRepository.existsById(ban.getId())) {
            banRepository.save(ban);
            return Either.left(null);
        }
        return Either.right("指定的封禁不存在");
    }

    @Override
    @Transactional
    public Either<Void, String> deleteBan(Long banId) {
        var banOptional = banRepository.findById(banId);
        if(banOptional.isEmpty()){
            return Either.right("指定的封禁不存在");
        }
        var ban = banOptional.get();

        var userOptional = registedUserRepository.findById(ban.getUserId());
        if (userOptional.isEmpty()){
            return Either.right("用户不存在");
        }
        var user = userOptional.get();

        user.getBanList().remove(ban);
        if(user.getBanList().isEmpty()){
            user.setBanned(false);
        }

        registedUserRepository.save(user);
        banRepository.delete(ban);
        return Either.left(null);
    }

    @Override
    public Either<Ban, String> getBanById(Long banId) {
        var banOptional = banRepository.findById(banId);
        return banOptional.<Either<Ban, String>>map(Either::left).orElseGet(() -> Either.right("指定的封禁不存在"));
    }

    @Override
    public Either<List<Ban>, String> getBanByUserId(Long userId) {
        var userOptional = registedUserRepository.findById(userId);
        return userOptional.<Either<List<Ban>, String>>map(registeredUser -> Either.left(registeredUser.getBanList())).orElseGet(() -> Either.right("指定的用户不存在"));
    }
}
