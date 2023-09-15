package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final ViewResolvers viewResolvers = new ViewResolvers();
    private final HandlerMappings mappings = new HandlerMappings();
    private final HandlerAdapters adapters = new HandlerAdapters();

    @Override
    public void init() {
        viewResolvers.initialize();
        mappings.initialize();
        adapters.initialize(viewResolvers);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = mappings.getHandler(request);

            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return ;
            }

            final HandlerAdapter handlerAdapter = adapters.getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.execute(request, response, handler);

            move(modelAndView, request, response);
        } catch (final Exception e) {
            log.error("Exception : {}", e.getMessage(), e);

            throw new ServletException(e.getMessage());
        }
    }

    /**
     * Legacy MVC + @MVC 통합 과정에서 Controller.execute()로 바로 viewName을 반환받던 코드에서 ModelAndView를 반환받도록 변경이 되었습니다.
     * 그래서 View 관련 코드를 작성하다가 미션을 보니까 3단계...에서 하는 것 같더라고요...
     * 그래서 일단 이렇게 방치를 하게 되었습니다...
     */
    private void move(
            final ModelAndView modelAndView,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        // ignored
    }
}
