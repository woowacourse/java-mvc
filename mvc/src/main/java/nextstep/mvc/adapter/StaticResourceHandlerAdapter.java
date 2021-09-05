package nextstep.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.StaticResourceHandler;
import nextstep.mvc.view.ModelAndView;

public class StaticResourceHandlerAdapter implements HandlerAdapter{

    @Override
    public boolean supports(Object handler) {
        return handler instanceof StaticResourceHandler;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                               Object handler) throws Exception {
        final String fileUri = request.getRequestURI();
        return new ModelAndView(null);
    }
}
