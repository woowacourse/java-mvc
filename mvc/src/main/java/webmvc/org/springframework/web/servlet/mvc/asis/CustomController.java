package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CustomController {

    String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception;

}
