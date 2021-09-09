package nextstep.mvc.controller.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public interface Controller {
    String execute(HttpServletRequest req, HttpServletResponse res) throws Exception;
}
