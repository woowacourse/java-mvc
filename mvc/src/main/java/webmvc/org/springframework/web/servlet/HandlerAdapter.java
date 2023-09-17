package webmvc.org.springframework.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean isSupport(Object handler);

    Object handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
