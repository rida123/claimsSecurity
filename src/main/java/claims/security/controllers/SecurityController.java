package claims.security.controllers;


import claims.security.entities.*;
import claims.security.http.response.ApiResponse;
import claims.security.http.response.CoreUserProfileResponse;
import claims.security.http.response.JWTResponse;
import claims.security.http.response.StatusCode;
import claims.security.repositories.UserRepository;
import claims.security.security.managers.JwtAuthManager;
import claims.security.security.managers.SignInAuthenticationManager;
//import claims.security.security.model.CustomTokenAuthentication;
import claims.security.security.model.CustomTokenAuthentication;
import claims.security.security.model.LoginInRequest;
import claims.security.security.model.SecurityUser;
import claims.security.security.model.UsernamePasswordAuthentication;
import claims.security.security.services.JWTService;
import claims.security.security.services.RefreshTokenService;
import claims.security.services.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@Slf4j
public class SecurityController {

    @Autowired
    UserTraceService userTraceService;

    @Autowired
    CoreUserProfileService userProfileService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    CoreUserProfilePermService permService;

    @Autowired
    DBUtils dbUtils;

    @Autowired
    private JwtAuthManager jwtAuthManager;

    @Autowired
    private SignInAuthenticationManager signInAuthenticationManager;


    private final JWTService jwtService;

    @Autowired
    CoreUserService userService;


    private final UserRepository userRepository;

