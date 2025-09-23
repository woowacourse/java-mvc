package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutor;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;
    private HandlerExecutor handlerExecutor;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();

        initHandlerMappings();
        initHandlerAdapters();

        handlerExecutor = new HandlerExecutor(handlerMappingRegistry, handlerAdapterRegistry);
    }

    private void initHandlerMappings() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        handlerMappingRegistry.initialize();
    }

    private void initHandlerAdapters() {
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            ModelAndView mav = handlerExecutor.execute(request, response);
            if (mav == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            render(mav, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws Exception {
        mav.getView().render(mav.getModel(), request, response);
    }
}
