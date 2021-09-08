package nextstep.mvc.assembler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.adaptor.ControllerAdapter;
import nextstep.mvc.adaptor.HandlerAdapter;
import nextstep.mvc.adaptor.HandlerAdapters;
import nextstep.mvc.adaptor.HandlerExecutionAdapter;
import nextstep.mvc.handler.exception.ExceptionHandlerExecutor;
import nextstep.mvc.handler.tobe.AnnotationHandlerMapping;
import nextstep.mvc.handler.tobe.HandlerMapping;
import nextstep.mvc.handler.tobe.HandlerMappings;
import nextstep.mvc.view.resolver.ViewResolver;
import nextstep.mvc.view.resolver.ViewResolverImpl;

public class Assembler {

    private final Map<Class<?>, Object> container;

    private final String basePath;

    public Assembler(String basePath) {
        this.basePath = basePath;

        this.container = new ComponentScanner().scan(basePath);

        this.container.put(HandlerMappings.class, handlerMappings());
        this.container.put(HandlerAdapters.class, handlerAdaptors());
        this.container.put(ExceptionHandlerExecutor.class, new ExceptionHandlerExecutor(basePath));
        this.container.put(ViewResolver.class, new ViewResolverImpl());
        this.container.put(DispatcherServlet.class, dispatcherServlet());
    }

    public DispatcherServlet dispatcherServlet() {
        HandlerMappings handlerMappings = (HandlerMappings) container.get(HandlerMappings.class);
        HandlerAdapters handlerAdaptors = (HandlerAdapters) container.get(HandlerAdapters.class);
        ExceptionHandlerExecutor exceptionHandlerExecutor = (ExceptionHandlerExecutor) container.get(ExceptionHandlerExecutor.class);
        ViewResolver viewResolver = (ViewResolver) container.get(ViewResolver.class);

        final DispatcherServlet dispatcherServlet
                = new DispatcherServlet(handlerMappings, handlerAdaptors, viewResolver, exceptionHandlerExecutor);

        return dispatcherServlet;
    }

    private HandlerAdapters handlerAdaptors() {
        List<HandlerAdapter> handlerAdapters = Arrays.asList(
                new HandlerExecutionAdapter(), new ControllerAdapter()
        );
        return new HandlerAdapters(handlerAdapters);
    }

    private HandlerMappings handlerMappings(){
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePath);
        this.container.put(AnnotationHandlerMapping.class, annotationHandlerMapping);

        HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.add(annotationHandlerMapping);

        return handlerMappings;
    }
}
