package nextstep.mvc.argument;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.MethodParameter;

public class HttpResponseArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.isTypeOf(HttpServletResponse.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        return httpResponse;
    }
}
