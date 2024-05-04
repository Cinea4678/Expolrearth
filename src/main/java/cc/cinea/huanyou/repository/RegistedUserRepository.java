package cc.cinea.huanyou.repository;

import cc.cinea.huanyou.entity.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;



public interface RegistedUserRepository extends JpaRepository<RegisteredUser, Long> {
    Optional<RegisteredUser> getByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);
}
