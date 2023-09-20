package webmvc.org.springframework.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final transient List<HandlerMapping> handlerMappings;

    public static InitBuilder Builder() {
        return new InitBuilder();
    }

    public static class InitBuilder {

        private InitBuilder() {
        }

        public Builder addHandlerMapping(final HandlerMapping handlerMapping) {
            return new Builder().addHandlerMapping(handlerMapping);
        }
    }

    public static class Builder {

        private final List<HandlerMapping> handlerMappings;

        private Builder() {
            this.handlerMappings = new ArrayList<>();
        }

        public Builder addHandlerMapping(final HandlerMapping handlerMapping) {
            handlerMappings.add(handlerMapping);
            return this;
        }

        public DispatcherServlet build() {
            return new DispatcherServlet(this);
        }

        private List<HandlerMapping> getHandlerMappings() {
            return handlerMappings;
        }
    }

    private DispatcherServlet(final Builder builder) {
        this.handlerMappings = builder.getHandlerMappings();
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = findHandler(request);
            final ModelAndView modelAndView = handle(request, response, handler);
            resolveView(modelAndView, request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        if(handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(request, response);
        }
        if(handler instanceof Controller) {
            final String viewName = ((Controller) handler).execute(request, response);
            return new ModelAndView(new JspView(viewName));
        }
        throw new UnsupportedOperationException();
    }

    private Object findHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        "요청을 처리할 수 있는 Handler를 찾을 수 없습니다!: "
                        + request.getMethod() + " "
                        + request.getRequestURI())
                );
    }

    private void resolveView(final ModelAndView modelAndView, final HttpServletRequest request,
                             final HttpServletResponse response) throws Exception {
        final View view = modelAndView.getView();
        final Map<String, Object> model = modelAndView.getModel();
        view.render(model, request, response);
    }
}
