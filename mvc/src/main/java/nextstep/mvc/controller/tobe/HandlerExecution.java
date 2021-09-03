package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution implements Controller{

    private final Object controller;
    private final Method handler;

    public HandlerExecution(Object controller, Method handler) {
        this.controller = controller;
        this.handler = handler;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView((model, request1, response1) -> {
        });

        Object id = request.getAttribute("id");
        modelAndView.addObject("id", id);

        return modelAndView;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        return (String) handler.invoke(controller, req, res);
    }
}
