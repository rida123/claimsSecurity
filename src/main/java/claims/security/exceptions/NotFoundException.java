package claims.security.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {


    public NotFoundException(String message) {
        super("Not Found", message);
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
