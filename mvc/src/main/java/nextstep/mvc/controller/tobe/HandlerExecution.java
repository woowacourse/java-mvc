package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exception.MethodInvokeException;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    private final Method method;
    private final Object target;

    public HandlerExecution(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            return (ModelAndView) this.method.invoke(this.target, request, response);
        } catch (Exception e) {
            log.error("매핑된 메서드를 실행할 수 없습니다.", e);
            throw new MethodInvokeException("매핑된 메서드를 실행할 수 없습니다.");
        }
    }
}
