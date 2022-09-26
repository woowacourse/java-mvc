package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

public interface HandlerMapping {

    void initialize();

    Object getHandler(HttpServletRequest request);
}
