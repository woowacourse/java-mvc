package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.handlerAdapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.view.ViewAdapter;

public class ControllerHandlerAdaptor implements HandlerAdapter {

    private final ViewAdapter viewAdapter;

    public ControllerHandlerAdaptor(ViewAdapter viewAdapter) {
        this.viewAdapter = viewAdapter;
    }

    @Override
    public boolean support(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Controller controller = (Controller) handler;
        String viewName = controller.execute(request, response);
        View view = viewAdapter.getView(viewName);
        return new ModelAndView(view);
    }
}
