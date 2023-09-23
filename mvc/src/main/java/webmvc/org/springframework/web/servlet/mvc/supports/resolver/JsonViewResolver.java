package webmvc.org.springframework.web.servlet.mvc.supports.resolver;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.View;
import webmvc.org.springframework.web.servlet.mvc.supports.ViewResolver;
import webmvc.org.springframework.web.servlet.mvc.view.JsonView;

public class JsonViewResolver implements ViewResolver {

    private static final String ACCEPT_HEADER_KET = "Accept";
    private static final String APPLICATION_JSON_VALUE = "application/json";
    private static final String DEFAULT_ACCEPT_VALUE = "*/*";

    @Override
    public boolean supports(final HttpServletRequest request, final String ignored) {
        final String acceptHeaderValue = request.getHeader(ACCEPT_HEADER_KET);

        if (acceptHeaderValue == null) {
            return false;
        }

        return DEFAULT_ACCEPT_VALUE.equals(acceptHeaderValue) || acceptHeaderValue.contains(APPLICATION_JSON_VALUE);
    }

    @Override
    public View resolve(final String ignored) {
        return new JsonView();
    }
}
