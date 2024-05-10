package cc.cinea.huanyou.service.impl;

import cc.cinea.huanyou.entity.Administrator;
import cc.cinea.huanyou.repository.AdministratorRepository;
import cc.cinea.huanyou.repository.RegistedUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author cinea
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    AdministratorRepository administratorRepository;
    RegistedUserRepository registedUserRepository;

    public UserDetailsServiceImpl(AdministratorRepository administratorRepository, RegistedUserRepository registedUserRepository) {
        this.administratorRepository = administratorRepository;
        this.registedUserRepository = registedUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var registeredUserOptional = registedUserRepository.getByPhoneNumber(username);
        if (registeredUserOptional.isPresent()) {
            var registeredUser = registeredUserOptional.get();
            return User.withUsername(registeredUser.getId().toString())
                    .password(registeredUser.getPasswordHash())
                    .roles("USER")
                    .build();
        }

        Optional<Administrator> administrator = administratorRepository.findById(username);
        return administrator.map(value -> User.withUsername(username)
                .password(value.getPasswordHash())
                .roles("ADMIN")
                .build()).orElse(null);
    }
}
