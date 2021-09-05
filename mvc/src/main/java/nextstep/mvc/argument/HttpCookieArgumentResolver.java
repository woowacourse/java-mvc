package nextstep.mvc.argument;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.HttpCookie;
import nextstep.mvc.controller.MethodParameter;

public class HttpCookieArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.isTypeOf(HttpCookie.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        return httpRequest.getCookies();
    }
}
