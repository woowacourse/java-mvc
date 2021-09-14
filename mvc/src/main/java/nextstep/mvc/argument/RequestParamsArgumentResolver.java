package nextstep.mvc.argument;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.argument.annotation.RequestParams;
import nextstep.mvc.controller.MethodParameter;

public class RequestParamsArgumentResolver implements HandlerMethodArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasAnnotationType(RequestParams.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        final RequestParams requestParams = (RequestParams) parameter.getAnnotationOf(RequestParams.class);
        return httpRequest.getAttribute(requestParams.name());
    }
}
