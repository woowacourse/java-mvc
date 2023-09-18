package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.util.ReflectionUtils;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final Object[] basePackage;

    private List<HandlerMapping> handlerMappings;

    public DispatcherServlet(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        initHandlerMappings();
    }

    private void initHandlerMappings() {
        handlerMappings = new ArrayList<>();
        addCustomHandlerMappings();
        addPreDefinedHandlerMappings();
    }

    private void addCustomHandlerMappings() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<? extends HandlerMapping>> subTypes = reflections.getSubTypesOf(
                HandlerMapping.class);
        for (Class<? extends HandlerMapping> clazz : subTypes) {
            handlerMappings.add((HandlerMapping) ReflectionUtils.instantiate(clazz));
        }
    }

    private void addPreDefinedHandlerMappings() {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(
                basePackage);
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            HandlerExecution handlerExecution = getHandler(request);
            ModelAndView modelAndView = handlerExecution.handle(request, response);
            processDispatchResult(request, response, modelAndView);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerExecution getHandler(HttpServletRequest request) throws Exception {
        for (HandlerMapping handlerMapping : handlerMappings) {
            HandlerExecution handlerExecution = handlerMapping.getHandler(request);
            if (handlerExecution != null) {
                return handlerExecution;
            }
        }
        return null;
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
            ModelAndView modelAndView)
            throws Exception {
        modelAndView.getView().render(modelAndView.getModel(), request, response);
    }
}
