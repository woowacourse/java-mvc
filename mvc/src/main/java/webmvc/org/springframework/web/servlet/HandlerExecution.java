package webmvc.org.springframework.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerExecution {

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
