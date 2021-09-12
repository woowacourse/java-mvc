package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nextstep.web.annotation.RequestParam;

public class ParameterResolverExecutor {

    private final Set<ParameterResolver> resolvers = new HashSet<>();

    {
        List<ParameterResolver> defaultParameterResolvers = List.of(
            new RequestAndResponseParameterResolver(),
            new SessionResolver(),
            new RequestParameterResolver()
        );
        this.resolvers.addAll(defaultParameterResolvers);
    }

    public ParameterResolverExecutor(
        List<ParameterResolver> resolvers) {
        this.resolvers.addAll(resolvers);
    }

    public Object[] captureProperParameter(Method method, HttpServletRequest request,
                                           HttpServletResponse response) {
        return Arrays.stream(method.getParameters())
            .map(parameter -> extractParameters(parameter, request, response))
            .toArray();
    }

    private Object extractParameters(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
        for (ParameterResolver resolver : resolvers) {
            if (resolver.isSupport(parameter)) {
                return resolver.resolveParameter(parameter, request, response);
            }
        }

        throw new IllegalArgumentException("바인딩할 수 없는 파라미터입니다.");
    }

    private static class RequestAndResponseParameterResolver implements ParameterResolver {

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

    private static class SessionResolver implements ParameterResolver {

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

    private static class RequestParameterResolver implements ParameterResolver {

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
}
