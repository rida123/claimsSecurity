package claims.security.repositories;

import claims.security.entities.UserTrace;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserTraceRepository extends BaseRepository<UserTrace, String> {

    Optional<UserTrace> findFirstByCoreUserIdOrderBySysCreatedDateDesc(String coreUserId);

    @Query("SELECT e FROM UserTrace e WHERE e.coreUserId =:userId order by  e.sysCreatedDate desc")
    List<UserTrace> findUserTraceByCoreUserIdOrderBySysCreatedDateDesc(@Param("userId") String userId);

    default Optional<UserTrace> findSecondRecordOrderBySyscreatedDesc(@Param("userId") String userId) {
        List<UserTrace> records = findUserTraceByCoreUserIdOrderBySysCreatedDateDesc( userId);
        if (records.size() >= 2) {
            return Optional.of(records.get(1));
        } else {
            return Optional.empty();
        }
    }
    @Query("SELECT e FROM UserTrace e WHERE e.coreUserId =:userId and  e.sysCreatedDate >= :datetime order by  e.sysCreatedDate desc")
    List<UserTrace> findTop2UserTracesWithinSpecifiedMinutes(@Param("userId") String userId,
                                                 @Param("datetime") LocalDateTime datetime);

    @Query("SELECT COUNT(e) FROM UserTrace e WHERE e.coreUserId = :userId and e.objectCode='login' and (e.objectId = '1' OR e.objectId = null) AND e.sysCreatedDate > :pwd_updated_date")
    Long countSuccessfulLoginsAfterPwdChange(@Param("userId") String userId,
            @Param("pwd_updated_date") LocalDateTime pwd_updated_date);
}
