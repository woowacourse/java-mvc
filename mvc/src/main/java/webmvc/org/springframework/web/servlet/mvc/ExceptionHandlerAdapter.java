package webmvc.org.springframework.web.servlet.mvc;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ExceptionHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Exception;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        handleException(response, (Exception) handler);
    }

    protected void handleException(HttpServletResponse response, Exception exception) throws IOException {
        response.sendError(SC_INTERNAL_SERVER_ERROR, exception.getMessage());
    }

}
