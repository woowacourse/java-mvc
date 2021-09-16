package nextstep.mvc.resolver.parameter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Parameter;

public class SessionResolver implements ParameterResolver {

    @Override
    public boolean isSupport(Parameter parameter) {
        Class<?> type = parameter.getType();
        return type.equals(HttpSession.class);
    }

    @Override
    public Object resolveParameter(Parameter parameter, HttpServletRequest request,
                                   HttpServletResponse response) {
        return request.getSession();
    }
}