package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ManualHandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static final String REDIRECT_PREFIX = "redirect:";
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = new ArrayList<>();

        addHandlerMapping(new ManualHandlerMapping());
        addHandlerMapping(new AnnotationHandlerMapping("com.interface21"));

        handlerAdapters = new ArrayList<>();

        handlerAdapters.add(new AnnotationHandlerAdapter());
        handlerAdapters.add(new ManualHandlerAdapter());
    }

    private void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappings.add(handlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = getHandler(request);

            ModelAndView modelAndView = execute(request, response, handler);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter.handle(request, response, handler);
            }
        }

        throw new IllegalArgumentException("해당 요청을 처리하는 어댑터가 존재하지 않습니다.");
    }

    private Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMappings : handlerMappings) {
            Object handler = handlerMappings.getHandler(request);

            if (handler != null) {
                return handler;
            }
        }

        throw new IllegalArgumentException("처리할 수 없는 요청입니다.");
    }

    private void render(ModelAndView mav,
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
        Object view = mav.getView();

        if (view instanceof String viewName) {
            if (viewName.startsWith(REDIRECT_PREFIX)) {
                response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
                return;
            }
            new JspView(viewName).render(mav.getModel(), request, response);
            return;
        }

        if (view instanceof View v) {
            v.render(mav.getModel(), request, response);
            return;
        }

        throw new IllegalStateException("지원하지 않는 뷰 타입: " + view);
    }
}
