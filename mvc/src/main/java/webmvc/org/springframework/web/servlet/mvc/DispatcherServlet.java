package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.exception.AdapterNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = new ArrayList<>();
        handlerMappings.add(new AnnotationHandlerMapping("com.techcourse"));
        handlerMappings.forEach(HandlerMapping::initialize);

        handlerAdapters = new ArrayList<>();
        handlerAdapters.add(new RequestMappingHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final Object handler = handlerMappings.stream()
                .map(mapping -> mapping.getHandler(request))
                .findFirst()
                .orElseThrow(() -> new HandlerNotFoundException("존재하지 않는 요청입니다."));
        final HandlerAdapter handlerAdapter = handlerAdapters.stream()
                .filter(Adapter -> Adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new AdapterNotFoundException("정의하지 않는 요청입니다."));

        try {
            final ModelAndView handle = handlerAdapter.handle(request, response, handler);
            final View view = handle.getView();
            final Map<String, Object> model = handle.getModel();
            view.render(model, request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
