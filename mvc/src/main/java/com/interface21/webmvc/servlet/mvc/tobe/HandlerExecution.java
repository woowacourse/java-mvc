package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);
    private final Class<?> aClass;
    private final Method method;

    public HandlerExecution(Class<?> aClass, Method method) {
        this.aClass = aClass;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        try {
            return (ModelAndView) method.invoke(
                    aClass.getDeclaredConstructor().newInstance(),
                    request,
                    response);
        } catch (final InvocationTargetException e) {
            log.error("내부 예외 발생", e.getCause());
            throw e;
        } catch (final InstantiationException e) {
            log.error("인스턴스 생성 실패 ", e);
            throw e;
        } catch (final NoSuchMethodException e) {
            log.error("생성자 없음 ", e);
            throw e;
        } catch (final IllegalAccessException e) {
            log.error("접근 불가 ", e);
            throw e;
        } catch (final ClassCastException e) {
            log.error("잘못된 반환 타입 ", e);
            throw e;
        }
    }
}
