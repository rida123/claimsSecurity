package claims.security.http.response;

import org.springframework.beans.factory.annotation.Value;

public interface CoreUserPreferenceProjection {
    @Value("#{target.ID}")
    String getId();

    @Value("#{target.DISPLAY_NAME}")
    String getDisplayName();
}
