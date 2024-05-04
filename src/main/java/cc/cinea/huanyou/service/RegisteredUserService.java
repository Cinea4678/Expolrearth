package cc.cinea.huanyou.service;

import cc.cinea.huanyou.entity.RegisteredUser;
import com.spencerwi.either.Either;

/**
 * @author cinea
 */
public interface RegisteredUserService {
    Either<RegisteredUser, String> getUserInfoById(Long id);

    Either<RegisteredUser, String> register(RegisteredUser user);

    Either<Void, String> checkPhoneNumberValidate(String phoneNumber);
}
