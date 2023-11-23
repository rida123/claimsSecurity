package claims.security.controllers;

import claims.security.entities.CoreCompany;
import claims.security.entities.UserTrace;
import claims.security.exceptions.TokenRefreshException;
import claims.security.http.response.CoreUserProfileResponse;
import claims.security.http.response.MessageResponse;
import claims.security.http.response.TokenRefreshResponse;
import claims.security.security.model.TokenRefreshRequest;
import claims.security.security.services.JWTService;
import claims.security.security.services.RefreshTokenService;
import claims.security.services.CoreUserProfileService;
import claims.security.services.CoreUserService;
import claims.security.services.DBUtils;
import claims.security.services.UserTraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("api/refresh")
@RestController
public class RefreshTokenController extends BaseController {

    @Autowired
    CoreUserProfileService userProfileService;

    @Autowired
    CoreUserService userService;

    @Autowired
    UserTraceService userTraceService;

    @Autowired
    RefreshTokenService refreshTokenService;


    @Autowired
    DBUtils db;
    @Autowired
    JWTService jwtService;
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    refreshToken = refreshTokenService.createRefreshToken(refreshToken.getUser().getId());
                    List<CoreUserProfileResponse> enrolledProfilesNames = this.userProfileService.getEnrolledUserProfiles(refreshToken.getUser());
//                    String token = this.jwtService.generateTokenFromProfileNames(refreshToken.getUser().getId(), enrolledProfilesNames);
                    String token = this.jwtService.generateTokenFromProfileNames(refreshToken.getUser().getId());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, refreshToken.getToken()));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in the database!"));
    }



        @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {

        //from
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("type of Authentication " +  authentication.getClass());
            System.out.println("Type===== " + authentication.getPrincipal().toString());
            UserDetails userDetails =(UserDetails) authentication.getPrincipal();

            //test

         //   CoreUser coreUser = coreUserService.getCoreUser(username);
            // to
       // String username = getCurrentUser().getUsername();
            // we insert record in user trace as successful login transaction
            CoreCompany userCompany = this.userService.getCompanyByCoreUser(userDetails.getUsername());
            UserTrace loginTransaction = new UserTrace();
            loginTransaction.setCoreUserId(userDetails.getUsername());
            loginTransaction.setCompanyId(Integer.valueOf(userCompany.getId()));
            loginTransaction.setComments("logout");
            loginTransaction.setObjectCode("logout");
            loginTransaction.setObjectId("1");
            loginTransaction.setSysCreatedDate(LocalDateTime.now());
            loginTransaction.setSysUpdatedDate(LocalDateTime.now());
            loginTransaction.setSysVersionNumber(1L);

        refreshTokenService.deleteByUserId(getCurrentUser().getUsername());
            this.userTraceService.save(loginTransaction);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }





}
