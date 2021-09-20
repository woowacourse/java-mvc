package nextstep.mvc.resolver.parameter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ParameterResolverExecutor {

    private final Set<ParameterResolver> resolvers;

    public ParameterResolverExecutor(ParameterResolver... resolvers) {
        this.resolvers = new HashSet<>(Arrays.asList(resolvers));
    }

    public Object[] captureProperParameter(Method method, HttpServletRequest request,
                                           HttpServletResponse response) {
        return Arrays.stream(method.getParameters())
            .map(parameter -> extractParameters(parameter, request, response))
            .toArray();
    }

    private Object extractParameters(Parameter parameter, HttpServletRequest request,
                                     HttpServletResponse response) {
        for (ParameterResolver resolver : resolvers) {
            if (resolver.isSupport(parameter)) {
                return resolver.resolveParameter(parameter, request, response);
            }
        }

        throw new IllegalArgumentException("바인딩할 수 없는 파라미터입니다.");
    }

}
