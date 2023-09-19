package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(final Object handleExecution) {
        return handleExecution instanceof HandlerExecution;
    }

    @Override
    public ModelAndView doInternalService(final HttpServletRequest request,
                                          final HttpServletResponse response,
                                          final Object method
    ) throws Exception {
        final HandlerExecution handlerExecution = (HandlerExecution) method;

        return handlerExecution.handle(request, response);
    }
}
