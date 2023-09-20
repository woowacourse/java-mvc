package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandler implements Handler {

    private final Controller controller;

    public ManualHandler(Controller controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            String viewName = controller.execute(request, response);
            JspView view = new JspView(viewName);
            return new ModelAndView(view);
        } catch (Exception e) {
            throw new IllegalArgumentException("메서드 실행에 실패했습니다.", e);
        }
    }
}
