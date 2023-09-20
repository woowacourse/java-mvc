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
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final transient List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

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
        private final List<HandlerAdapter> handlerAdapters;

        private Builder() {
            this.handlerMappings = new ArrayList<>();
            this.handlerAdapters = new ArrayList<>();
        }

        public Builder addHandlerMapping(final HandlerMapping handlerMapping) {
            handlerMappings.add(handlerMapping);
            return this;
        }

        public Builder addHandlerAdapter(final HandlerAdapter handlerAdapter) {
            handlerAdapters.add(handlerAdapter);
            return this;
        }

        public DispatcherServlet build() {
            return new DispatcherServlet(this);
        }

        private List<HandlerMapping> getHandlerMappings() {
            return handlerMappings;
        }

        public List<HandlerAdapter> getHandlerAdapters() {
            return handlerAdapters;
        }
    }

    private DispatcherServlet(final Builder builder) {
        this.handlerMappings = builder.getHandlerMappings();
        this.handlerAdapters = builder.getHandlerAdapters();
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
            final Object handler = findHandlerAdapter(request);
            final HandlerAdapter handlerAdapter = findHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);
            resolveView(modelAndView, request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandlerAdapter(final HttpServletRequest request) {
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

    private HandlerAdapter findHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("요청을 처리할 수 있는 HandlerAdapter를 찾을 수 없습니다!: "));
    }

    private void resolveView(final ModelAndView modelAndView, final HttpServletRequest request,
                             final HttpServletResponse response) throws Exception {
        final View view = modelAndView.getView();
        final Map<String, Object> model = modelAndView.getModel();
        view.render(model, request, response);
    }
}
