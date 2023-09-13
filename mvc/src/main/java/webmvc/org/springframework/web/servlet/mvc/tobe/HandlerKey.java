package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class HandlerKey {

    private final String url;
    private final List<RequestMethod> requestMethods;

    public HandlerKey(final String url, final RequestMethod... requestMethods) {
        validateUrl(url);
        validateRequestMethods(requestMethods);

        this.url = url;
        this.requestMethods = Arrays.asList(requestMethods);
    }

    private void validateUrl(final String url) {
        if (url == null || url.isEmpty() || url.isBlank()) {
            throw new IllegalArgumentException("유효하지 않은 URL 입니다.");
        }
    }

    private void validateRequestMethods(final RequestMethod[] requestMethods) {
        if (requestMethods == null || requestMethods.length <= 0) {
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


        return Objects.equals(url, targetHandlerKey.url) &&
                this.requestMethods.stream().anyMatch(targetHandlerKey.requestMethods::contains);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethods);
    }
}
