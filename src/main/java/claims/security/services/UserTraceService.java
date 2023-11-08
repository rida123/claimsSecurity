package claims.security.services;

import claims.security.entities.UserTrace;
import claims.security.repositories.UserTraceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserTraceService extends BaseService<UserTrace> {

    @Autowired
    UserTraceRepository userTraceRepository;

    public Optional<UserTrace> findLastLoginByCoreUser(String coreUserId) {
//        return this.userTraceRepository.findFirstByCoreUserIdOrderBySysCreatedDateDesc(coreUserId);
        return this.userTraceRepository.findSecondRecordOrderBySyscreatedDesc(coreUserId);

    }

    public int findFailedLoginAttemptsPerUserId(String userId, int period_in_minutes, int max_trials) {
        LocalDateTime currentTime_Minus_max_period = LocalDateTime.now().minusMinutes(period_in_minutes);
        List<UserTrace> lastTwoLoginAttempts = this.userTraceRepository.findTop2UserTracesWithinSpecifiedMinutes(userId, currentTime_Minus_max_period);

       int failedLoginCounter = 0;

       for(int i = 0; i <lastTwoLoginAttempts.size() && i<max_trials; i++) {
           UserTrace trace = lastTwoLoginAttempts.get(i);
           System.out.println("trace login: " + trace.getCoreUserId() + " ==> " + trace.getObjectId() );
           if(trace.getObjectId() != null && trace.getObjectId().equals("0")) {
               failedLoginCounter += 1;
           }
       }
        return failedLoginCounter;
    }
}
