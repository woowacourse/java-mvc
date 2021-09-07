package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize() throws ReflectiveOperationException;

    Object getHandler(HttpServletRequest request);
}
