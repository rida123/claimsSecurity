package claims.security.services;

import claims.security.entities.CoreCompanyProfile;
import claims.security.entities.CoreDocumentFile;
import claims.security.entities.CoreUser;
import claims.security.entities.CoreUserProfile;
import claims.security.http.response.CoreUserProfileResponse;
import claims.security.repositories.CoreUserProfileJpaRepository;
import claims.security.utils.ClaimsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CoreUserProfileService extends BaseService<CoreUserProfile> {

/*    @Autowired
    net.claims.express.next2.utils.Utility utility;*/
    @Autowired
CoreUserProfileJpaRepository coreUserProfileJpaRepository;
    @Autowired
    DBUtils db;

    @Autowired
    ClaimsUtils utility;


    public List<CoreUserProfile> getUserProfiles(String coreUserId) {
        return this.coreUserProfileJpaRepository.findCoreUserProfileByCoreUserId(coreUserId);
    }

    public List<CoreUserProfile> getUserProfilesWithRoles(String coreUserId) {
        return this.coreUserProfileJpaRepository.findCoreUserProfileByCoreUserIdWithAllData(coreUserId);
    }

    public String getCoreProfTfPermId(String role_id, String userProfile_id) {
        return this.coreUserProfileJpaRepository.getCoreProfTfPermId(role_id, userProfile_id);
    }

    public List<CoreUserProfileResponse> getEnrolledUserProfiles(CoreUser coreUser) {
        List<CoreCompanyProfile> myCompanyProfiles = coreUser.getProfiles();
        List<CoreUserProfileResponse> enrolledProfilesNames = new ArrayList<>();
        for (CoreCompanyProfile companyProfile : myCompanyProfiles) {
            System.out.println("@ValidateUserController(/validate endpoint) => @PROFILE => " + companyProfile.getId());

//            CoreProfile coreProfile = companyProfile.getCoreProfile();
            CoreUserProfileResponse coreProfileResponse = new CoreUserProfileResponse();
            coreProfileResponse.setName(companyProfile.getCoreProfile().getName());
            coreProfileResponse.setCode(companyProfile.getCoreProfile().getCode());
            coreProfileResponse.setDescription(companyProfile.getCoreProfile().getDescription());
            coreProfileResponse.setCompanyId(companyProfile.getCoreCompany().getId());
            coreProfileResponse.setUserCode(coreUser.getId());
            coreProfileResponse.setDisplayName(utility.getUserDisplayName(coreUser.getId()));

            String userProfileId = coreUser.getId() + "." + companyProfile.getId();
            coreProfileResponse.setId(userProfileId);
            Optional<CoreDocumentFile> coreDocumentFileOptional = db.coreDocumentFileRepository.findById(companyProfile.getId());
            coreDocumentFileOptional.ifPresent(coreDocumentFile -> coreProfileResponse.setLogo(coreDocumentFile.getContent()));
            if (coreDocumentFileOptional.isEmpty()) {
                Optional<CoreDocumentFile> coreDocumentFileOptionalCE = db.coreDocumentFileRepository.findById("1." + companyProfile.getCoreProfile().getId());
                coreDocumentFileOptionalCE.ifPresent(coreDocumentFile -> coreProfileResponse.setLogo(coreDocumentFile.getContent()));
                if (coreDocumentFileOptionalCE.isEmpty()) {
                    Optional<CoreDocumentFile> coreDocumentFileOptionalDefault = db.coreDocumentFileRepository.findById("1.default");
                    coreDocumentFileOptionalDefault.ifPresent(coreDocumentFile -> coreProfileResponse.setLogo(coreDocumentFile.getContent()));

                }
            }

            enrolledProfilesNames.add(coreProfileResponse);
            Collections.sort(enrolledProfilesNames,  new Comparator<CoreUserProfileResponse>() {
                @Override
                public int compare(CoreUserProfileResponse s1, CoreUserProfileResponse s2) {
                    return s1.getDescription().compareTo(s2.getDescription());
                }
            });
        }
        return enrolledProfilesNames;
    }
}
