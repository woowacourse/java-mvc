package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;
import java.util.Map;
import nextstep.web.support.RequestMethod;
import nextstep.web.support.RequestUrl;

public class HandlerExecutions {

    private final Map<HandlerKey, HandlerExecution> values;

    public HandlerExecutions(final Map<HandlerKey, HandlerExecution> values) {
        this.values = values;
    }

    public Object get(final RequestUrl requestURI, final RequestMethod requestMethod) {
        HandlerKey key = new HandlerKey(requestURI, requestMethod);
        return values.get(key);
    }

    public void put(final RequestUrl requestUrl, final RequestMethod requestMethod, final Object instance, final Method method) {
        HandlerKey key = new HandlerKey(requestUrl, requestMethod);
        HandlerExecution execution = new HandlerExecution(instance, method);
        values.put(key, execution);
    }
}
