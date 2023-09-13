package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.Objects;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class HandlerKey {

    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(final String url, final RequestMethod requestMethod) {
        validateUrl(url);
        validateRequestMethods(requestMethod);

        this.url = url;
        this.requestMethod = requestMethod;
    }

    private void validateUrl(final String url) {
        if (url == null || url.isEmpty() || url.isBlank()) {
            throw new IllegalArgumentException("유효하지 않은 URL 입니다.");
        }
    }

    private void validateRequestMethods(final RequestMethod requestMethod) {
        if (requestMethod == null) {
            throw new IllegalArgumentException("유효하지 않은 HTTP Method 입니다.");
        }
    }

    @Override
    public boolean equals(final Object target) {
        if (this == target) {
            return true;
        }
        if (target == null || getClass() != target.getClass()) {
            return false;
        }
        final HandlerKey targetHandlerKey = (HandlerKey) target;


        return Objects.equals(url, targetHandlerKey.url) && this.requestMethod == targetHandlerKey.requestMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethod);
    }
}
