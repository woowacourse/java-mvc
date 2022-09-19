package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public enum HandlerAdapter {

    CONTROLLER(Controller.class) {
        @Override
        protected ModelAndView eachHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            Controller controller = (Controller) handler;
            String viewName = controller.execute(request, response);
            return new ModelAndView(new JspView(viewName));
        }
    },
    ANNOTATION(HandlerExecution.class) {
        @Override
        protected ModelAndView eachHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            return ((HandlerExecution) handler).handle(request, response);
        }
    };

    private final Class<?> handlerType;

    HandlerAdapter(Class<?> handlerType) {
        this.handlerType = handlerType;
    }

    protected abstract ModelAndView eachHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception;

    public static ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerAdapter handlerAdapter = Arrays.stream(values())
                .filter(adapter -> adapter.handlerType.isInstance(handler))
                .findFirst()
                .orElseThrow();

        return handlerAdapter.eachHandle(request, response, handler);
    }
}
