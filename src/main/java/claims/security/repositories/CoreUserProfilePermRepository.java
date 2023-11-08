package claims.security.repositories;

import claims.security.entities.CoreUserProfilePerm;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface CoreUserProfilePermRepository extends BaseRepository<CoreUserProfilePerm, String>{
    @Query(value = "select p from CoreUserProfilePerm  p join fetch  p.coreUserProfile up where up.id =?1 ")
    List<CoreUserProfilePerm> findByCoreUserProfile(String coreUserProfileId);

//    List<CoreUserProfilePerm> findCoreUserProfilePermByCoreUserProfile(String userProfileId);

    @Query(value = "select p from CoreUserProfilePerm  p where p.coreRole.id= ?1 ")
    List<CoreUserProfilePerm> getUserProfilesWithRole(String roleId);

}
