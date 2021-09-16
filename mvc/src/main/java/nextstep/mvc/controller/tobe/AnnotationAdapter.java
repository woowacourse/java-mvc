package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.RedirectView;
import nextstep.mvc.view.View;

public class AnnotationAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        Object result = handlerExecution.handle(request, response);
        if (result instanceof String) {
            View view = getView((String) result);
            return new ModelAndView(view);
        }
        return (ModelAndView) result;
    }

    private View getView(String viewName) {
        if (viewName.startsWith(RedirectView.REDIRECT_PREFIX)) {
            return new RedirectView(viewName);
        }
        return new JspView(viewName);
    }
}
