package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean support(Object handler);

    String invoke(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
