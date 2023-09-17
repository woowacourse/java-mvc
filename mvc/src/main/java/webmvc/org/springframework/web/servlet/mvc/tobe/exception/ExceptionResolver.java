package webmvc.org.springframework.web.servlet.mvc.tobe.exception;

import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ExceptionResolver {

    public ModelAndView handle(Throwable ex) {
        ModelAndView mv = null;
        if (ex instanceof HandlerNotFoundException) {
            mv = new ModelAndView(new JspView("/404.jsp"));
        }

        if (mv == null) {
            return new ModelAndView(new JspView("/500.jsp"));
        }

        return mv;
    }
}

