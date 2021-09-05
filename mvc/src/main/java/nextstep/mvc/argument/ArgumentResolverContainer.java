package nextstep.mvc.argument;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.controller.MethodParameter;

public class ArgumentResolverContainer {

    private final List<HandlerMethodArgumentResolver> argumentResolvers;

    public ArgumentResolverContainer() {
        this.argumentResolvers = defaultArgumentResolvers();
    }

    private List<HandlerMethodArgumentResolver> defaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> argumentResolvers =
                new ArrayList<>();
        argumentResolvers.add(new HttpRequestArgumentResolver());
        argumentResolvers.add(new HttpResponseArgumentResolver());
        argumentResolvers.add(new ModelAttributeArgumentResolver());
        argumentResolvers.add(new RequestParamsArgumentResolver());
        argumentResolvers.add(new HttpCookieArgumentResolver());
        argumentResolvers.add(new HttpSessionArgumentResolver());
        return argumentResolvers;
    }

    public Object resolve(MethodParameter methodParameter, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        for (HandlerMethodArgumentResolver argumentResolver : argumentResolvers) {
            if(argumentResolver.supportsParameter(methodParameter)) {
                return argumentResolver.resolveArgument(methodParameter, httpRequest, httpResponse);
            }
        }
        throw new IllegalStateException("can not resolve method parameter");
    }
}
