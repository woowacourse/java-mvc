package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static final String REDIRECT_PREFIX = "redirect:";
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private ManualHandlerMapping manualHandlerMapping;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var controller = manualHandlerMapping.getHandler(requestURI);
            ModelAndView modelAndView = controller.execute(request, response);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
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
