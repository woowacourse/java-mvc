package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private AnnotationHandlerMapping handlerMapping;

    @Override
    public void init() {
        handlerMapping = new AnnotationHandlerMapping("com.techcourse");
        handlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        log.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        try {
            final HandlerExecution handler = (HandlerExecution) handlerMapping.getHandler(req);
            if (handler == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final ModelAndView modelAndView = handler.handle(req, res);
            render(modelAndView, req, res);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final View view = modelAndView.getView();
        view.render(modelAndView.getModel(), req, res);
    }
}
