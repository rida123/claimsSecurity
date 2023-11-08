package claims.security.exceptions;

import org.springframework.http.HttpStatus;

public class WrongPasswordException  extends BaseException {

    public WrongPasswordException(String message) {

        super("Wrong Credentials", message);
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return HttpStatus.UNAUTHORIZED;
    }
}
