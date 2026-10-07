package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.RequestMappingHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {
    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    public DispatcherServlet(String... basePackage) {
        validateBasePackage(basePackage);
        registerHandlers(basePackage);
    }

    private void validateBasePackage(String[] basePackage) {
        if (basePackage.length == 0) {
            throw new IllegalArgumentException("루트 패키지를 지정해주세요");
        }
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    private void registerHandlers(String... basePackage) {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        addHandlerMapping(annotationHandlerMapping);
        addHandlerAdapter(new RequestMappingHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<Object> foundHandler = handlerMappingRegistry.getHandler(request);
        if (foundHandler.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Object handler = foundHandler.get();
        try {
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (IOException | ServletException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void render(final ModelAndView modelAndView,
                        final HttpServletRequest request,
                        final HttpServletResponse response) throws Exception {
        if (modelAndView == null) {
            return;
        }

        request.setAttribute("modelAndView", modelAndView);
        Map<String, Object> model = modelAndView.getModel();
        modelAndView.getView().render(model, request, response);
    }
}
