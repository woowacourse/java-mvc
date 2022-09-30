package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

public interface Handleable {
    Object handle(HttpServletRequest request, HttpServletResponse response)
            throws InvocationTargetException, IllegalAccessException;
}
