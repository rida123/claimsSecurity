package claims.security.http.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse {

    private int statusCode;

    private String title;

    private Object data;

    public ApiResponse(int statusCode, String title, Object data) {
        super();
        this.statusCode = statusCode;
        this.title = title;
        this.data = data;
    }



}