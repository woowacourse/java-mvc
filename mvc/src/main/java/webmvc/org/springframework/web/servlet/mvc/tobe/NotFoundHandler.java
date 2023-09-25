package webmvc.org.springframework.web.servlet.mvc.tobe;

import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

public class NotFoundHandler {

    public ModelAndView handle(String viewName) {
        return new ModelAndView(new JspView(viewName));
    }
}
