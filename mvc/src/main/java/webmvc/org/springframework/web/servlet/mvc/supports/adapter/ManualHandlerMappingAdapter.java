package webmvc.org.springframework.web.servlet.mvc.supports.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.View;
import webmvc.org.springframework.web.servlet.mvc.supports.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.Controller;
import webmvc.org.springframework.web.servlet.mvc.supports.resolver.ViewResolvers;

public class ManualHandlerMappingAdapter implements HandlerAdapter {

    private final ViewResolvers resolvers;

    public ManualHandlerMappingAdapter(final ViewResolvers resolvers) {
        validateViewResolvers(resolvers);

        this.resolvers = resolvers;
    }

    private void validateViewResolvers(final ViewResolvers resolvers) {
        if (resolvers.isEmpty()) {
            throw new IllegalArgumentException("viewResolver를 지정해주세요.");
        }
    }

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView execute(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        final String viewName = ((Controller) handler).execute(request, response);
        final View view = resolvers.findView(request, viewName);

        return new ModelAndView(view);
    }
}
