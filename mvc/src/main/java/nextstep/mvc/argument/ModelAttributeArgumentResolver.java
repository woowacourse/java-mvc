package nextstep.mvc.argument;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.argument.annotation.ModelAttribute;
import nextstep.mvc.controller.MethodParameter;

public class ModelAttributeArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasAnnotationType(ModelAttribute.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        return parameter.createInstance(httpRequest.getParameterMap());
    }
}
