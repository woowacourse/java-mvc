package nextstep.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.argument.ArgumentResolverContainer;
import nextstep.mvc.controller.MethodParameter;
import nextstep.mvc.controller.tobe.MethodHandler;
import nextstep.mvc.returntype.ReturnTypeResolverContainer;
import nextstep.mvc.view.ModelAndView;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

    private final ArgumentResolverContainer argumentResolverContainer;
    private final ReturnTypeResolverContainer returnTypeResolverContainer;

    public RequestMappingHandlerAdapter() {
        this.argumentResolverContainer = new ArgumentResolverContainer();
        this.returnTypeResolverContainer = new ReturnTypeResolverContainer();
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof MethodHandler;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                               Object handler) throws Exception {
        final MethodHandler methodHandler = (MethodHandler) handler;
        final Object returnValue = methodHandler.invoke(getMethodParameters(methodHandler, request, response));
        return resolveReturnValue(methodHandler, returnValue);
    }

    private Object[] getMethodParameters(MethodHandler methodHandler, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        final Object[] parameters = new Object[methodHandler.getMethodParameterSize()];
        for (MethodParameter methodParameter : methodHandler.getMethodParameters()) {
            final Object value = argumentResolverContainer.resolve(methodParameter, httpRequest, httpResponse);
            parameters[methodParameter.getParameterOrder()] = value;
        }
        return parameters;
    }

    private ModelAndView resolveReturnValue(MethodHandler methodHandler, Object returnValue) {
        return returnTypeResolverContainer.resolve(methodHandler, returnValue);
    }
}
