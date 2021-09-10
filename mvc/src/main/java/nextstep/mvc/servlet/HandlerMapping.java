package nextstep.mvc.servlet;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.InvocationTargetException;

public interface HandlerMapping {

    void initialize() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;

    Object getHandler(HttpServletRequest request);
}
