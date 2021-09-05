package nextstep.mvc.argument;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.controller.MethodParameter;

public class HttpSessionArgumentResolver implements HandlerMethodArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.isTypeOf(HttpSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        return httpRequest.getSession();
    }
}
