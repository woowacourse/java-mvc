package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DefaultHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupport(Handler handler) {
        return true;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Handler handler) throws Exception {
        return new ModelAndView(new JspView("/404.jsp"));
    }
}
