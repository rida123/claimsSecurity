package claims.security.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException{

    public BadRequestException(String message) {
        super("Bad Request", message);
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
