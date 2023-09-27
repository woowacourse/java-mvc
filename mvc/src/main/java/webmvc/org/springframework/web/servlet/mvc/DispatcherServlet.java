package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.adapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.adapter.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.handler.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerMappingException;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerMappings;
import webmvc.org.springframework.web.servlet.mvc.viewresolver.JsonViewResolver;
import webmvc.org.springframework.web.servlet.mvc.viewresolver.JspViewResolver;
import webmvc.org.springframework.web.servlet.mvc.viewresolver.ViewResolverException;
import webmvc.org.springframework.web.servlet.mvc.viewresolver.ViewResolvers;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMappings;
    private HandlerAdapters handlerAdapters;
    private ViewResolvers viewResolvers;

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdapters();
        initViewResolvers();
    }

    private void initHandlerMappings() {
        handlerMappings = new HandlerMappings(
                List.of(new AnnotationHandlerMapping())
        );
        handlerMappings.initialize();
    }

    private void initHandlerAdapters() {
        handlerAdapters = new HandlerAdapters(
                List.of(new AnnotationHandlerAdapter())
        );
    }

    private void initViewResolvers() {
        viewResolvers = new ViewResolvers(
                List.of(new JspViewResolver(), new JsonViewResolver())
        );
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = getHandler(request);
            final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);
            final View view = getView(modelAndView);
            final Map<String, Object> model = modelAndView.getModel();
            view.render(model, request, response);
        } catch (final HandlerMappingException | ViewResolverException e) {
            throw e;
        } catch (final Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.getHandler(request);
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.getHandlerAdapter(handler);
    }

    private View getView(final ModelAndView modelAndView) {
        return viewResolvers.getView(modelAndView);
    }
}
