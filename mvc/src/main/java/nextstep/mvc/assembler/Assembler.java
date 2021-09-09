package nextstep.mvc.assembler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.adaptor.ControllerAdapter;
import nextstep.mvc.adaptor.HandlerAdapters;
import nextstep.mvc.adaptor.HandlerExecutionAdapter;
import nextstep.mvc.handler.exception.ExceptionHandlerExecutor;
import nextstep.mvc.handler.tobe.AnnotationHandlerMapping;
import nextstep.mvc.handler.tobe.HandlerMappings;
import nextstep.mvc.view.resolver.ViewResolver;
import nextstep.mvc.view.resolver.ViewResolverImpl;

public class Assembler {

    private final Map<Class<?>, Object> container = new HashMap<>();

    public void componentScan(String basePath) {
        handlerMappings(basePath);
        handlerAdaptors();
        viewResolver();
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

    private void viewResolver() {
        container.put(ViewResolver.class, new ViewResolverImpl());
    }

    private void exceptionHandlerExecutor(String basePath) {
        container.put(ExceptionHandlerExecutor.class, new ExceptionHandlerExecutor(basePath));
    }

    private void dispatcherServlet() {
        HandlerAdapters handlerAdapters = (HandlerAdapters) container.get(HandlerAdapters.class);
        HandlerMappings handlerMappings = (HandlerMappings) container.get(HandlerMappings.class);
        ViewResolver viewResolver = (ViewResolver) container.get(ViewResolver.class);
        ExceptionHandlerExecutor exceptionHandlerExecutor = (ExceptionHandlerExecutor) container.get(ExceptionHandlerExecutor.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappings, handlerAdapters, viewResolver, exceptionHandlerExecutor);
        container.put(DispatcherServlet.class, dispatcherServlet);
    }
}
