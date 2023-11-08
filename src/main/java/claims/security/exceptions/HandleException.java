package claims.security.exceptions;

import claims.security.http.response.ApiResponse;
import claims.security.http.response.StatusCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@Log4j2
public class HandleException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponse notFoundException(NotFoundException exception, WebRequest request){
        ErrorDetails details = new ErrorDetails(exception.getMessage(),exception.getTitle(),new Date());
        log.error(details.toString());
        return new ApiResponse(StatusCode.NOT_FOUND.getCode(), exception.getTitle(),  null);
    }

    //javax.persistence.EntityNotFoundException:
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponse entityNotFoundException(EntityNotFoundException exception, WebRequest request){
        ErrorDetails details = new ErrorDetails(exception.getMessage(),exception.getMessage() ,new Date());
        log.error(details.toString());
        return new ApiResponse(StatusCode.NOT_FOUND.getCode(), "Entity Not Found Exception",  null);
    }

    //javax.persistence.EntityNotFoundException:
    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponse invalidDataAccessApiUsageException(EntityNotFoundException exception, WebRequest request){
        ErrorDetails details = new ErrorDetails(exception.getMessage(),exception.getMessage() ,new Date());
        log.error(details.toString());
        return new ApiResponse(StatusCode.NOT_FOUND.getCode(), "invalid Data Access ApiUsageException",  null);
    }


    @ExceptionHandler(value = {UsernameNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        System.out.println("@Controller ADVICE:::: Username not found in the database ");
        String message = "Username not found";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, message, ex.getMessage());
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), errorResponse.getStatus(), request);
    }

    @Data
    private static class ErrorResponse {
        private HttpStatus status;
        private String message;
        private String error;

        public ErrorResponse(HttpStatus status, String message, String error) {
            this.status = status;
            this.message = message;
            this.error = error;
        }
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ApiResponse handleBadCredentialsException(BadCredentialsException ex) {
        System.out.println("we are @ bad credentials exception");

        ErrorDetails details = new ErrorDetails(ex.getMessage(),ex.getMessage() ,new Date());
        log.error(details.toString());
        return new ApiResponse(StatusCode.UNAUTHORIZED.getCode(), "Username or password is incorrect",  null);
    }

   @ExceptionHandler(value = {WrongPasswordException.class})
   public ResponseEntity<ApiResponse> handleWrongPasswordException(BadCredentialsException ex, WebRequest request) {
       System.out.println("@Controller ADVICE::::WrongPasswordException ");
       String message = "Incorrect username or password";
       ErrorDetails details = new ErrorDetails(ex.getMessage(),ex.getMessage() ,new Date());
       log.error(details.toString());
       ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, message, ex.getMessage());

       ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error",  null);
       return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//       return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), errorResponse.getStatus(), request);
   }


    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse handleAuthenticationException(AuthenticationException ex) {
        System.out.println("==> AuthenticationException occured");

        ErrorDetails details = new ErrorDetails(ex.getMessage(),ex.getMessage() ,new Date());
        log.error(details.toString());
        return new ApiResponse(StatusCode.UNAUTHORIZED.getCode(), "AuthenticationException", ex.getMessage());
    }

}
