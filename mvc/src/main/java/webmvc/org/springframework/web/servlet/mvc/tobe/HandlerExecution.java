package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    private final Method method;

    private Object executeInstance;

    public HandlerExecution(Class<?> controller, Method method) {
        this.method = method;
        try {
            this.executeInstance = controller.getConstructor().newInstance();
        } catch (Exception e) {
            log.error("handler실핼 인스턴스를 생성할 수 없습니다.");
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(executeInstance, request, response);
        } catch (IllegalAccessException e) {
            log.error("fail to access method");
            throw new IllegalArgumentException("handler실행에 접근할 수 없습니다.");
        } catch (InvocationTargetException e) {
            log.error("fail to invoke method");
            throw new IllegalArgumentException("handler 실행 중 오류가 발생했습니다.");
        }
    }
}
