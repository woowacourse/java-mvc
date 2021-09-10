package nextstep.mvc;

import java.lang.reflect.InvocationTargetException;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

    Object getHandler(HttpServletRequest request);
}
