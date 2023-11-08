package claims.security.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public abstract class BaseException extends RuntimeException{

    private String title;
    private String message;

    public abstract HttpStatus getHttpStatusCode();
}
