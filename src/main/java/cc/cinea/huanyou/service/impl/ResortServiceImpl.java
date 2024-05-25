package cc.cinea.huanyou.service.impl;

import cc.cinea.huanyou.entity.RegisteredUser;
import cc.cinea.huanyou.entity.Resort;
import cc.cinea.huanyou.repository.RegistedUserRepository;
import cc.cinea.huanyou.repository.ResortRepository;
import cc.cinea.huanyou.service.ResortService;
import com.spencerwi.either.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cinea
 */
@Service
public class ResortServiceImpl implements ResortService {

    ResortRepository resortRepository;
    RegistedUserRepository registedUserRepository;

    public ResortServiceImpl(ResortRepository resortRepository, RegistedUserRepository registedUserRepository) {
        this.resortRepository = resortRepository;
        this.registedUserRepository = registedUserRepository;
    }

    @Override
    public Either<Void, String> add(Resort resort) {
        resort.setId(null);
        resort.setLikes(0L);

        resortRepository.save(resort);
        return Either.left(null);
    }

    @Override
    public Either<Void, String> delete(Long resortId) {
        resortRepository.deleteById(resortId);
        return Either.left(null);
    }

    @Override
    public Either<Void, String> updateInfo(Resort resort) {
        var oldResortOptional = resortRepository.findById(resort.getId());
        if (oldResortOptional.isEmpty()) {
            return Either.right("景区不存在");
        }

        var oldResort = oldResortOptional.get();
        oldResort.setAddress(resort.getAddress());
        oldResort.setName(resort.getName());
        oldResort.setImageList(resort.getImageList());
        oldResort.setDescription(resort.getDescription());
        oldResort.setSummary(resort.getSummary());

        resortRepository.save(oldResort);

        return Either.left(null);
    }

    @Override
    public Either<Void, String> like(Long resortId, Long operatorId) {
        var resortOptional = resortRepository.findById(resortId);
        if (resortOptional.isEmpty()) {
            return Either.right("景点不存在");
        }

        var resort = resortOptional.get();
        if (resortRepository.isLikedByUser(resortId, operatorId)) {
            return Either.right("您已经为景区点过赞了");
        }

        var user = new RegisteredUser();
        user.setId(operatorId);
        resort.getLikedUser().add(user);
        resort.setLikes(resort.getLikes() + 1);

        resortRepository.save(resort);
        return Either.left(null);
    }

    @Override
    public Either<Void, String> cancelLike(Long resortId, Long operatorId) {
        var resortOptional = resortRepository.findById(resortId);
        if (resortOptional.isEmpty()) {
            return Either.right("景点不存在");
        }

        var resort = resortOptional.get();
        if (!resortRepository.isLikedByUser(resortId, operatorId)) {
            return Either.right("您没有为这个景区点过赞");
        }

        var user = registedUserRepository.findById(operatorId).orElse(null);
        resort.getLikedUser().remove(user);
        resort.setLikes(resort.getLikes() - 1);

        resortRepository.save(resort);
        return Either.left(null);
    }

    @Override
    public Either<Resort, String> getInfoById(Long resortId) {
        return resortRepository.findById(resortId).map(Either::<Resort, String>left).orElse(Either.right("景区不存在"));
    }

    @Override
    public Page<Resort> getResorts(Pageable pageable) {
        return resortRepository.findAll(pageable);
    }

    @Override
    public List<Resort> searchByName(String name) {
        return resortRepository.findAllByNameLike(String.format("%%%s%%", name));
    }
    
}
