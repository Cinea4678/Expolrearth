package cc.cinea.huanyou.service;

import cc.cinea.huanyou.entity.Administrator;

import java.util.Optional;

/**
 * @author cinea
 */
public interface AdministratorService {
    Optional<Administrator> getAdministratorServiceInfoById(String id);
}
