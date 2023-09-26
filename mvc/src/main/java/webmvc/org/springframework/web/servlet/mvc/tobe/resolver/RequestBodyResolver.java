package webmvc.org.springframework.web.servlet.mvc.tobe.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import web.org.springframework.util.HttpRequestBodyConverter;
import web.org.springframework.util.HttpRequestBodyParser;
import web.org.springframework.web.bind.annotation.RequestBody;

public class RequestBodyResolver implements Resolver {

    @Override
    public Object resolveArgument(
            final Parameter parameter,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final String json = HttpRequestBodyParser.parse(request);
        return HttpRequestBodyConverter.serialize(json, parameter.getType());
    }

    @Override
    public boolean isSupported(final Parameter parameter) {
        return parameter.isAnnotationPresent(RequestBody.class);
    }
}
