package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class ManualHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Controller controller = (Controller) handler;
        String path = controller.execute(request, response);
        JspView jspView = new JspView(path);
        ModelAndView modelAndView = new ModelAndView(jspView);
        request.getAttributeNames().asIterator()
                .forEachRemaining(name -> modelAndView.addObject(name, request.getAttribute(name)));
        return modelAndView;
    }
}
