package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.InvocationTargetException;

public interface HandlerMapping {

    void initialize() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

    Class<?> getHandler(final HttpServletRequest request);
}
