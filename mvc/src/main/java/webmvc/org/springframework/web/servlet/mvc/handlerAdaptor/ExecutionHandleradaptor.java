package webmvc.org.springframework.web.servlet.mvc.handlerAdaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExecutionHandleradaptor implements Handleradaptor {

    @Override
    public boolean isSame(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public Object execute(final Object handler, final HttpServletRequest request,
                          final HttpServletResponse response)
            throws Exception {
        final HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }
}
