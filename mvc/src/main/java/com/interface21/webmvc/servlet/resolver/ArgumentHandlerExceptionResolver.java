package com.interface21.webmvc.servlet.resolver;

import com.interface21.webmvc.servlet.HandlerExceptionResolver;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgumentHandlerExceptionResolver implements HandlerExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger(ArgumentHandlerExceptionResolver.class);

    @Override
    public ModelAndView resolveException(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws ServletException {

        Throwable cause = ex.getCause();

        try {
            if (cause instanceof IllegalArgumentException) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain");
                response.getWriter().write(cause.getMessage());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.error("Exception : {}", ex.getMessage(), ex);
        throw new ServletException(ex.getMessage());
    }
}
