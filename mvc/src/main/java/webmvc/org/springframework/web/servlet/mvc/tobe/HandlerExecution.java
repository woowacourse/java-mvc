package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JsonView;

public class HandlerExecution {

    private static final String ID = "id";

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        return modelAndView.addObject(ID, request.getAttribute(ID));
    }
}
