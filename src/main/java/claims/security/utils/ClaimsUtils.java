package claims.security.utils;

import claims.security.entities.CoreRole;
import claims.security.entities.CoreUserPreference;
import claims.security.entities.CoreUserProfile;
import claims.security.services.DBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ClaimsUtils {
    @Autowired
    DBUtils dbUtils;


    public String getUserDisplayName(String username) {
        Optional<CoreUserPreference> userPreferences = this.dbUtils.coreUserPreferenceRepository.findByCoreUser(username);
        if (userPreferences.isPresent()) {
            CoreUserPreference userPref = userPreferences.get();
            return userPref.getDisplayName();
        }
        else{
            if(username==null||username.isEmpty()){
                return "Anonymous";

            }
            else{
                return username;
            }

        }
    }

    public Date convertDateFormat(String originalDate, String originalFormat, String desiredFormat) {
        SimpleDateFormat originalFormatter = new SimpleDateFormat(originalFormat);
        SimpleDateFormat desiredFormatter = new SimpleDateFormat(desiredFormat);

        try {
            Date date = originalFormatter.parse(originalDate);
            String formattedDateString = desiredFormatter.format(date);
            return desiredFormatter.parse(formattedDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null; // Return null if an error occurs
    }

    public static Map<String, Set<CoreRole>> getRolesForInvolvedProfiles(List<CoreUserProfile> coreUser_companyProfiles) {
        Map<String, Set<CoreRole>> userRolesPerProfile = new HashMap<>();

        for(CoreUserProfile profile: coreUser_companyProfiles) {
            userRolesPerProfile.put(profile.getCoreCompanyProfileId(), profile.getUserRoles());
            System.out.println("@@@_" + profile.getCoreCompanyProfileId());
        }
        return userRolesPerProfile;
    }
}