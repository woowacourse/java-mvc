package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.ViewFactory;

public class HandlerExecution {

    private final HandlerMethod handlerMethod;
    private final ViewFactory viewFactory;

    public HandlerExecution(final HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
        this.viewFactory = new ViewFactory();
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object invokedResponse = handlerMethod.invoke(request, response);
        if (invokedResponse instanceof ModelAndView) {
            return (ModelAndView) invokedResponse;
        }
        return new ModelAndView(viewFactory.createView((String) invokedResponse));
    }
}
