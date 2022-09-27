package nextstep.mvc.controller.tobe;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import nextstep.web.support.RequestMethod;

public class HandlerExecutions {

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new ConcurrentHashMap<>();

    public void add(final HandlerKey handlerKey, final HandlerExecution handlerExecution) {
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    public Object getHandlerExecution(final String uri, final String method) {
        return handlerExecutions.get(new HandlerKey(uri, RequestMethod.valueOf(method)));
    }

    public Set<HandlerKey> getHandlers() {
        return handlerExecutions.keySet();
    }
}
