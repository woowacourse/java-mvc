package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView(new JspView(""));
        Object attribute = request.getAttribute("id");
        modelAndView.addObject("id", attribute);
        return modelAndView;
    }
}
