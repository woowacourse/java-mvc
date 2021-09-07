package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.InvocationTargetException;

public interface HandlerMapping {

    void initialize() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

    Object getHandler(HttpServletRequest request);
}
