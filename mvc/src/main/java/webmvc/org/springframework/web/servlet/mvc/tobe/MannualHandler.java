package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class MannualHandler implements Handler {

    private final Controller controller;

    public MannualHandler(final Controller controller) {
        this.controller = controller;
    }

    @Override
    public boolean isSupport() {
        return controller != null;
    }
    
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String viewName = controller.execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }

    public Controller getController() {
        return controller;
    }
}
