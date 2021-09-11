package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public interface ParameterResolver {
    boolean isSupport(Parameter parameter);

    Object resolveParameter(Parameter parameter, HttpServletRequest request, HttpServletResponse response);

}
