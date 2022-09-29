package nextstep.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.mapping.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import nextstep.mvc.view.resolver.ViewResolvers;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

    private final ViewResolvers viewResolvers;

    public RequestMappingHandlerAdapter() {
        this.viewResolvers = new ViewResolvers();
    }

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler) throws Exception {
        final var handlerExecution = (HandlerExecution) handler;
        final var handleResult = handlerExecution.handle(request, response);

        if (handleResult instanceof ModelAndView) {
            return (ModelAndView) handleResult;
        }

        final View view = this.viewResolvers.resolve(handleResult);
        return new ModelAndView(view)
                .addObject(String.valueOf(handleResult), handleResult);
    }
}
