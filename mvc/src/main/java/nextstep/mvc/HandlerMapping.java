package nextstep.mvc;

import java.lang.reflect.InvocationTargetException;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    Object getHandler(HttpServletRequest request);
}
