package cc.cinea.huanyou.repository;

import cc.cinea.huanyou.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

    @Query("select case when f.owner.id = :userId then true else false end from Favorites f where f.id = :id")
    boolean isOwnedBy(@Param("id") Long id, @Param("userId") Long userId);
}
