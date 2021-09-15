package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.adapter.HandlerAdapterRegistry;
import nextstep.mvc.adapter.HandlerMappingRegistry;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import nextstep.mvc.view.resolver.UrlBasedViewResolver;
import nextstep.mvc.view.resolver.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final long serialVersionUID = 1L;
    private static final List<ViewResolver> viewResolvers = new ArrayList<>();

    @Override
    public void init() {
        HandlerMappingRegistry.initAll();
        initViewResolvers();
    }

    private void initViewResolvers() {
        viewResolvers.add(new UrlBasedViewResolver());
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        HandlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        HandlerAdapterRegistry.addHandlerAdapters(handlerAdapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        LOG.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        ModelAndView mv;
        View view;
        try {
            Object handler = getHandler(request);
            HandlerAdapter handlerAdapter = HandlerAdapterRegistry.getHandlerAdapter(handler);
            mv = handlerAdapter.handle(request, response, handler);
        } catch (ClassNotFoundException e) {
            mv = new ModelAndView("/404.jsp");

        }

        try {
            view = resolveViewName(mv.getViewName());
            Objects.requireNonNull(view).render(mv.getModel(), request, response);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private View resolveViewName(String viewName) {
        for (ViewResolver viewResolver : viewResolvers) {
            View view = viewResolver.resolveViewName(viewName);
            if (Objects.nonNull(view)) {
                return view;
            }
        }
        return null;
    }

    private void handleException(Exception e) throws ServletException {
        LOG.error("Exception : {}", e.getMessage(), e);
        throw new ServletException(e.getMessage());
    }

    private Object getHandler(HttpServletRequest request) throws ClassNotFoundException {
        return HandlerMappingRegistry.getHandler(request)
                .orElseThrow(() -> new ClassNotFoundException("해당 url을 찾을 수 없습니다."));
    }
}
