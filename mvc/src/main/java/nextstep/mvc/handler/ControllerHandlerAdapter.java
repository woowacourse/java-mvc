package nextstep.mvc.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class ControllerHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String viewName = ((Controller) handler).execute(request, response);
        ModelAndView modelAndView = new ModelAndView(new JspView(viewName));
        addAttribute(request, modelAndView);
        return modelAndView;
    }

    private void addAttribute(HttpServletRequest request, ModelAndView modelAndView) {
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attrName = attributeNames.nextElement();
            modelAndView.addObject(attrName, request.getAttribute(attrName));
        }
    }
}
