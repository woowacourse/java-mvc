package nextstep.mvc.resolver.parameter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public class RequestAndResponseParameterResolver implements ParameterResolver {

    @Override
    public boolean isSupport(Parameter parameter) {
        Class<?> type = parameter.getType();
        return isRequestType(type) || isResponseType(type);
    }

    @Override
    public Object resolveParameter(Parameter parameter,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        Class<?> type = parameter.getType();
        return isRequestType(type) ? request : response;
    }

    private boolean isRequestType(Class<?> type) {
        return type.equals(HttpServletRequest.class);
    }

    private boolean isResponseType(Class<?> type) {
        return type.equals(HttpServletResponse.class);
    }
}