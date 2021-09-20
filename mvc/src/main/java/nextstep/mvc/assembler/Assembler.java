package nextstep.mvc.assembler;

import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.adaptor.ControllerAdapter;
import nextstep.mvc.adaptor.HandlerAdapters;
import nextstep.mvc.adaptor.HandlerExecutionAdapter;
import nextstep.mvc.handler.exception.ExceptionHandlerExecutor;
import nextstep.mvc.handler.tobe.AnnotationHandlerMapping;
import nextstep.mvc.handler.tobe.HandlerMappings;
import nextstep.mvc.view.View;
import nextstep.mvc.view.ViewName;
import nextstep.mvc.view.resolver.JsonViewResolver;
import nextstep.mvc.view.resolver.JspViewResolver;
import nextstep.mvc.view.resolver.RedirectionViewResolver;
import nextstep.mvc.view.resolver.ViewResolvers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Assembler {

    private final Map<Class<?>, Object> container = new HashMap<>();

    public void componentScan(String basePath) {
        handlerMappings(basePath);
        handlerAdaptors();
        viewResolvers();
        exceptionHandlerExecutor(basePath);
        dispatcherServlet();
    }

    public Object getBeanByType(Class<?> type) {
        return container.get(type);
    }

    private void handlerMappings(String basePath) {
        HandlerMappings handlerMappings = new HandlerMappings();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePath);
        handlerMappings.add(annotationHandlerMapping);

        container.put(HandlerMappings.class, handlerMappings);
    }

    private void handlerAdaptors() {
        HandlerExecutionAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();
        ControllerAdapter controllerAdapter = new ControllerAdapter();
        HandlerAdapters handlerAdapters = new HandlerAdapters(
                Arrays.asList(handlerExecutionAdapter, controllerAdapter));

        container.put(HandlerExecutionAdapter.class, handlerExecutionAdapter);
        container.put(ControllerAdapter.class, controllerAdapter);
        container.put(HandlerAdapters.class, handlerAdapters);
    }

    private void viewResolvers() {
        JspViewResolver jspViewResolver = new JspViewResolver();
        JsonViewResolver jsonViewResolver = new JsonViewResolver();
        RedirectionViewResolver redirectionViewResolver = new RedirectionViewResolver();

        ViewResolvers viewResolvers = new ViewResolvers(Arrays.asList(redirectionViewResolver, jspViewResolver, jsonViewResolver));
        container.put(ViewResolvers.class, viewResolvers);
    }

    private void exceptionHandlerExecutor(String basePath) {
        container.put(ExceptionHandlerExecutor.class, new ExceptionHandlerExecutor(basePath));
    }

    private void dispatcherServlet() {
        HandlerAdapters handlerAdapters = (HandlerAdapters) container.get(HandlerAdapters.class);
        HandlerMappings handlerMappings = (HandlerMappings) container.get(HandlerMappings.class);
        ViewResolvers viewResolvers = (ViewResolvers) container.get(ViewResolvers.class);
        ExceptionHandlerExecutor exceptionHandlerExecutor = (ExceptionHandlerExecutor) container.get(ExceptionHandlerExecutor.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappings, handlerAdapters, viewResolvers, exceptionHandlerExecutor);
        container.put(DispatcherServlet.class, dispatcherServlet);
    }
}
