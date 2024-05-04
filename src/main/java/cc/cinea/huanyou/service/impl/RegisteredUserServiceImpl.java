package cc.cinea.huanyou.service.impl;

import cc.cinea.huanyou.entity.Favorites;
import cc.cinea.huanyou.entity.RegisteredUser;
import cc.cinea.huanyou.repository.FavoritesRepository;
import cc.cinea.huanyou.repository.RegistedUserRepository;
import cc.cinea.huanyou.service.RegisteredUserService;
import com.spencerwi.either.Either;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author cinea
 */
@Service
public class RegisteredUserServiceImpl implements RegisteredUserService {

    private final Pattern chinaPhoneNumber = Pattern.compile("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");
    RegistedUserRepository registedUserRepository;
    FavoritesRepository favoritesRepository;
    PasswordEncoder passwordEncoder;

    public RegisteredUserServiceImpl(RegistedUserRepository registedUserRepository, FavoritesRepository favoritesRepository, PasswordEncoder passwordEncoder) {
        this.registedUserRepository = registedUserRepository;
        this.favoritesRepository = favoritesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Either<RegisteredUser, String> getUserInfoById(Long id) {
        var userOptional = registedUserRepository.findById(id);
        return userOptional.<Either<RegisteredUser, String>>map((user) -> {
            user.setFavoritesList(null);
            user.setPasswordHash(null);
            return Either.left(user);
        }).orElseGet(() -> Either.right("未找到该用户"));
    }

    @Override
    @Transactional
    public Either<RegisteredUser, String> register(RegisteredUser user) {
        if (user.getName() == null || user.getName().isBlank()) {
            return Either.right("未填必填项：用户昵称");
        }
        if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
            return Either.right("未填必填项：手机号");
        }
        if (user.getGender() == null || user.getGender().isBlank()) {
            return Either.right("未填必填项：性别");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            return Either.right("未填必填项：密码");
        }

        if (registedUserRepository.getByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            return Either.right("手机号已被使用，请登录或更换");
        }
        if (!"男".equals(user.getGender()) && !"女".equals(user.getGender())) {
            return Either.right("性别不合法");
        }

        user.setId(null);
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setFollowing(0L);
        user.setFans(0L);
        user.setBanned(false);
        user.setFavoritesList(new ArrayList<>());

        var newUser = registedUserRepository.save(user);

        var favorites = new Favorites();
        favorites.setName("默认收藏夹");
        favorites.setOwner(newUser);
        favorites.setCreationTime(LocalDateTime.now());

        var newFavorites = favoritesRepository.save(favorites);

        newUser.getFavoritesList().add(newFavorites);
        registedUserRepository.save(newUser);

        return Either.left(newUser);
    }

    @Override
    public Either<Void, String> checkPhoneNumberValidate(String phoneNumber) {
        if (!chinaPhoneNumber.matcher(phoneNumber).matches()) {
            return Either.right("手机号码格式不合法");
        }
        if (registedUserRepository.existsByPhoneNumber(phoneNumber)) {
            return Either.right("手机号已被使用，请登录或更换");
        } else {
            return Either.left(null);
        }
    }
}
