package cc.cinea.huanyou.service.impl;

import cc.cinea.huanyou.entity.Administrator;
import cc.cinea.huanyou.repository.AdministratorRepository;
import cc.cinea.huanyou.service.AdministratorService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author cinea
 */
@Service
public class AdministratorServiceImpl implements AdministratorService {

    AdministratorRepository administratorRepository;

    public AdministratorServiceImpl(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    @Override
    public Optional<Administrator> getAdministratorServiceInfoById(String id) {
        return administratorRepository.findById(id);
    }
}
