package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.ViewFactory;

public class ManualHandlerAdapter implements HandlerAdapter {

    private final ViewFactory viewFactory;

    public ManualHandlerAdapter() {
        this.viewFactory = new ViewFactory();
    }

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler)
            throws Exception {
        String executedResponse = ((Controller) handler).execute(request, response);
        ModelAndView modelAndView = new ModelAndView(viewFactory.createView(executedResponse));
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
