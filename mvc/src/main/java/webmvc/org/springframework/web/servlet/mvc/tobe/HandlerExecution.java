package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.List;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.view.resolver.ViewResolver;

public class HandlerExecution {

    private final List<ViewResolver> viewResolvers;
    private final Object handler;
    private final Method method;

    public HandlerExecution(final List<ViewResolver> viewResolvers, final Object handler, final Method method) {
        this.viewResolvers = viewResolvers;
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(handler, request, response);
    }

    private ModelAndView processModelAndView(final HttpServletRequest request, final View view) {
        final ModelAndView modelAndView = new ModelAndView(view);
        final HttpSession session = request.getSession(false);

        if (session == null) {
            return modelAndView;
        }

        final Enumeration<String> attributeNames = session.getAttributeNames();

        while (attributeNames.hasMoreElements()) {
            final String attributeName = attributeNames.nextElement();

            modelAndView.addObject(attributeName, session.getAttribute(attributeName));
        }

        return modelAndView;
    }

    private ViewResolver findViewResolver(final String viewName) {
        for (final ViewResolver viewResolver : viewResolvers) {
            if (viewResolver.supports(viewName)) {
                return viewResolver;
            }
        }

        throw new IllegalArgumentException("해당 View를 처리할 수 없습니다.");
    }
}
