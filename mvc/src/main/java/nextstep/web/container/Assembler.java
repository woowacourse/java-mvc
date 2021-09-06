package nextstep.web.container;

import java.util.Arrays;
import java.util.List;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.adaptor.ControllerAdapter;
import nextstep.mvc.adaptor.HandlerAdapter;
import nextstep.mvc.adaptor.HandlerAdapters;
import nextstep.mvc.adaptor.HandlerExecutionAdapter;
import nextstep.mvc.mapper.tobe.HandlerMappings;
import nextstep.mvc.view.resolver.ViewResolver;
import nextstep.mvc.view.resolver.ViewResolverImpl;

public class Assembler {

    private final DispatcherServlet dispatcherServlet;

    public Assembler() {
        HandlerMappings handlerMappings = new HandlerMappings();
        HandlerAdapters handlerAdapters = handlerAdaptors();
        ViewResolver viewResolver = new ViewResolverImpl();

        dispatcherServlet = new DispatcherServlet(handlerMappings, handlerAdapters, viewResolver);
    }

    public DispatcherServlet getDispatcherServlet() {
        return dispatcherServlet;
    }

    private HandlerAdapters handlerAdaptors() {
        List<HandlerAdapter> handlerAdapters = Arrays.asList(
                new HandlerExecutionAdapter(), new ControllerAdapter()
        );
        return new HandlerAdapters(handlerAdapters);
    }
}
