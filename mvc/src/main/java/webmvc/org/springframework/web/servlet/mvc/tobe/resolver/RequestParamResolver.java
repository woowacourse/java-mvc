package webmvc.org.springframework.web.servlet.mvc.tobe.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import web.org.springframework.web.bind.annotation.RequestParam;

public class RequestParamResolver implements Resolver {

    @Override
    public Object resolveArgument(
            final Parameter parameter,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final RequestParam annotation = parameter.getAnnotation(RequestParam.class);
        return request.getParameter(annotation.value());
    }

    @Override
    public boolean isSupported(final Parameter parameter) {
        return parameter.isAnnotationPresent(RequestParam.class);
    }
}
