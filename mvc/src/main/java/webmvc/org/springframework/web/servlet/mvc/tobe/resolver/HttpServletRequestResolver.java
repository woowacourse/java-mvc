package webmvc.org.springframework.web.servlet.mvc.tobe.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public class HttpServletRequestResolver implements Resolver {

    @Override
    public Object resolveArgument(
            final Parameter parameter,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        return request;
    }

    @Override
    public boolean isSupported(final Parameter parameter) {
        return parameter.getType().equals(HttpServletRequest.class);
    }
}
