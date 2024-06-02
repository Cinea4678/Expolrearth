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
    
}
