package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.mvc.HandlerKeys;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import com.interface21.webmvc.servlet.mvc.tobe.NotFoundMethodException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

public class MapHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(MapHandlerMapping.class);
    private static final String METHOD_NAME = "execute";

    private final HandlerKeys handlerKeys;

    public MapHandlerMapping(final HandlerKeys handlerKeys, final Map<HandlerKey, Controller> controllers) {
        this.handlerKeys = handlerKeys;
        controllers.forEach(this::init);
    }

    @Override
    public void initialize() {
        log.info("Initialized MapHandlerMapping!");
    }

    private void init(final HandlerKey key, final Controller controller) {
        final Method method = getExecuteMethod(controller.getClass());
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        handlerKeys.put(key, handlerExecution);
    }

    private static Method getExecuteMethod(final Class<? extends Controller> controller) {
        try {
            return controller.getDeclaredMethod(METHOD_NAME, HttpServletRequest.class, HttpServletResponse.class);
        } catch (final NoSuchMethodException e) {
            throw new NotFoundMethodException(String.format("%s 에서 %s 에 대한 메소드를 찾지 못했습니다.", controller, METHOD_NAME));
        }
    }

    @Override
    public HandlerExecution getHandler(final HandlerKey key) {
        return handlerKeys.get(key);
    }
}
