package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecution {

    private final Method method;
    private final Object controller;

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    public HandlerExecution(final Method method, final Object controller) {
        this.method = method;
        this.controller = controller;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        log.info("handler Execution");
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
