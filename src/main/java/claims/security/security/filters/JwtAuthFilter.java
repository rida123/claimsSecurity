package claims.security.security.filters;

import claims.security.http.response.ApiResponse;
import claims.security.security.config.SecurityProperties;
import claims.security.security.managers.JwtAuthManager;
import claims.security.security.model.CustomTokenAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter  {

    private  final JwtAuthManager authenticationManager;

    private final SecurityProperties securityProperties;

//    private  final ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
//        CustomAuthManager authenticationManager = new CustomAuthManager();
            String authHeader = request.getHeader("Authorization");
            final String jwt;
            if(authHeader == null || !authHeader.startsWith("Bearer ") || !securityProperties.isEnabled() ) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);//aba

            //the next step is to get an object of type UsernamePasswordAuthenticationToken
            //and that what we will use to store information about our user
            //{we want to store username + his roles in this authentication token}
            //after that we will put this authentication object in the SecurityContextHolder
            CustomTokenAuthentication authObject = new CustomTokenAuthentication(false, jwt, null, null);
            Authentication authenticationResult = authenticationManager.authenticate(authObject);
            if(authenticationResult == null || authenticationResult.isAuthenticated() == false) {
                //from
                ApiResponse responseData = new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "Wrong token", "token is not valid or expired");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(new ObjectMapper().writeValueAsString(responseData));
//                throw new TokenExpiredException("aaaaa");
                return;
                //to
                //throw new BadCredentialsException("Invalid JWT token");
            }
            if(authenticationResult.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authenticationResult);
                //carry on doing the processing...
                //lastly what we want to do is to propagate to the next filter in the filter chain
                filterChain.doFilter(request, response);//ONLY WHEN  THE AUTHENTICATION WORKS
            }
    }
}
