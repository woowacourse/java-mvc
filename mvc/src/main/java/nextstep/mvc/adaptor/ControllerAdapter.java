package nextstep.mvc.adaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exception.MvcComponentException;
import nextstep.mvc.handler.asis.Controller;
import nextstep.mvc.view.ModelAndView;

public class ControllerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (supports(handler)) {
            Controller controller = (Controller) handler;
            String viewName = controller.execute(request, response);
            return new ModelAndView(viewName);
        }
        throw new MvcComponentException("유효하지 않은 핸들러, 혹은 핸들러 어뎁터입니다.");
    }
}