    @PostMapping("validate")
    public ResponseEntity<?> userIsValid(HttpServletResponse response, @RequestBody LoginInRequest signInRequest) {
        System.out.println("@Validate controller....");

        if(signInRequest == null || StringUtils.isEmpty(signInRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed. Invalid credentials.");
        }

        Authentication auth = signInAuthenticationManager
                .authenticate(new UsernamePasswordAuthentication(signInRequest.getUsername(), signInRequest.getPassword()));

        if(auth.isAuthenticated()) {
            System.out.println("authentication result: " + auth.isAuthenticated());
            System.out.println("@SecurityController(/validate endpoint) => coming user::" + auth.getPrincipal());
            SecurityUser currentUser = (SecurityUser) auth.getPrincipal();

            if (!currentUser.isEnabled()) {
                ApiResponse apiResponse =  new ApiResponse(StatusCode.LOCKED.getCode(), "Disabled", "Your account is deactivated , please contact your Administrator ");
                return ResponseEntity.ok(apiResponse);
            }
            String name = currentUser.getUsername();
            System.out.println("@SecurityController(/validate endpoint) =>: username ==> " + name);

            //@ this level the user is validated (successful login) =>
            //1- check if first login
//                true => redirect user to change password screen
//                  false => login normal to system home page

            boolean firstLogin = this.userService.isFirstLogin(currentUser.getCoreUser().getId());
            if (firstLogin == false) {
                // we insert record in user trace as successful login transaction
                CoreCompany userCompany = this.userService.getCompanyByCoreUser(name);
                UserTrace loginTransaction = new UserTrace();
                loginTransaction.setCoreUserId(name);
                loginTransaction.setCompanyId(Integer.valueOf(userCompany.getId()));
                loginTransaction.setComments("login");
                loginTransaction.setObjectCode("login");
                loginTransaction.setObjectId("1");
                loginTransaction.setSysCreatedDate(LocalDateTime.now());
                loginTransaction.setSysUpdatedDate(LocalDateTime.now());
                loginTransaction.setSysVersionNumber(1L);
                this.userTraceService.save(loginTransaction);
            }

            //needed if we want to specify authority names in the token
            List<String> loginUserAuthoritiesNames = new ArrayList<>();

            for (GrantedAuthority authority : currentUser.getAuthorities()) {
                loginUserAuthoritiesNames.add(authority.getAuthority());
            }


            //getting corecompany profiles:
            CoreUser coreUser = currentUser.getCoreUser();
            System.out.println("@ValidateUserController(/validate endpoint) => listing my profiles:");

            //needed if we want to specify  names of profiles a user enrolled in to be in the token
            List<CoreUserProfileResponse> enrolledProfilesNames = this.userProfileService.getEnrolledUserProfiles(coreUser);

            System.out.println("@ValidateUserController(/validate endpoint) => FINAL TESTING###");
            System.out.println("@ValidateUserController(/validate endpoint) ==>  for each company profile, list roles that user: " + coreUser.getId() + " has:");
            //TESTING DATA:::
       /*for (CoreProfile p: myProfiles) {
            System.out.println("PROFILE: " +p.getDescription() + ", user:  " + coreUser.getId() + " has the following roles:");
            System.out.println("----------");
            for (CoreRole r: p.getProfileRoles()) {
                System.out.println("     ROLE: " + r.getDescription());
            }
        }
        System.out.println("done final testing...");
        System.out.println("authoriteis for user: " + name);
        for(String authority : loginUserAuthoritiesNames) {
            System.out.println("authority: " + authority);
        }*/

            //generate token using configured jwtToken Service:
//            String token = this.jwtService.generateToken(name, loginUserAuthoritiesNames);
            String token = this.jwtService.generateTokenFromProfileNames(name, enrolledProfilesNames);

            log.warn("token is 1234");
            System.out.println("@ValidateUserController(/validate endpoint) => TOKEN VALUE:::" + token);
            //cookie:
      /*  Cookie cookie = new Cookie("token", token);
        cookie.setPath("/next2");
        cookie.setHttpOnly(true); //doing that => WE CANNOT REFERENCE THIS COOKIE IN JAVASCRIPT(IMPORTANT IN SECURITY)
        cookie.setMaxAge(1800);
        //TODO: When in production  must do cookie.setSecure(true)
        //TODO:  cookie.setSecure(true); //=>this cookie should only be sent over SSL(when we deploy our app)
        response.addCookie(cookie);*/
            //end cookie
            Map<String, String> results = new HashMap<>();
            results.put("results", "ok");


            RefreshToken refreshToken = refreshTokenService.createRefreshToken(name);
            Optional<CoreDomainValue> timeoutOptional = this.dbUtils.coreDomainValueRepository.findById("PASSWORD_CONFIGURATON.SESSI_TIM_OUT");
            String timeOUt = timeoutOptional.isPresent()? timeoutOptional.get().getVal1() : String.valueOf(0);

//        return enrolledProfilesNames;
//        return myProfiles;
//        return results;
            //     return "{\"result\": \"ok\", \"name\": \"ok\"}";
            Map<String, Object> afterLogin_data = new HashMap<>();
            afterLogin_data.put("token", token);
            afterLogin_data.put("refreshToken", refreshToken.getToken());
            afterLogin_data.put("timeout_value", timeOUt);

//            afterLogin_data.put("profiles", enrolledProfilesNames);
            afterLogin_data.put("firstLogin", Boolean.valueOf(firstLogin));
            return ResponseEntity.ok(new ApiResponse(StatusCode.OK.getCode(),  "profiles and token", afterLogin_data));

        }
        else{
            return ResponseEntity.ok(new ApiResponse(StatusCode.FORBIDDEN.getCode(), "incorrect user or  password", null));
        }
    }


    @GetMapping(value="/profile-token")
    public  ResponseEntity<?> getProfileRolesToken(@RequestParam(required = true) String profileId) {

        ApiResponse rolesPerProfile = this.permService.getRolesByUserProfile(profileId);
        List<CoreRole> userRoles =    (List<CoreRole>)rolesPerProfile.getData();


        List<String> loginUserAuthoritiesNames = new ArrayList<>();
        for (CoreRole role: userRoles) {
            loginUserAuthoritiesNames.add(role.getId());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        SecurityUser loginUser = (SecurityUser) authentication.getPrincipal();

        CoreUser coreUser = loginUser.getCoreUser();

        String jwtToken = jwtService.generateToken(coreUser.getId(), loginUserAuthoritiesNames);
//        return ResponseEntity.ok(jwtToken);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(coreUser.getId());
        Optional<CoreDomainValue> timeoutOptional = this.dbUtils.coreDomainValueRepository.findById("PASSWORD_CONFIGURATON.SESSI_TIM_OUT");
        String timeOUt = timeoutOptional.isPresent()? timeoutOptional.get().getVal1() : String.valueOf(0);

//        return enrolledProfilesNames;
//        return myProfiles;
//        return results;
        //     return "{\"result\": \"ok\", \"name\": \"ok\"}";
        Map<String, Object> afterProfileSelection = new HashMap<>();
        afterProfileSelection.put("token", jwtToken);
        afterProfileSelection.put("refreshToken", refreshToken.getToken());
        afterProfileSelection.put("timeout_value", timeOUt);

        return  ResponseEntity.ok(afterProfileSelection);

    }

    @PostMapping("/check-token")
    public HashMap<String, Object> checkToken(@RequestBody String token) {

        if (token == null || StringUtils.isEmpty(token)) {
            CustomTokenAuthentication result = new CustomTokenAuthentication(false, token, null, null);
//CustomTokenAuthentication authenticationResult = new CustomTokenAuthentication(username, password, isAuthenticated, authorities);
            HashMap<String, Object> authenticationResult = new HashMap<>();

            authenticationResult.put("valid", false);
            authenticationResult.put("username", null);
            authenticationResult.put("password", null);
            authenticationResult.put("isAuthenticated", false);
            authenticationResult.put("expired", true);
            authenticationResult.put("roles", null);
            return authenticationResult;
        }
        try {
            CustomTokenAuthentication authObject = new CustomTokenAuthentication(false, token, null, null);
            CustomTokenAuthentication authentication = jwtAuthManager.authenticate(authObject);

            //start
            if (authentication == null || authentication.isAuthenticated() == false) {
                HashMap<String, Object> authenticationResult = new HashMap<>();

                authenticationResult.put("valid", false);
                authenticationResult.put("username", null);
                authenticationResult.put("password", null);
                authenticationResult.put("isAuthenticated", false);
                authenticationResult.put("expired", true);
                authenticationResult.put("roles", null);
                return authenticationResult;
            }

            if (authentication.isAuthenticated()) {
                HashMap<String, Object> authenticationResult = new HashMap<>();

//                String userDetails = (String)authentication.getPrincipal();


                List<GrantedAuthority> authorityList = authentication.getAuthorityList();

                // Extract role names from the GrantedAuthority list
               List<String> roles = authorityList.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

                authenticationResult.put("valid", true);
                authenticationResult.put("username", authentication.getName());
                authenticationResult.put("password", authentication.getCredentials());
                authenticationResult.put("isAuthenticated", Boolean.TRUE);
                authenticationResult.put("expired", false);
                authenticationResult.put("roles", roles);
                return authenticationResult;
            }
        }

    /*        if(authentication.isAuthenticated())  {
                System.out.println("I am authenticateddddd");

                SecurityUser currentUser = (SecurityUser) authentication.getPrincipal();

                String name = currentUser.getUsername();
                System.out.println("@ValidateUserController(/validate endpoint) =>: username ==> " + name);

                //needed if we want to specify authority names in the token
                List<String> loginUserAuthoritiesNames = new ArrayList<>();

                for (GrantedAuthority authority : currentUser.getAuthorities()) {
                    loginUserAuthoritiesNames.add(authority.getAuthority());
                }
                for (String authority: loginUserAuthoritiesNames) {
                    System.out.println("Authority: " + authority);
                }

                // Generate and return a JWT token upon successful authentication
                // You can use a library like jjwt to create JWT tokens
                // ...
                Optional<AppUser> user_check = userRepository.findByUsername(loginRequest.getUsername());
                String jwtToken = jwtService.generateToken(user_check.get().getUsername(), loginUserAuthoritiesNames);
                JWTResponse jwtResponse = new JWTResponse(jwtToken, "Login successful. Here's your JWT token.");
                return ResponseEntity.ok(jwtResponse);
            }*/
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("@LoginController => Login failed. Invalid credentials.");


        catch (AuthenticationException e) {
            return null;
        }

        return null;
    }
}
