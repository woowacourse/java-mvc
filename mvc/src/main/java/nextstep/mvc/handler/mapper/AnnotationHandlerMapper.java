package nextstep.mvc.handler.mapper;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nextstep.context.PeanutContainer;
import nextstep.mvc.handler.tobe.HandlerExecution;
import nextstep.mvc.handler.tobe.HandlerKey;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapper implements HandlerMapper {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapper.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapper(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        final Map<HandlerKey, HandlerExecution> handlerExecutions = new Reflections(basePackages)
                .getTypesAnnotatedWith(Controller.class)
                .stream()
                .map(PeanutContainer.INSTANCE::getPeanut)
                .flatMap(this::createHandlerExecutions)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        this.handlerExecutions.putAll(handlerExecutions);
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.of(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private Stream<? extends Entry<HandlerKey, HandlerExecution>> createHandlerExecutions(final Object handler) {
        return Arrays.stream(handler.getClass().getMethods())
                .filter(this::hasRequestMappingAnnotation)
                .flatMap(getMethodStreamFunction(handler));
    }

    private boolean hasRequestMappingAnnotation(final Method method) {
        return method.isAnnotationPresent(RequestMapping.class);
    }

    private Function<Method, Stream<? extends Entry<HandlerKey, HandlerExecution>>> getMethodStreamFunction(final Object handler) {
        return method -> {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final String url = requestMapping.value();
            final List<HandlerKey> handlerKeys = createHandlerKeys(requestMapping, url);
            final HandlerExecution handlerExecution = new HandlerExecution(handler, method);
            return handlerKeys.stream()
                    .map(handlerKey -> Map.entry(handlerKey, handlerExecution));
        };
    }

    private List<HandlerKey> createHandlerKeys(final RequestMapping requestMapping, final String url) {
        return Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .collect(Collectors.toUnmodifiableList());
    }
}
