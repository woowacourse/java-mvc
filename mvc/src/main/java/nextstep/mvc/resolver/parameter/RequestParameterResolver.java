package nextstep.mvc.resolver.parameter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import nextstep.web.annotation.RequestParam;

public class RequestParameterResolver implements ParameterResolver {

    @Override
    public boolean isSupport(Parameter parameter) {
        return parameter.isAnnotationPresent(RequestParam.class);
    }

    @Override
    public Object resolveParameter(Parameter parameter, HttpServletRequest request,
                                   HttpServletResponse response) {
        String parameterName = parameter.getAnnotation(RequestParam.class).value();
        if (parameterName.isBlank()) {
            parameterName = parameter.getName();
        }
        return request.getAttribute(parameterName);
    }
}