package claims.security.repositories;

import claims.security.entities.CoreUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface CoreUserRepository extends BaseRepository<CoreUser, String> {

    @Query(value = " SELECT CONFIG_VALUE FROM CORE_CONFIGURATION WHERE ID = ?1 ", nativeQuery = true)
    String findConfigByKey(String key);

    @Modifying
    @Query("UPDATE CoreUser e SET e.activeFlag = 0 WHERE e.id = :username")
    void deActivateUser(@Param("username") String username);
}
