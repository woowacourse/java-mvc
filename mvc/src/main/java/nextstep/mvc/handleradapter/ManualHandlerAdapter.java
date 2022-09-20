package nextstep.mvc.handleradapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(
            final HttpServletRequest request, final HttpServletResponse response, final Object handler
    ) throws Exception {
        String executedResult = ((Controller) handler).execute(request, response);
        ModelAndView modelAndView = new ModelAndView(new JspView(executedResult));
        setAttribute(request, modelAndView);
        return modelAndView;
    }

    private void setAttribute(final HttpServletRequest request, final ModelAndView modelAndView) {
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            modelAndView.addObject(attributeName, request.getAttribute(attributeName));
        }
    }
}
