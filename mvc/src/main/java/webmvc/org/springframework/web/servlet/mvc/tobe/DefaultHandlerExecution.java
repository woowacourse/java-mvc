package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.lang.reflect.Method;

public class DefaultHandlerExecution extends HandlerExecution {
    public DefaultHandlerExecution(final Method method, final Object controller) {
        super(method, controller);
    }
}
