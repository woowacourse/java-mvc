package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.exception.UnsupportedViewFileExtensionException;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class InterfaceBasedControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Controller controller = (Controller) handler;
        String viewName = controller.execute(request, response);

        if (viewName.endsWith(JspView.JSP_EXTENSION)) {
            return new ModelAndView(new JspView(viewName));
        }
        throw new UnsupportedViewFileExtensionException();
    }
}
