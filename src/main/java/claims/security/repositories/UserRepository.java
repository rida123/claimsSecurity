package claims.security.repositories;

import claims.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Integer> {

    @Query("select e from AppUser e where e.username = ?1")
    Optional<AppUser> findByUsername(String username);
}
