package nextstep.mvc.controller.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                               Object handler) throws Exception {
        String urlPath = ((Controller) handler).execute(request, response);
        ModelAndView modelAndView = new ModelAndView(new JspView(urlPath));

        request.getAttributeNames()
            .asIterator()
            .forEachRemaining(name -> modelAndView.addObject(name, request.getAttribute(name)));

        return modelAndView;
    }
}
