package nextstep.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.mapping.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.RedirectView;

public class ViewNameHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return ((HandlerExecution) handler).hasReturnTypeOf(String.class);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        final var viewName = (String) ((HandlerExecution) handler).handle(request, response);
        return toModelAndView(viewName);
    }

    private ModelAndView toModelAndView(String viewName) {
        if (viewName.startsWith(RedirectView.REDIRECT_PREFIX)) {
            return new ModelAndView(new RedirectView(viewName));
        }
        return new ModelAndView(new JspView(viewName));
    }
}
