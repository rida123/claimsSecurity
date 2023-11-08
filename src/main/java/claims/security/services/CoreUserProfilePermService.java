package claims.security.services;

import claims.security.entities.CoreRole;
import claims.security.entities.CoreUserProfile;
import claims.security.entities.CoreUserProfilePerm;
import claims.security.http.response.ApiResponse;
import claims.security.http.response.StatusCode;
import claims.security.repositories.CoreUserProfileJpaRepository;
import claims.security.repositories.CoreUserProfilePermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoreUserProfilePermService extends BaseService<CoreUserProfilePerm> {

    @Autowired
    CoreUserProfilePermRepository coreUserProfilePermRepository;
    @Autowired
    CoreUserProfileJpaRepository userProfileRepository;

    public ApiResponse getRolesByUserProfile(String userProfileId) {

        Optional<CoreUserProfile> userProfileFound = userProfileRepository.findById(userProfileId);

        if(userProfileId == null || userProfileFound.isEmpty()) {
            return new ApiResponse(StatusCode.BAD_REQUEST.getCode(),
                    "error", "invalid parameter value for: userProfileId");
        }
        List<CoreUserProfilePerm> perms =  this.coreUserProfilePermRepository.findByCoreUserProfile(userProfileId);
        List<CoreRole> userRoles = new ArrayList<>();
        for (CoreUserProfilePerm p :
                perms) {
          CoreRole r = p.getCoreRole();
          r.setGranted(true);
          userRoles.add(r);
        }
        return new ApiResponse(StatusCode.OK.getCode(),  "User profile roles.", userRoles);
    }
}
