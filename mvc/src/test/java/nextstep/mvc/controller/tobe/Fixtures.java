package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

public class Fixtures {

    public Fixtures() {
    }

    public Method getHandlerMethod(Object handler, String name) {
        try {
            return handler.getClass().getDeclaredMethod(name,
                    HttpServletRequest.class, HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            throw new NoSuchElementException("해당 메소드가 없습니다.");
        }
    }

    public Object getInstance(Method method) {
        try {
            return method.getDeclaringClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("해당 메소드의 클래스를 찾을 수 없습니다.");
        }
    }
}
