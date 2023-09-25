package webmvc.org.springframework.web.servlet.mvc.tobe.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ArgumentResolvers {

    private final List<Resolver> resolvers;

    public ArgumentResolvers(final List<Resolver> resolvers) {
        this.resolvers = resolvers;
    }

    public Object[] resolveArgument(
            final Method method,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final List<Object> resolvedArguments = new ArrayList<>();

        for (final Parameter parameter : method.getParameters()) {
            final Resolver resolver = resolvers.stream()
                    .filter(it -> it.isSupported(parameter))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 파라미터업니다."));

            resolvedArguments.add(resolver.resolveArgument(parameter, request, response));
        }

        return resolvedArguments.toArray();
    }
}
