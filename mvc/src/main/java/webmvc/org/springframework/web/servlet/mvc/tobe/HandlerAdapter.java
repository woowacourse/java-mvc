package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdapter {

    boolean support(final Object handleExecution);

    ModelAndView doInternalService(final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   final Object method) throws Exception;
}
