package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String id = (String) request.getAttribute("id");
        final ModelAndView modelAndView = new ModelAndView(null);
        return modelAndView.addObject("id", id);
    }
}
