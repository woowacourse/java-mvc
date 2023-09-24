package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public class NotFoundController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        return "/404.jsp";
    }
}
