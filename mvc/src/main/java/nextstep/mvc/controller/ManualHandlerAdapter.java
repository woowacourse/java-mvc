package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler)
            throws Exception {
        String executedResponse = ((Controller) handler).execute(request, response);
        ModelAndView modelAndView = new ModelAndView(new JspView(executedResponse));
        addAttribute(request, modelAndView);
        return modelAndView;
    }

    private void addAttribute(final HttpServletRequest request, final ModelAndView modelAndView) {
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            modelAndView.addObject(name, request.getAttribute(name));
        }
    }
}
