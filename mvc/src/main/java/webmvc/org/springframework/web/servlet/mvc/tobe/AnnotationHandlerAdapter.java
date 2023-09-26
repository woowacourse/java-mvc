package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.resolver.ArgumentResolvers;
import webmvc.org.springframework.web.servlet.mvc.tobe.resolver.HttpServletRequestResolver;
import webmvc.org.springframework.web.servlet.mvc.tobe.resolver.HttpServletResponseResolver;
import webmvc.org.springframework.web.servlet.mvc.tobe.resolver.HttpSessionResolver;
import webmvc.org.springframework.web.servlet.mvc.tobe.resolver.RequestBodyResolver;
import webmvc.org.springframework.web.servlet.mvc.tobe.resolver.RequestParamResolver;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    private static final ArgumentResolvers argumentResolvers = new ArgumentResolvers(
            List.of(new RequestBodyResolver(),
                    new RequestParamResolver(),
                    new HttpServletRequestResolver(),
                    new HttpServletResponseResolver(),
                    new HttpSessionResolver())
    );

    @Override
    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        Object[] arguments = argumentResolvers.resolveArgument(handlerExecution.getMethod(), request, response);
        return handlerExecution.handle(arguments);
    }

    @Override
    public boolean isSupported(final Object handler) {
        return handler instanceof HandlerExecution;
    }
}
