package claims.security.repositories;

import claims.security.entities.AppAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AppAuthority, Integer> {
}
