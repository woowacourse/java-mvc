package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface MyController {
    ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception;
}
