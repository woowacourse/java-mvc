package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import nextstep.mvc.adaptor.HandlerAdaptors;
import nextstep.mvc.mapper.tobe.HandlerMapping;
import nextstep.mvc.view.Model;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import nextstep.mvc.view.resolver.ViewResolver;
import nextstep.mvc.view.resolver.ViewResolverImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final HandlerAdaptors handlerAdaptors;

    private final ViewResolver viewResolver;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
        this.handlerAdaptors = new HandlerAdaptors(null);
        this.viewResolver = new ViewResolverImpl();
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = getHandler(request);
            ModelAndView modelAndView = handlerAdaptors.service(request, response, handler);

            Model model = modelAndView.getModel();

            View view = viewResolver.resolve(modelAndView.getViewName());
            System.out.println(view);
            view.render(model, request, response);
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    // TODO :: 역할 분리 리팩토링
    private Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow();
    }
}
