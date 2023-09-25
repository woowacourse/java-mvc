package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    HandlerExecution getHandler(HttpServletRequest request);

}
