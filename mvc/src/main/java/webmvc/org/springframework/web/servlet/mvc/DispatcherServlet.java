package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.adapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.adapter.HandlerAdapterException;
import webmvc.org.springframework.web.servlet.mvc.handler.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerMappingException;
import webmvc.org.springframework.web.servlet.mvc.viewresolver.JsonViewResolver;
import webmvc.org.springframework.web.servlet.mvc.viewresolver.JspViewResolver;
import webmvc.org.springframework.web.servlet.mvc.viewresolver.ViewResolver;
import webmvc.org.springframework.web.servlet.mvc.viewresolver.ViewResolverException;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private final List<ViewResolver> viewResolvers = new ArrayList<>();

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdapters();
        initViewResolvers();
    }

    private void initHandlerMappings() {
        handlerMappings.add(new AnnotationHandlerMapping());
        for (final HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    private void initViewResolvers() {
        viewResolvers.add(new JspViewResolver());
        viewResolvers.add(new JsonViewResolver());
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
        for (final HandlerMapping handlerMapping : handlerMappings) {
            final Optional<Object> handler = handlerMapping.getHandler(request);
            if (handler.isPresent()) {
                return handler.get();
            }
        }
        throw new HandlerMappingException("해당 요청에 대한 핸들러가 없습니다.");
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        for (final HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        throw new HandlerAdapterException("해당 요청에 대한 핸들러 어댑터가 없습니다.");
    }

    private View getView(final ModelAndView modelAndView) {
        final Object view = modelAndView.getView();
        return viewResolvers.stream()
                .filter(viewResolver -> viewResolver.supports(view))
                .findFirst()
                .map(viewResolver -> viewResolver.resolve(view))
                .orElseThrow(() -> new ViewResolverException("해당 요청에 대한 view를 처리할 수 없습니다."));
    }
}
