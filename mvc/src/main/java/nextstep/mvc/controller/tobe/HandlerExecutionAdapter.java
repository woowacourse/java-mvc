package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecutionAdapter implements HandlerAdapter {

    private final ParameterResolverExecutor parameterResolverExecutor;

    public HandlerExecutionAdapter(
        ParameterResolverExecutor parameterResolverExecutor) {
        this.parameterResolverExecutor = parameterResolverExecutor;
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                               Object handler) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        Object[] parameters = parameterResolverExecutor
            .captureProperParameter(handlerExecution.getMethod(), request, response);
        return handlerExecution.handle(parameters);
    }
}
