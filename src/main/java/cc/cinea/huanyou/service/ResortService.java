package cc.cinea.huanyou.service;

import cc.cinea.huanyou.entity.Resort;
import com.spencerwi.either.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author cinea
 */
public interface ResortService {
    Either<Void, String> add(Resort resort);

    Either<Void, String> delete(Long resortId);
    
}
